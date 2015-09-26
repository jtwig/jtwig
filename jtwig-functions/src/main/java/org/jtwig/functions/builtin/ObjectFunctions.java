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

public class ObjectFunctions {
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
