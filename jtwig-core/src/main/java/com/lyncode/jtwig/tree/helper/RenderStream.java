package com.lyncode.jtwig.tree.helper;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.tree.api.Content;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RenderStream {

    private static final int EXECUTOR_AWAIT_TIMEOUT = 15;

    private final OutputStream defaultOutputStream;
    private final MultiOuputStream multiOuputStream;
    private RenderIndex renderStreamIndex;
    private final ExecutorService executorService;
    private final ReentrantReadWriteLock rwl;
    private final RenderControl mRenderControl;

    private RenderStream(MultiOuputStream multiOuputStream, OutputStream defaultOutputStream, RenderIndex defaultIndex,
                         ExecutorService executorService, ReentrantReadWriteLock rwl, RenderControl renderControl) {
        this.multiOuputStream = multiOuputStream;
        if (defaultIndex != null) {
            multiOuputStream.newStream(defaultIndex);
        }
        this.defaultOutputStream = defaultOutputStream;
        this.renderStreamIndex = defaultIndex;
        this.executorService = executorService;
        this.mRenderControl = renderControl;
        this.rwl = rwl;

    }

    public RenderStream(OutputStream outputStream) {
        this(new MultiOuputStream(), outputStream, null, Executors.newCachedThreadPool(),
             new ReentrantReadWriteLock(),
             new RenderControl());
    }

    public RenderStream renderConcurrent(final Content content, final JtwigContext context) throws RenderException {
        try {
            mRenderControl.push();
            executorService.execute(new RenderTask(fork(), content, context));
        } catch (IOException e) {
            throw new RenderException(e);
        }
        return this;
    }

    public RenderStream waitForExecutorCompletion() throws RenderException {
        try {
            mRenderControl.waitFinish();
        } catch (InterruptedException e) {
            throw new RenderException(e);
        }
        return this;
    }

    public RenderStream notifyTaskFinished() {
        mRenderControl.poll();
        return this;
    }

    private OutputStream getOuputStream() {
        if (renderStreamIndex != null) {
            return multiOuputStream.get(renderStreamIndex);
        } else {
            return getDefaultOutputStream();
        }
    }

    public RenderStream write(byte[] bytes) throws IOException {
        getOuputStream().write(bytes);
        close();
        return this;
    }

    public RenderStream close() throws IOException {
        lockOutputWrite();
        if (renderStreamIndex != null) {
            multiOuputStream.close(renderStreamIndex);
        }
        unlockOutputWrite();
        return this;
    }

    public RenderStream block() throws IOException {
        lockOutputWrite();
        if (renderStreamIndex != null) {
            multiOuputStream.close(renderStreamIndex);
        }
        unlockOutputWrite();
        return this;
    }

    public RenderStream fork() throws IOException {
        lockOutputChange();
        if (renderStreamIndex == null) {
            renderStreamIndex = RenderIndex.newIndex();
        }

        multiOuputStream.waitOrder(renderStreamIndex); // this index will wait for his left children

        RenderIndex forkedIndex = renderStreamIndex.left();
        renderStreamIndex = renderStreamIndex.right();

        multiOuputStream.newStream(renderStreamIndex, new SingleOuputStream());
        RenderStream forkedRendersStream = new RenderStream(multiOuputStream, getDefaultOutputStream(), forkedIndex,
                                                            executorService, rwl, mRenderControl);
        unlockOutputChange();
        return forkedRendersStream;
    }

    public RenderStream merge() throws IOException {
        lockOutputChange();
        RenderIndex index = renderStreamIndex.clone();
        if (multiOuputStream.isClosed(index)) {
            RenderIndex previous = index.previous();
            RenderIndex toMerge = index;
            while (!index.isRoot()) {
                if (multiOuputStream.isWaitingOrder(
                        previous) && index.isLeft()) {
                    //Close the parent if was waiting for his left children
                    multiOuputStream.close(previous);
                }
                if (multiOuputStream.isClosed(previous)) {
                    SingleOuputStream previousStream = multiOuputStream.get(previous);
                    SingleOuputStream toMergeStream = multiOuputStream.get(toMerge);
                    previousStream.write(
                            toMergeStream.toByteArray());
                    multiOuputStream.merged(toMerge);
                    toMerge = previous;
                    // Check if the right child is closed and merge from there
                    if (multiOuputStream.isClosed(previous.right())) {
                        multiOuputStream.get(previous).write(
                                multiOuputStream.get(previous.right()).toByteArray());
                        multiOuputStream.merged(previous.right());
                    }
                } else if (multiOuputStream.isOpen(previous) || (multiOuputStream.isWaitingOrder(
                        previous) && !index.isLeft())) {
                    unlockOutputChange();
                    return this;
                }
                index = previous;
                previous = index.previous();
            }

            getDefaultOutputStream().write(multiOuputStream.get(toMerge).toByteArray());
            multiOuputStream.merged(toMerge);
        }
        unlockOutputChange();
        return this;
    }

    private OutputStream getDefaultOutputStream() {
        return defaultOutputStream;
    }

    private void lockOutputWrite() {
        this.rwl.readLock().lock();
    }

    private void unlockOutputWrite() {
        this.rwl.readLock().unlock();
    }

    private void lockOutputChange() {
        this.rwl.writeLock().lock();
    }

    private void unlockOutputChange() {
        this.rwl.writeLock().unlock();
    }

    @Override
    public String toString() {
        return getDefaultOutputStream().toString();
    }

}
