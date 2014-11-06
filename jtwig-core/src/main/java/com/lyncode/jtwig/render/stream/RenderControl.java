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

package com.lyncode.jtwig.render.stream;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RenderControl {

    private final AtomicInteger mStackCounter;
    private final Semaphore mSemaphore;
    private final ReentrantReadWriteLock mLock;

    public RenderControl() {
        mStackCounter = new AtomicInteger(0);
        mSemaphore = new Semaphore(0);
        mLock = new ReentrantReadWriteLock();
    }

    public void waitFinish() throws InterruptedException {
        while (mStackCounter.get() > 0) {
            mSemaphore.acquire();
        }
    }

    public void push() {
        mStackCounter.incrementAndGet();
    }

    public void cancel() {
        int value = mStackCounter.getAndSet(0);
        if (value <= 0) {
            mSemaphore.release();
        }
    }

    public void poll() {
        int value = mStackCounter.decrementAndGet();
        if (value <= 0) {
            mSemaphore.release();
        }
    }

    public void lockWrite() {
        this.mLock.readLock().lock();
    }

    public void unlockWrite() {
        this.mLock.readLock().unlock();
    }

    public void lockChange() {
        this.mLock.writeLock().lock();
    }

    public void unlockChange() {
        this.mLock.writeLock().unlock();
    }
}
