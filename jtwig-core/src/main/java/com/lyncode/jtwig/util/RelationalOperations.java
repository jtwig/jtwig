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
    public static Object gt (Object a, Object b) {
        if (areDouble(a, b)) return toDouble(a) > toDouble(b);
        else return toInt(a) > toInt(b);
    }
    public static Object gte (Object a, Object b) {
        if (areDouble(a, b)) return toDouble(a) >= toDouble(b);
        else return toInt(a) >= toInt(b);
    }
    public static Object lt (Object a, Object b) {
        if (areDouble(a, b)) return toDouble(a) < toDouble(b);
        else return toInt(a) < toInt(b);
    }
    public static Object lte (Object a, Object b) {
        if (areDouble(a, b)) return toDouble(a) <= toDouble(b);
        else return toInt(a) <= toInt(b);
    }

    public static boolean eq (Object a, Object b) {
        if (a == null) return b == null || b.equals(a);
        return a.equals(b);
    }

    public static boolean neq (Object a, Object b) {
        return !eq(a, b);
    }

}
