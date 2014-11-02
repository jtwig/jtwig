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

package com.lyncode.jtwig.functions.util;

public class ObjectUtils {
    /**
     * This compare function is designed to operate the same way as its PHP
     * counterpart. Some pretty funky stuff happens during comparisons in PHP,
     * so don't be surprised if you see some funky stuff going on here.
     * @param obj1 The primary object to compare against
     * @param obj2 The object to compare to
     * @return 1 if the first object is greater than the second, -1 if less, 0
     *         if equal
     */
    public static int compare(Object obj1, Object obj2) {
        // Basics
        if(obj1 == null && obj2 == null) {
            return 0;
        }
        if(obj1 != null && obj2 == null) {
            return 1;
        }
        if(obj1 == null) {
            return -1;
        }
        
        // Numeric comparisons
        if(obj1 instanceof Number && !(obj2 instanceof Number)) {
            return 1;
        }
        if(!(obj1 instanceof Number) && obj2 instanceof Number) {
            return -1;
        }
        if(obj1 instanceof Number) {
            return compareNumber((Number)obj1, (Number)obj2);
        }
        
        // Compare everything else as a string
        int res = obj1.toString().compareTo(obj2.toString());
        return res > 0 ? 1 : (res < 0 ? -1 : 0);
    }
    
    protected static int compareNumber(Number num1, Number num2) {
        if(num1 instanceof Double || num2 instanceof Double) {
            return ((Double)num1.doubleValue()).compareTo(num2.doubleValue());
        }
        if(num1 instanceof Float || num2 instanceof Float) {
            return ((Float)num1.floatValue()).compareTo(num2.floatValue());
        }
        if(num1 instanceof Long || num2 instanceof Long) {
            return ((Long)num1.longValue()).compareTo(num2.longValue());
        }
        if(num1 instanceof Integer || num2 instanceof Integer) {
            return ((Integer)num1.intValue()).compareTo(num2.intValue());
        }
        if(num1 instanceof Short || num2 instanceof Short) {
            return ((Short)num1.shortValue()).compareTo(num2.shortValue());
        }
        if(num1 instanceof Byte || num2 instanceof Byte) {
            return ((Byte)num1.byteValue()).compareTo(num2.byteValue());
        }
        throw new IllegalArgumentException("Cannot compare "+num1.getClass().getName()+" with "+num2.getClass().getName());
    }
}