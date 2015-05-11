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

package org.jtwig.render.stream;

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

    /**
     * Add the Output Stream as parameter to index
     *
     * @param index             destination index
     * @param singleOuputStream output stream to add
     * @return this object
     */
    public MultiOuputStream addStream(RenderIndex index, SingleOuputStream singleOuputStream) {
        mStreamMap.put(index, singleOuputStream);
        return this;
    }

    /**
     * Add a new Output Stream to RenderIndex
     *
     * @param index destination index
     * @return this object
     */
    public MultiOuputStream addStream(RenderIndex index) {
        return addStream(index, SingleOuputStream.builder().build());
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
