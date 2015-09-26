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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SingleOuputStream extends ByteArrayOutputStream {

    private OutputStream mStream;
    private StreamState mStreamState;
    private final boolean mInheritedStream;
    private final boolean mByteStream;

    private SingleOuputStream(Builder builder) {
        mStream = builder.mStream;
        mInheritedStream = builder.mInheritedStream;
        mByteStream = builder.mByteStream;
        mStreamState = StreamState.OPEN;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        if (mByteStream) {
            ((FastByteArrayOutputStream) mStream).writeTo(out);
        }
    }

    @Override
    public byte[] toByteArray() {
        if (mByteStream) {
            return ((FastByteArrayOutputStream) mStream).toByteArray();
        } else {
            return new byte[0];
        }
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        mStream.write(bytes);
    }

    @Override
    public void flush() throws IOException {
        mStream.flush();
    }

    public void setStreamState(StreamState streamState) {
        mStreamState = streamState;
    }

    @Override
    public void close() throws IOException {
        mStream.flush();
        mStreamState = StreamState.CLOSED;
    }

    @Override
    public void finalize() throws IOException {
        close();
        if (!mInheritedStream) {
            mStream.close();
            mStream = null;
        }
    }

    public boolean isClosed() {
        return mStreamState.equals(StreamState.CLOSED);
    }

    public boolean isOpen() {
        return mStreamState.equals(StreamState.OPEN);
    }

    public boolean isWaiting() {
        return mStreamState.equals(StreamState.WAITING);
    }

    public enum StreamState {
        OPEN,
        WAITING,
        CLOSED
    }

    public OutputStream getStream() {
        return mStream;
    }

    public static final class Builder {

        private OutputStream mStream;
        private boolean mInheritedStream = false;
        private boolean mByteStream = true;

        private Builder() {
        }

        public Builder withStream(OutputStream mStream) {
            this.mStream = mStream;
            return this;
        }

        public Builder withInheritedStream(boolean mInheritedStream) {
            this.mInheritedStream = mInheritedStream;
            return this;
        }

        public Builder withByteStream(boolean mByteStream) {
            this.mByteStream = mByteStream;
            return this;
        }

        public SingleOuputStream build() {
            if (mStream == null) {
                mStream = new FastByteArrayOutputStream();
            }
            return new SingleOuputStream(this);
        }
    }
}
