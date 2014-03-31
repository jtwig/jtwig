package com.lyncode.jtwig.tree.helper;

import java.io.IOException;
import java.util.HashMap;

public class MultiOuputStream {

    private HashMap<RenderIndex, SingleOuputStream> mSingleOuputStreamTreeMap;

    public MultiOuputStream() {
        mSingleOuputStreamTreeMap = new HashMap<>();
    }

    public SingleOuputStream get(RenderIndex index) {
        return mSingleOuputStreamTreeMap.get(index);
    }

    public int size() {
        return mSingleOuputStreamTreeMap.size();
    }

    public MultiOuputStream close(RenderIndex index) throws IOException {
        if (mSingleOuputStreamTreeMap.containsKey(index)) {
            SingleOuputStream value = mSingleOuputStreamTreeMap.get(index);
            value.close();
        }
        return this;
    }

    public MultiOuputStream waitOrder(RenderIndex index) throws IOException {
        if (mSingleOuputStreamTreeMap.containsKey(index)) {
            SingleOuputStream value = mSingleOuputStreamTreeMap.get(index);
            value.close();
            value.setStreamState(SingleOuputStream.StreamState.WAITING);
        }
        return this;
    }

    public MultiOuputStream merged(RenderIndex index) throws IOException {
        if (mSingleOuputStreamTreeMap.containsKey(index)) {
            SingleOuputStream value = mSingleOuputStreamTreeMap.get(index);
            value.finalize();
            mSingleOuputStreamTreeMap.remove(index);
        }
        return this;
    }

    public MultiOuputStream newStream(RenderIndex index, SingleOuputStream singleOuputStream) {
        mSingleOuputStreamTreeMap.put(index, singleOuputStream);
        return this;
    }

    public MultiOuputStream newStream(RenderIndex index) {
        return newStream(index, new SingleOuputStream());
    }

    public boolean isClosed(RenderIndex index) {
        if (mSingleOuputStreamTreeMap.containsKey(index)) {
            return mSingleOuputStreamTreeMap.get(index).isClosed();
        }
        return false;
    }

    public boolean isOpen(RenderIndex index) {
        if (mSingleOuputStreamTreeMap.containsKey(index)) {
            return mSingleOuputStreamTreeMap.get(index).isOpen();
        }
        return false;
    }

    public boolean isWaitingOrder(RenderIndex index) {
        if (mSingleOuputStreamTreeMap.containsKey(index)) {
            return mSingleOuputStreamTreeMap.get(index).isWaiting();
        }
        return false;
    }

}
