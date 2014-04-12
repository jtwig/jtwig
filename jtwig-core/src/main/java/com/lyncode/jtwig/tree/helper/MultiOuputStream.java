package com.lyncode.jtwig.tree.helper;

import java.io.IOException;
import java.util.HashMap;

public class MultiOuputStream {

    private HashMap<RenderIndex, SingleOuputStream> mStreamMap;

    public MultiOuputStream() {
        mStreamMap = new HashMap<>();
    }

    public SingleOuputStream get(RenderIndex index) {
        return mStreamMap.get(index);
    }

    public int size() {
        return mStreamMap.size();
    }

    public MultiOuputStream close(RenderIndex index) throws IOException {
        if (mStreamMap.containsKey(index)) {
            mStreamMap.get(index).close();
        }
        return this;
    }

    public MultiOuputStream waitOrder(RenderIndex index) throws IOException {
        if (mStreamMap.containsKey(index)) {
            SingleOuputStream value = mStreamMap.get(index);
            value.close();
            value.setStreamState(SingleOuputStream.StreamState.WAITING);
        }
        return this;
    }

    public MultiOuputStream merged(RenderIndex index) throws IOException {
        if (mStreamMap.containsKey(index)) {
            mStreamMap.get(index).finalize();
            mStreamMap.remove(index);
        }
        return this;
    }

    public MultiOuputStream newStream(RenderIndex index, SingleOuputStream singleOuputStream) {
        mStreamMap.put(index, singleOuputStream);
        return this;
    }

    public MultiOuputStream newStream(RenderIndex index) {
        return newStream(index, new SingleOuputStream());
    }

    public boolean isClosed(RenderIndex index) {
        if (mStreamMap.containsKey(index)) {
            return mStreamMap.get(index).isClosed();
        }
        return false;
    }

    public boolean isOpen(RenderIndex index) {
        if (mStreamMap.containsKey(index)) {
            return mStreamMap.get(index).isOpen();
        }
        return false;
    }

    public boolean isWaitingOrder(RenderIndex index) {
        if (mStreamMap.containsKey(index)) {
            return mStreamMap.get(index).isWaiting();
        }
        return false;
    }

    @Override
    public String toString() {
        return mStreamMap.keySet().toString();
    }
}
