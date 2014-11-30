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

package org.jtwig.functions.builtin;

import org.jtwig.functions.annotations.JtwigFunction;
import org.jtwig.functions.annotations.Parameter;

import java.lang.reflect.Array;

import static org.jtwig.types.Undefined.UNDEFINED;

public class ObjectFunctions {
    @JtwigFunction(name = "default")
    public Object defaultFunction (@Parameter Object input, @Parameter Object defaultValue) {
        if (input == null || input.equals(UNDEFINED))
            return defaultValue;
        else
            return input;
    }


    @JtwigFunction(name = "length")
    public int length (@Parameter Object input) {
        if (input instanceof String)
            return ((String) input).length();
        else
            return 1;
    }


    @JtwigFunction(name = "first")
    public Object first (@Parameter Object input) {
        if (input == null) return input;
        if (input.getClass().isArray()) {
            if (Array.getLength(input) == 0) return null;
            return Array.get(input, 0);
        } else return input;
    }
    @JtwigFunction(name = "last")
    public Object last (@Parameter Object input) {
        if (input == null) return input;
        if (input.getClass().isArray()) {
            int length = Array.getLength(input);
            if (length == 0) return null;
            return Array.get(input, length - 1);
        } else return input;
    }

    @JtwigFunction(name = "toDouble", aliases = { "toFloat" })
    public Double toDouble (@Parameter Object input) {
        return Double.valueOf(input.toString());
    }
    @JtwigFunction(name = "toInt")
    public Integer toInt (@Parameter Object input) {
        if (input instanceof Number)
            return ((Number) input).intValue();
        return Integer.parseInt(input.toString());
    }
}
