/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.util;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jtwig.types.Undefined;

/**
 * A collection of type conversion and determination functions.
 */
public class TypeUtil {
    public static final String PATTERN_STR = "([\\-+]?([\\d]+)?(\\.[\\d]+)?).*";
    public static final Pattern PATTERN = Pattern.compile("^"+PATTERN_STR+"$");
//    public static final String DECIMAL_PATTERN = "([\\-+]?(?:[\\d]+)?(\\.[\\d]+)?).*";
//    public static final Pattern DECIMAL = Pattern.compile("^"+DECIMAL_PATTERN+"$");
    
    /**
     * Determines the twig-compatible boolean equivalent of the given object.
     * @param obj
     * @return 
     */
    public static Boolean toBoolean(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Undefined) return false;
        if (obj instanceof Boolean) return (Boolean) obj;
        if (obj instanceof Integer) return ((Integer) obj) != 0;
        if (obj instanceof Double) return ((Double) obj) != 0;
        if (obj instanceof Iterable) return ((Iterable) obj).iterator().hasNext();
        if (obj instanceof Map) return !((Map) obj).isEmpty();
        if (obj.getClass().isArray()) return ((Object[]) obj).length > 0;
        if (obj instanceof String && ("0".equals(obj.toString()) || obj.toString().isEmpty())) return false;
        return true;
    }
    /**
     * Determines whether the given object is strictly a boolean value.
     * @param obj
     * @return 
     */
    public static boolean isBoolean(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Boolean) return true;
        return obj instanceof Number;
    }
    /**
     * Determines the whole number equivalent of the given object. Twig does not
     * support converting maps and arrays to whole numbers.
     * @param obj
     * @return 
     */
    public static Long toLong(Object obj) {
        if (obj == null || obj instanceof Undefined) return 0L;
        if (obj instanceof Boolean) return ((Boolean)obj) ? 1L : 0L;
        if (obj instanceof String) obj = toNumber(obj.toString());
        if (obj instanceof Number) return ((Number)obj).longValue();
        // Twig craps out when adding things like maps, arrays, etc. So crap out
        // here until we figure out something better to do.
        if (obj instanceof Iterable || obj instanceof Map
                || obj.getClass().isArray()) {
            throw new IllegalArgumentException();
        }
        return 1L;
    }
    /**
     * Determines whether the given object is strictly a long/int/short/byte.
     * Nulls and objects of types other than string or long/int/short/byte are
     * not considered. Strings are considered longs only if they begin with a
     * single whole number.
     * @param obj
     * @return 
     */
    public static boolean isLong(Object obj) {
        if (obj == null || obj instanceof Undefined || obj instanceof Boolean) {
            return false;
        }
        if (obj instanceof String) {
            if (obj.toString().trim().isEmpty()) {
                return false;
            }
            Matcher matcher = PATTERN.matcher(obj.toString());
            matcher.matches();
            return matcher.group(2) != null && matcher.group(3) == null;
        }
        return obj instanceof Number && !(obj instanceof Double
                || obj instanceof Float || obj instanceof BigDecimal);
    }
    /**
     * Determines the real number equivalent of the given object. Twig does not
     * support converting maps or arrays to real numbers.
     * @param obj
     * @return 
     */
    public static BigDecimal toDecimal(Object obj) {
        if (obj == null || obj instanceof Undefined) return BigDecimal.ZERO;
        if (obj instanceof Boolean) return ((Boolean)obj) ? BigDecimal.ONE : BigDecimal.ZERO;
        if (obj instanceof String) obj = toNumber(obj.toString());
        if (obj instanceof BigDecimal) return (BigDecimal)obj;
        if (obj instanceof Number) return new BigDecimal(obj.toString());
        // Twig craps out when adding things like maps, arrays, etc. So crap out
        // here until we figure out something better to do.
        if (obj instanceof Iterable || obj instanceof Map
                || obj.getClass().isArray()) {
            throw new IllegalArgumentException();
        }
        return BigDecimal.ONE;
    }
    /**
     * Determines whether the given object is strictly a decimal number. Nulls
     * and objects of types other than string, BigDecimal, double, or float are
     * not considered. Strings are considered decimals only if they begin with
     * two whole numbers separated by a decimal point.
     * @param obj
     * @return 
     */
    public static boolean isDecimal(Object obj) {
        if (obj == null || obj instanceof Undefined || obj instanceof Boolean) {
            return false;
        }
        if (obj instanceof String) {
            if (obj.toString().trim().isEmpty()) {
                return false;
            }
            
            Matcher matcher = PATTERN.matcher(obj.toString());
            matcher.matches();
            return matcher.group(3) != null;
        }
        return obj instanceof BigDecimal || obj instanceof Double
                || obj instanceof Float;
    }
    /**
     * Determines if all the given objects are real numbers.
     * @param list
     * @return 
     */
    public static boolean areDecimal(Object...list) {
    	if (list == null || list.length == 0) 
            return false;
    	
        for (Object obj : list) {
            if (!isDecimal(obj)) {
                return false;
            }
        }

        return true;
    }
    /**
     * Converts the given string to its numeric equivalent, either a long or a
     * double in order to support largest-width operations.
     * @param str
     * @return 
     */
    public static Number toNumber(String str) {
        if (str == null || str.trim().isEmpty()) {
            return 0L;
        }
        
        // Twig supports pulling numbers from the beginning of strings
        Matcher matcher = PATTERN.matcher(str);
        if (!matcher.matches() || (matcher.group(2) == null
                && matcher.group(3) == null)) {
            return 0L;
        }
        if (matcher.group(3) != null) {
            return new BigDecimal(matcher.group(1));
        }
        return Long.valueOf(matcher.group(1));
    }
    /**
     * Determines if the given object is in any way considered numeric by Twig.
     * Nulls, booleans, strings, and numbers are all considered numeric by Twig
     * in some way.
     * @param obj
     * @return 
     */
    public static boolean isNumeric(Object obj) {
        return obj == null || obj instanceof Undefined || obj instanceof Boolean
                || obj instanceof String || obj instanceof Number;
    }
    
    
    /**
     * Determines if the given object is an empty String, e.g. "".
     * 
     * Note this is necessary due to Twigs behaviour for String comparisons 
     * of Null and empty Strings.
     * 
     * @param obj
     * @return 
     */
    private static boolean isEmptyString(Object obj) {
        return obj instanceof String && ((String)obj).isEmpty();
    }
    
    /**
     * Determines if the given objects are equal in PHP's loose equality terms.
     * 
     * Note this is the only operation contained here, as it is the only one
     * used in more than one location.
     * @param a
     * @param b
     * @return 
     */
    public static boolean areLooselyEqual(Object a, Object b) {
        // Using the same behaviour for Jtwig as we retrieve in twig
        if (a == null) {
            return b == null || isEmptyString(b) || (isBoolean(b) && !toBoolean(b));
        }
        if (b == null) {
            return isEmptyString(a) || isBoolean(a) && !toBoolean(a);
        }
        if (isBoolean(a) || isBoolean(b)) {
            return Objects.equals(toBoolean(a), toBoolean(b));
        }
        return a.toString().equals(b.toString());
    }
}