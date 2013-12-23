/**
 * Copyright 2012 Lyncode
 *
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

package com.lyncode.jtwig.functions.internal.list;

import com.lyncode.jtwig.functions.Function;
import com.lyncode.jtwig.functions.exceptions.FunctionException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Batch implements Function {
    @Override
    public Object execute(Object... arguments) throws FunctionException {
        if (arguments.length < 2 || arguments.length > 3) throw new FunctionException("Invalid number of arguments");
        if (!(arguments[1] instanceof Integer)) throw new FunctionException("Second arguments must be an integer");
        ObjectIterator iterator = new ObjectIterator(arguments[0]);
        int groupSize = (Integer) arguments[1];
        boolean end = false;
        List<List<Object>> result = new ArrayList<List<Object>>();
        while (iterator.hasNext()) {
            List<Object> batch = new ArrayList<Object>();
            for (int i=0;i<groupSize;i++) {
                if (iterator.hasNext())
                    batch.add(iterator.next());
                else if (arguments.length == 3)
                    batch.add(arguments[2]);
            }
            result.add(batch);
        }

        return result;
    }


    public static class ObjectIterator {
        private Iterator<Object> iterator = null;
        private int size;

        public ObjectIterator(Object context) {
            List<Object> list = new ArrayList<Object>();
            if (context != null) {
                if (context instanceof Iterable) {
                    Iterator iterator = ((Iterable) context).iterator();
                    while (iterator.hasNext())
                        list.add(iterator.next());
                } else if (context.getClass().isArray()) {
                    Object[] objects = (Object[]) context;
                    for (Object obj : objects)
                        list.add(obj);
                } else list.add(context);
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
    }
}
