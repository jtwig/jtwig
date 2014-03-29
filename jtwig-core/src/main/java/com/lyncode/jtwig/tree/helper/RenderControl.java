package com.lyncode.jtwig.tree.helper;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by rsilva on 3/23/14.
 */
public class RenderControl {

    private AtomicInteger mStackCounter;
    private Semaphore mSemaphore;

    public RenderControl() {
        mStackCounter = new AtomicInteger(0);
        mSemaphore = new Semaphore(0);
    }

    public void waitFinish() throws InterruptedException {
        while (mStackCounter.get() > 0) {
            mSemaphore.acquire();
        }
    }

    public void push() {
        mStackCounter.incrementAndGet();
    }

    public void poll() {
        int value = mStackCounter.decrementAndGet();
        if (value <= 0) {
            mSemaphore.release();
        }
    }

}
