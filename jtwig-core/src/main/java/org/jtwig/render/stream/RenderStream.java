/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.render.stream;

import org.jtwig.content.api.Renderable;
import org.jtwig.exception.RenderException;
import org.jtwig.render.RenderContext;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.jtwig.Environment;

public class RenderStream {

    private static ExecutorService sExecutor = null;

    private static void initExecutorService(Environment env) {
        if (sExecutor == null) {
            sExecutor = new ThreadPoolExecutor(env.getConfiguration().getMinThreads(), env.getConfiguration().getMaxThreads(),
                                               env.getConfiguration().getKeepAliveTime(), TimeUnit.SECONDS,
                                               new SynchronousQueue<Runnable>());
        }
    }

    private final OutputStream mRootOutputStream;
    private final MultiOuputStream mMultiStream;
    private final RenderControl mControl;
    private final Environment env;
    private RenderIndex mIndex;

    private RenderStream(MultiOuputStream multiStream, OutputStream stream, RenderIndex dIndex,
                         RenderControl renderControl, Environment env) {
        mMultiStream = multiStream;
        mRootOutputStream = stream;
        mIndex = dIndex;
        mControl = renderControl;
        this.env = env;
        initExecutorService(env);

        if (mIndex != null) {
            SingleOuputStream.Builder builder = SingleOuputStream.builder().withInheritedStream(true);
            if (mIndex.isMostLeft()) {
                builder.withByteStream(false);
                builder.withStream(stream);
            } else if (mIndex.isLeft()) {
                builder.withByteStream(true);
                builder.withStream(multiStream.get(mIndex.previous()).getStream());
            }
            multiStream.addStream(mIndex, builder.build());
        }
    }

    public RenderStream(OutputStream outputStream, Environment env) {
        this(new MultiOuputStream(), outputStream, null,
             new RenderControl(), env);
    }

    public RenderStream renderConcurrent(final Renderable content, final RenderContext context) {
        try {
            mControl.push();
            sExecutor.execute(new RenderTask(content, context));
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
        mControl.lockWrite();
        getOuputStream().write(bytes);
        mControl.unlockWrite();
        return this;
    }

    public RenderStream close() throws IOException {
        mControl.lockWrite();
        if (mIndex != null) {
            mMultiStream.close(mIndex);
        }
        mControl.unlockWrite();
        return this;
    }

    public RenderStream fork() throws IOException {
        mControl.lockChange();
        if (mIndex == null) {
            mIndex = RenderIndex.newIndex(); //create new index for the first time we have a concurrent stream
        }
        mMultiStream.waitOrder(mIndex); // this index will wait for his left children

        RenderIndex forkedIndex = mIndex.left(); // forked will render the left children
        mIndex = mIndex.right(); // this will continue with the right children
        mMultiStream.addStream(mIndex);

        RenderStream forkedStream = new RenderStream(mMultiStream, getRootOutputStream(), forkedIndex, mControl,
                                                     env);
        mControl.unlockChange();
        return forkedStream;
    }

    public RenderStream merge() throws IOException {
        mControl.lockChange();
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
                    mControl.unlockChange();
                    return this;
                }
                index = previous;
                previous = index.previous();
            }
            if (getRootOutputStream() == null) {
                mMultiStream.merged(toMerge);
                mControl.unlockChange();
                throw new IOException("Output stream not available");
            }
            getRootOutputStream().write(mMultiStream.get(toMerge).toByteArray());
            mMultiStream.merged(toMerge);
        }
        mControl.unlockChange();
        return this;
    }

    private void mergeStreams(RenderIndex destinationIndex, RenderIndex originIndex) throws IOException {
        SingleOuputStream singleOuputStream = mMultiStream.get(destinationIndex);
        mMultiStream.get(originIndex).writeTo(singleOuputStream.getStream());
    }

    private OutputStream getRootOutputStream() {
        return mRootOutputStream;
    }

}
