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

package com.lyncode.jtwig.tree.value;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.tree.api.Calculable;
import com.lyncode.jtwig.tree.helper.ElementList;

import java.util.ArrayList;
import java.util.List;

public class IntegerList extends ElementList implements Calculable {
    private final int start;
    private final int end;

    private static Integer[] toList(int start, int end) {
        List<Integer> list = new ArrayList<Integer>();
        if (end < start)
            for (int i = end; i >= start; i--)
                list.add(i);
        else
            for (int i = start; i<=end; i++)
                list.add(i);

        return list.toArray(new Integer[list.size()]);
    }

    public IntegerList(int start, int end) {
        super(toList(start, end));
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        return getList();
    }
}
