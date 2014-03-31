package com.lyncode.jtwig.tree.helper;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * Created by rsilva on 3/25/14.
 */
public class RenderIndex {

    private final int[] mIndex;

    public static RenderIndex newIndex() {
        return new RenderIndex(ArrayUtils.add(new int[0], 0));
    }

    public boolean isRoot() {
        return mIndex.length == 1 && mIndex[0] == 0;
    }

    public boolean isLeft() {
        return mIndex[mIndex.length - 1] == 0;
    }

    public RenderIndex clone() {
        return new RenderIndex(getIndex());
    }

    public RenderIndex right() {
        int[] newIndex = ArrayUtils.clone(mIndex);
        newIndex[newIndex.length - 1]++;
        return new RenderIndex(newIndex);
    }

    public RenderIndex left() {
        return new RenderIndex(ArrayUtils.add(getIndex(), 0));
    }

    public RenderIndex previous() {
        int[] newIndex = ArrayUtils.clone(mIndex);
        if (isLeft()) {
            newIndex = ArrayUtils.remove(mIndex, mIndex.length - 1);
        } else {
            newIndex[newIndex.length - 1]--;
            newIndex = ArrayUtils.add(newIndex, 0);
        }
        return new RenderIndex(newIndex);
    }

    private RenderIndex(int[] index) {
        mIndex = index;
    }

    private int[] getIndex() {
        return mIndex;
    }

    @Override
    public int hashCode() {
        return Arrays.toString(mIndex).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RenderIndex that = (RenderIndex) o;

        if (!Arrays.equals(mIndex, that.mIndex)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "RenderIndex{" +
                "mIndex=" + Arrays.toString(mIndex) +
                '}';
    }
}
