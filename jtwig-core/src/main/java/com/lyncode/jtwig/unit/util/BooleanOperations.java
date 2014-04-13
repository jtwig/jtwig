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

package com.lyncode.jtwig.unit.util;

import com.lyncode.jtwig.types.Undefined;

import java.util.Map;

public class BooleanOperations {
    public static boolean and (Object a, Object b) {
        return isTrue(a) && isTrue(b);
    }

    public static boolean or (Object a, Object b) {
        return isTrue(a) || isTrue(b);
    }

    public static boolean not (Object a) {
        return !isTrue(a);
    }



    public static boolean isTrue(Object obj) {
        if (obj != null) {
            if (obj instanceof Undefined) return false;
            if (obj instanceof Boolean) return (Boolean) obj;
            if (obj instanceof Integer) return ((Integer) obj) != 0;
            if (obj instanceof Double) return ((Double) obj) != 0;
            if (obj instanceof Iterable) return ((Iterable) obj).iterator().hasNext();
            if (obj instanceof Map) return !((Map) obj).isEmpty();
            if (obj.getClass().isArray()) return ((Object[]) obj).length > 0;
            return true;
        }
        return false;
    }
}
