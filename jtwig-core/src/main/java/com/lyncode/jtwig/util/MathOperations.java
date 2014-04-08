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

package com.lyncode.jtwig.util;

public class MathOperations {
    public static Object sum (Object a, Object b) {
        if (areDouble(a, b)) return toDouble(a) + toDouble(b);
        else return toInt(a) + toInt(b);
    }

    public static Object sub (Object a, Object b) {
        if (areDouble(a, b)) return toDouble(a) - toDouble(b);
        else return toInt(a) - toInt(b);
    }

    public static Object mod (Object a, Object b) {
        if (areDouble(a, b)) return toDouble(a) % toDouble(b);
        else return toInt(a) % toInt(b);
    }

    public static Object mul (Object a, Object b) {
        if (areDouble(a, b)) return toDouble(a) * toDouble(b);
        else return toInt(a) * toInt(b);
    }

    public static Object div (Object a, Object b) {
        if (areDouble(a, b)) return toDouble(a) / toDouble(b);
        else return toInt(a) / toInt(b);
    }

    public static double toDouble (Object obj) {
        return ((Number) obj).doubleValue();
    }

    public static int toInt (Object obj) {
        return ((Number) obj) .intValue();
    }

    public static boolean areDouble(Object... list) {
        for (Object obj : list)
            if (obj instanceof Double)
                return true;

        return false;
    }
    public static boolean areInt(Object... list) {
        for (Object obj : list)
            if (obj instanceof Integer)
                return true;

        return false;
    }
}
