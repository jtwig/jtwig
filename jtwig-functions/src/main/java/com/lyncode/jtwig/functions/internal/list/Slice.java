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
import com.lyncode.jtwig.functions.util.ObjectIterator;

import java.util.ArrayList;
import java.util.List;

import static com.lyncode.jtwig.functions.util.Requirements.isArray;
import static com.lyncode.jtwig.functions.util.Requirements.requires;
import static java.lang.Math.min;
import static org.hamcrest.CoreMatchers.*;

public class Slice implements Function {
    @Override
    public Object execute(Object... arguments) throws FunctionException {
        requires(arguments)
                .withNumberOfArguments(equalTo(3))
                .withArgument(0, anyOf(instanceOf(String.class), instanceOf(Iterable.class), isArray()))
                .withArgument(1, instanceOf(Integer.class))
                .withArgument(2, instanceOf(Integer.class));

        int begin = (Integer) arguments[1];
        int length = (Integer) arguments[2];

        if (arguments[0] instanceof String) {
            String input = (String) arguments[0];
            if (input.length() < begin) return "";
            return input.substring(begin, min(input.length(), begin + length));
        }

        ObjectIterator iterator = new ObjectIterator(arguments[0]);
        List list = new ArrayList();
        int i = 0;
        while (iterator.hasNext()) {
            if (i >= begin && i < begin + length)
                    list.add(iterator.next());
            else
                iterator.next();
            i++;
        }

        if (arguments[0] instanceof Iterable)
            return list;
        else
            return list.toArray();
    }
}
