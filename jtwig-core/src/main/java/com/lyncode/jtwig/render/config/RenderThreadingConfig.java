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

package com.lyncode.jtwig.render.config;

public class RenderThreadingConfig {

    private int mMinThreads;
    private int mMaxThreads;
    private long mKeepAliveTime;

    public RenderThreadingConfig() {
        mMinThreads = 20;
        mMaxThreads = 100;
        mKeepAliveTime = 60L;
    }

    public int minThreads() {
        return mMinThreads;
    }

    public void minThreads(int minThreads) {
        mMinThreads = minThreads;
    }

    public int maxThreads() {
        return mMaxThreads;
    }

    public void maxThreads(int maxThreads) {
        mMaxThreads = maxThreads;
    }

    public long keepAliveTime() {
        return mKeepAliveTime;
    }

    /**
     * In Seconds
     *
     * @param keepAliveTime
     */
    public void keepAliveTime(long keepAliveTime) {
        mKeepAliveTime = keepAliveTime;
    }
}
