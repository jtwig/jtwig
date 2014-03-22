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

package com.lyncode.jtwig.functions.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ObjectIterator {
    private List<Object> list = new ArrayList<Object>();
    private Iterator<Object> iterator = null;
    private int size;

    public ObjectIterator(Object context) {
        list = new ArrayList<Object>();
        if (context != null) {
            if (context instanceof Iterable) {
                Iterator iterator = ((Iterable) context).iterator();
                while (iterator.hasNext())
                    list.add(iterator.next());
            } else if (context.getClass().isArray()) {
                Object[] objects = (Object[]) context;
                for (Object obj : objects)
                    list.add(obj);
            } else if (context instanceof Map) {
                list.addAll(((Map) context).keySet());
            }
            else list.add(context);
        }
        size = list.size();
        iterator = list.iterator();
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public Object next() {
        return iterator.next();
    }

    public int size() {
        return size;
    }

    public boolean contains(Object item) {
        return list.contains(item);
    }
}
