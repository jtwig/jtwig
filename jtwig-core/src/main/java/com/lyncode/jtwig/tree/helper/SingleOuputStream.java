package com.lyncode.jtwig.tree.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by rsilva on 3/25/14.
 */
public class SingleOuputStream extends OutputStream {

    private ByteArrayOutputStream mByteArrayOutputStream;
    private StreamState mStreamState;

    public SingleOuputStream() {
        this(new ByteArrayOutputStream());
    }

    @Override
    public void write(int b) throws IOException {

    }

    public SingleOuputStream(ByteArrayOutputStream byteArrayOutputStream) {
        mByteArrayOutputStream = byteArrayOutputStream;
        mStreamState = StreamState.OPEN;
    }

    public byte[] toByteArray() {
        return mByteArrayOutputStream.toByteArray();
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        mByteArrayOutputStream.write(bytes);
    }

    public void setStreamState(StreamState streamState) {
        mStreamState = streamState;
    }

    public void close() throws IOException {
        mStreamState = StreamState.CLOSED;
        mByteArrayOutputStream.flush();
    }

    public void finalize() throws IOException {
        close();
        mByteArrayOutputStream.close();
        mByteArrayOutputStream = null;
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

    @Override
    public String toString() {
        return "SingleOuputStream{" +
                "mByteArrayOutputStream=" + new String(mByteArrayOutputStream.toByteArray()) +
                '}';
    }
}
