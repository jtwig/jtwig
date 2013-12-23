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

public class CharacterList extends ElementList implements Calculable {

    private static Character[] toList(Character start, Character end) {
        List<Character> list = new ArrayList<Character>();
        if (end < start)
            for (char i = end; i >= start; i--)
                list.add(i);
        else
            for (char i = start; i<=end; i++)
                list.add(i);

        return list.toArray(new Character[list.size()]);
    }

    private char start;
    private char end;

    public CharacterList (char start, char end) {
        super(toList(start, end));
        this.start = start;
        this.end = end;
    }

    public char getStart() {
        return start;
    }

    public char getEnd() {
        return end;
    }

    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        return getList();
    }
}
