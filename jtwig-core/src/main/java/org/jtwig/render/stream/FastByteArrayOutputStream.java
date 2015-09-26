package org.jtwig.render.stream;
/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */

import java.io.IOException;
import java.io.OutputStream;

/**
 * A speedy implementation of ByteArrayOutputStream. It's not synchronized, and it
 * does not copy buffers when it's expanded. There's also no copying of the internal buffer
 * if it's contents is extracted with the writeTo(stream) method.
 *
 * @author Rickard ?berg
 * @author Brat Baker (Atlassian)
 * @author Alexey
 * @version $Date: 2008-01-19 10:09:56 +0800 (Sat, 19 Jan 2008) $ $Id: FastByteArrayOutputStream.java 3000 2008-01-19 02:09:56Z tm_jee $
 */
public class FastByteArrayOutputStream extends OutputStream {

    // Static --------------------------------------------------------
    private static final int DEFAULT_BLOCK_SIZE = 8192;

    // Attributes ----------------------------------------------------
    // internal buffer
    private final byte[] buffer;

    // is the stream closed?
    private boolean closed;
    private int blockSize;
    private int index;
    private int size;

    // Constructors --------------------------------------------------
    public FastByteArrayOutputStream() {
        this(DEFAULT_BLOCK_SIZE);
    }

    public FastByteArrayOutputStream(int aSize) {
        blockSize = aSize;
        buffer = new byte[blockSize];
    }

    public int getSize() {
        return size + index;
    }

    @Override
    public void close() {
        closed = true;
    }

    public byte[] toByteArray() {
        byte[] data = new byte[getSize()];

        // Check if we have a list of buffers
        int pos = 0;

        // write the internal buffer directly
        System.arraycopy(buffer, 0, data, pos, index);

        return data;
    }

    @Override
    public void write(int datum) throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        } else {
            // store the byte
            buffer[index++] = (byte) datum;
        }
    }

    // Public
    public void writeTo(OutputStream out) throws IOException {
        // write the internal buffer directly
        out.write(buffer, 0, index);
    }
}
