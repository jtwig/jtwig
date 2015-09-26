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

package org.jtwig.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jtwig.util.TypeUtil;

public class ObjectIterator implements Iterable<Object>, Iterator<Object> {
    private final List<Object> list;
    private final Iterator<Object> iterator;
    private final int size;

    public ObjectIterator(Object context) {
        list = new ArrayList<>();
        if (context != null) {
            if (context instanceof Iterable) {
                Iterator it = ((Iterable) context).iterator();
                while (it.hasNext())
                    list.add(it.next());
            } else if (context.getClass().isArray()) {
                Object[] objects = (Object[]) context;
                for (Object obj : objects)
                    list.add(obj);
            } else if (context instanceof Map) {
                list.addAll(((Map) context).values());
            }
            else list.add(context);
        }
        size = list.size();
        iterator = list.iterator();
    }
    
    //~ Iterable impl ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public Iterator<Object> iterator() {
        return this;
    }

    //~ Iterator impl ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Object next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    //~ Helpers ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public int size() {
        return size;
    }

    public boolean contains(Object item) {
        return list.contains(item);
    }
    
    public boolean containsLoose(Object item) {
        BigDecimal itemDecimal = TypeUtil.toDecimal(item);
        Long itemLong = TypeUtil.toLong(item);
        Boolean itemBool = TypeUtil.toBoolean(item);
        
        // Use a new iterator so that we don't confuse anything
        Iterator<Object> it = list.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj == null) {
                if (item == null) {
                    return true;
                }
                continue;
            }
            if (TypeUtil.isDecimal(obj)) {
                if (TypeUtil.toDecimal(obj).equals(itemDecimal)) {
                    return true;
                }
                continue;
            }
            if (TypeUtil.isLong(obj)) {
                if (TypeUtil.toLong(obj).equals(itemLong)) {
                    return true;
                }
                continue;
            }
            if (TypeUtil.isBoolean(obj) || TypeUtil.isBoolean(item)) {
                if (TypeUtil.toBoolean(obj).equals(itemBool)) {
                    return true;
                }
                continue;
            }
            if (obj.toString().contains(item.toString())) {
                return true;
            }
        }
        return false;
    }
}