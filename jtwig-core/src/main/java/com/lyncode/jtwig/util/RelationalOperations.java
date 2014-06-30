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

package com.lyncode.jtwig.util;

import static com.lyncode.jtwig.util.MathOperations.*;

public class RelationalOperations {
    public static boolean gt (Object a, Object b) {
        if (areDouble(a, b))
            return toDouble(a) > toDouble(b);
        return toInt(a) > toInt(b);
    }
    public static boolean gte (Object a, Object b) {
        if (areDouble(a, b))
            return toDouble(a) >= toDouble(b);
        return toInt(a) >= toInt(b);
    }
    public static boolean lt (Object a, Object b) {
        if (areDouble(a, b))
            return toDouble(a) < toDouble(b);
        return toInt(a) < toInt(b);
    }
    public static boolean lte (Object a, Object b) {
        if (areDouble(a, b))
            return toDouble(a) <= toDouble(b);
        return toInt(a) <= toInt(b);
    }

    public static boolean eq (Object a, Object b) {
        // Using the same behaviour for Jtwig as we get in twig
        if (a == null)
            return b == null || ((b instanceof Number) && isZero((Number) b));
        if (b == null)
            return (a instanceof Number) && isZero((Number) a);
        if (a instanceof Long)
            return (b instanceof Number) && ((Long)a).equals(((Number)b).longValue());
        if (b instanceof Long)
            return (a instanceof Number) && ((Long)b).equals(((Number)a).longValue());
        return a.equals(b);
    }

    public static boolean isZero(Number number) {
        return number.doubleValue() == 0;
    }
}
