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

package org.jtwig.util;

import org.apache.commons.lang3.ObjectUtils;
import static org.jtwig.util.MathOperations.*;

public class RelationalOperations {
    public static boolean gt (Object a, Object b) {
        if (isNumeric(a) && isNumeric(b)) {
            return toDouble(a) > toDouble(b);
        }
        if ((a.getClass().isAssignableFrom(b.getClass())
                || b.getClass().isAssignableFrom(a.getClass()))
                && a instanceof Comparable && b instanceof Comparable) {
            return ObjectUtils.compare((Comparable)a, (Comparable)b) > 0;
        }
        return false;
    }
    public static boolean gte (Object a, Object b) {
        if (isNumeric(a) && isNumeric(b)) {
            return toDouble(a) >= toDouble(b);
        }
        if ((a.getClass().isAssignableFrom(b.getClass())
                || b.getClass().isAssignableFrom(a.getClass()))
                && a instanceof Comparable && b instanceof Comparable) {
            return ObjectUtils.compare((Comparable)a, (Comparable)b) >= 0;
        }
        return false;
    }
    public static boolean lt (Object a, Object b) {
        if (isNumeric(a) && isNumeric(b)) {
            return toDouble(a) < toDouble(b);
        }
        if ((a.getClass().isAssignableFrom(b.getClass())
                || b.getClass().isAssignableFrom(a.getClass()))
                && a instanceof Comparable && b instanceof Comparable) {
            return ObjectUtils.compare((Comparable)a, (Comparable)b) < 0;
        }
        return false;
    }
    public static boolean lte (Object a, Object b) {
        if (isNumeric(a) && isNumeric(b)) {
            return toDouble(a) <= toDouble(b);
        }
        if ((a.getClass().isAssignableFrom(b.getClass())
                || b.getClass().isAssignableFrom(a.getClass()))
                && a instanceof Comparable && b instanceof Comparable) {
            return ObjectUtils.compare((Comparable)a, (Comparable)b) <= 0;
        }
        return false;
    }

    public static boolean eq (Object a, Object b) {
        // Using the same behaviour for Jtwig as we retrieve in twig
        if (a == null)
            return b == null || ((b instanceof Number) && isZero((Number) b));
        if (b == null)
            return (a instanceof Number) && isZero((Number) a);
        return a.toString().equals(b.toString());
    }

    public static boolean isZero(Number number) {
        return number.doubleValue() == 0;
    }
}
