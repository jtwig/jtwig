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

package com.lyncode.jtwig.functions.builtin;

import com.lyncode.jtwig.functions.annotations.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.Parameter;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapFunctions {
    @JtwigFunction(name = "keys")
    public Set keys (@Parameter Map map) {
        return map.keySet();
    }

    @JtwigFunction(name = "length")
    public int length (@Parameter Map input) {
        return input.size();
    }


    @JtwigFunction(name = "first")
    public Object first (@Parameter Map input) {
        if (input.isEmpty()) return null;
        return input.get(input.keySet().iterator().next());
    }
    @JtwigFunction(name = "last")
    public Object last (@Parameter Map input) {
        if (input.isEmpty()) return null;
        Iterator iterator = input.keySet().iterator();
        Object key = iterator.next();
        while (iterator.hasNext()) key = iterator.next();
        return input.get(key);
    }
}
