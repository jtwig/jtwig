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

package com.lyncode.jtwig.tree.helper;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.join;

public class ElementList {
    private List<Object> list;

    public ElementList(Object... list) {
        this.list = new ArrayList<Object>(asList(list));
    }

    public boolean add (Object elem) {
        list.add(elem);
        return true;
    }

    public List<Object> getList() {
        return list;
    }

    public String toString () {
        return toString("[","]");
    }

    public String toString(String start, String end) {
        return start+join(list,",")+end;
    }
}
