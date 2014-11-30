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

public class MathFunctions {
    @JtwigFunction(name = "abs")
    public Double abs (@Parameter Double input) {
        return Math.abs(input);
    }
    @JtwigFunction(name = "abs")
    public Float abs (@Parameter Float input) {
        return Math.abs(input);
    }
    @JtwigFunction(name = "abs")
    public Long abs (@Parameter Long input) {
        return Math.abs(input);
    }
    @JtwigFunction(name = "abs")
    public Integer abs (@Parameter Integer input) {
        return Math.abs(input);
    }
    @JtwigFunction(name = "round")
    public int round (@Parameter Double input, @Parameter String strategy) {
        switch (RoundStrategy.valueOf(strategy.toUpperCase())) {
            case CEIL:
                return (int) Math.ceil(input);
            case FLOOR:
                return (int) Math.floor(input);
            default:
                return (int) Math.round(input);
        }
    }
    @JtwigFunction(name = "round")
    public int round (@Parameter Double input) {
        return round(input, RoundStrategy.COMMON.name());
    }


    public static enum RoundStrategy {
        COMMON,
        CEIL,
        FLOOR
    }
}
