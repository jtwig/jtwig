package com.lyncode.jtwig.render.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by rsilva on 3/25/14.
 */
public class SingleOuputStream extends ByteArrayOutputStream {

    private OutputStream mStream;
    private StreamState mStreamState;
    private final boolean mInheritedStream;
    private final boolean mByteStream;

    public SingleOuputStream() {
        this(new FastByteArrayOutputStream(), false, true);
    }

    public SingleOuputStream(OutputStream outputStream, boolean inheritedStream, boolean byteStream) {
        mStream = outputStream;
        mStreamState = StreamState.OPEN;
        mInheritedStream = inheritedStream;
        mByteStream = byteStream;
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

    public void close() throws IOException {
        mStream.flush();
        mStreamState = StreamState.CLOSED;
    }

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
}
