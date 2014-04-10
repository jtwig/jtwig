package com.lyncode.jtwig.tree.helper;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.tree.api.Content;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RenderStream {

    private final static ExecutorService sExecutor;
    private final OutputStream mRootOutputStream;
    private final MultiOuputStream mMultiStream;
    private RenderIndex mIndex;
    private final ReentrantReadWriteLock mLock;
    private final RenderControl mControl;

    static {
        sExecutor = new ThreadPoolExecutor(20, Integer.MAX_VALUE,
                                           60L, TimeUnit.SECONDS,
                                           new SynchronousQueue<Runnable>());
    }

    private RenderStream(MultiOuputStream multiStream, OutputStream stream, RenderIndex dIndex,
                         ReentrantReadWriteLock rwl, RenderControl renderControl) {
        this.mMultiStream = multiStream;
        this.mRootOutputStream = stream;
        this.mIndex = dIndex;
        this.mControl = renderControl;
        this.mLock = rwl;

        if (mIndex != null) {
            if (mIndex.isMostLeft()) {
                multiStream.newStream(mIndex, new SingleOuputStream(stream, true, false));
            } else if (mIndex.isLeft()) {
                multiStream.newStream(mIndex, new SingleOuputStream(
                        multiStream.get(mIndex.previous()).getStream(), true, true));
            } else {
                multiStream.newStream(mIndex);
            }
        }
    }

    public RenderStream(OutputStream outputStream) {
        this(new MultiOuputStream(), outputStream, null,
             new ReentrantReadWriteLock(),
             new RenderControl());
    }

    public RenderStream renderConcurrent(final Content content, final JtwigContext context) throws RenderException {
        try {
            mControl.push();
            sExecutor.execute(new RenderTask(fork(), content, context));
        } catch (IOException e) {
            throw new RenderException(e);
        } catch (OutOfMemoryError e) {
            sExecutor.shutdownNow();
            mControl.cancel();
        }
        return this;
    }

    public RenderStream waitForExecutorCompletion() throws RenderException {
        try {
            mControl.waitFinish();
        } catch (InterruptedException e) {
            throw new RenderException(e);
        }
        return this;
    }

    public RenderStream notifyTaskFinished() {
        mControl.poll();
        return this;
    }

    public OutputStream getOuputStream() {
        if (mIndex != null) {
            return mMultiStream.get(mIndex);
        } else {
            return getRootOutputStream();
        }
    }

    public RenderStream write(byte[] bytes) throws IOException {
        lockWrite();
        getOuputStream().write(bytes);
        unlockWrite();
        return this;
    }

    public RenderStream close() throws IOException {
        lockWrite();
        if (mIndex != null) {
            mMultiStream.close(mIndex);
        }
        unlockWrite();
        return this;
    }

    public RenderStream fork() throws IOException {
        lockChange();
        if (mIndex == null) {
            mIndex = RenderIndex.newIndex(); //create new index for the first time we have a concurrent stream
        }
        mMultiStream.waitOrder(mIndex); // this index will wait for his left children

        RenderIndex forkedIndex = mIndex.left(); // forked will render the left children
        mIndex = mIndex.right(); // this will continue with the right children
        mMultiStream.newStream(mIndex);

        RenderStream forkedStream = new RenderStream(mMultiStream, getRootOutputStream(), forkedIndex,
                                                     mLock, mControl);
        unlockChange();
        return forkedStream;
    }

    public RenderStream merge() throws IOException {
        lockChange();
        if (mIndex != null && mMultiStream.isClosed(mIndex)) {
            RenderIndex index = mIndex.clone();
            RenderIndex previous = index.previous();
            RenderIndex toMerge = index;
            while (!index.isRoot()) {
                if (mMultiStream.isWaitingOrder(
                        previous) && index.isLeft()) {
                    //Close the parent if was waiting for his left children
                    mMultiStream.close(previous);
                }
                if (mMultiStream.isClosed(previous)) {
                    if (toMerge.isRight()) {
                        //only merge if toMerge is a right children, if is the left, is already using the same outputstream
                        mergeStreams(previous, toMerge);
                    }
                    mMultiStream.merged(toMerge);
                    toMerge = previous;
                    // Check if the right child is closed and merge from there
                    if (mMultiStream.isClosed(previous.right())) {
                        mergeStreams(previous, previous.right());
                        mMultiStream.merged(previous.right());
                    }
                } else if (mMultiStream.isOpen(previous) || (mMultiStream.isWaitingOrder(
                        previous) && !index.isLeft())) {
                    unlockChange();
                    return this;
                }
                index = previous;
                previous = index.previous();
            }
            getRootOutputStream().write(mMultiStream.get(toMerge).toByteArray());
            mMultiStream.merged(toMerge);
        }
        unlockChange();
        return this;
    }

    private void mergeStreams(RenderIndex destinationIndex, RenderIndex originIndex) throws IOException {
        SingleOuputStream singleOuputStream = mMultiStream.get(destinationIndex);
        mMultiStream.get(originIndex).writeTo(singleOuputStream.getStream());
    }

    private OutputStream getRootOutputStream() {
        return mRootOutputStream;
    }

    private void lockWrite() {
        this.mLock.readLock().lock();
    }

    private void unlockWrite() {
        this.mLock.readLock().unlock();
    }

    private void lockChange() {
        this.mLock.writeLock().lock();
    }

    private void unlockChange() {
        this.mLock.writeLock().unlock();
    }

    @Override
    public String toString() {
        return getRootOutputStream().toString();
    }

}
