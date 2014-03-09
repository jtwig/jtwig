package com.lyncode.jtwig.tree.helper;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by rsilva on 3/9/14.
 */
public class RenderStream {

    private OutputStream outputStream;

    public RenderStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public RenderStream write(byte[] bytes) throws IOException {
        outputStream.write(bytes);
        return this;
    }

    @Override
    public String toString() {
        return outputStream.toString();
    }
}
