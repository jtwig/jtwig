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

package org.jtwig.content.model.tag;

import org.apache.commons.lang3.tuple.MutablePair;

public class WhiteSpaceControl {
    private static final boolean DEFAULT_VALUE = false;
    private MutablePair<Boolean, Boolean> begin = new MutablePair<>();
    private MutablePair<Boolean, Boolean> end = new MutablePair<>();

    public boolean trimBeforeOpen () {
        return get(begin.getLeft());
    }

    public boolean trimAfterOpen () {
        return get(begin.getRight());
    }

    public boolean trimBeforeClose () {
        return get(end.getLeft());
    }

    public boolean trimAfterClose () {
        return get(end.getRight());
    }

    public WhiteSpaceControl trimBeforeOpen(boolean value) {
        begin.setLeft(value);
        return this;
    }

    public WhiteSpaceControl trimAfterOpen(boolean value) {
        begin.setRight(value);
        return this;
    }

    public WhiteSpaceControl trimBeforeClose(boolean value) {
        end.setLeft(value);
        return this;
    }

    public WhiteSpaceControl trimAfterClose(boolean value) {
        end.setRight(value);
        return this;
    }

    private boolean get(Boolean value) {
        if (value != null) return value;
        return DEFAULT_VALUE;
    }
}
