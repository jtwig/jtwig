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

import org.apache.commons.lang3.ArrayUtils;

public class ArrayUtil {
    public static Byte[] toArray(byte[] arr) {
        return ArrayUtils.toObject(arr);
    }
    public static Short[] toArray(short[] arr) {
        return ArrayUtils.toObject(arr);
    }
    public static Integer[] toArray(int[] arr) {
        return ArrayUtils.toObject(arr);
    }
    public static Long[] toArray(long[] arr) {
        return ArrayUtils.toObject(arr);
    }
    public static Boolean[] toArray(boolean[] arr) {
        return ArrayUtils.toObject(arr);
    }
    public static Character[] toArray(char[] arr) {
        return ArrayUtils.toObject(arr);
    }
    public static Float[] toArray(float[] arr) {
        return ArrayUtils.toObject(arr);
    }
    public static Double[] toArray(double[] arr) {
        return ArrayUtils.toObject(arr);
    }
    public static Object[] toArray(Object[] arr) {
        return arr;
    }
    public static Object[] toArray(Object obj) {
        if (!obj.getClass().isArray()) {
            throw new IllegalArgumentException("Argument is not an array");
        }
        
        Class<?> type = obj.getClass().getComponentType();
        if (!type.isPrimitive()) {
            return (Object[])obj;
        }
        
        
        if (Byte.TYPE.isAssignableFrom(type)) {
            return toArray((byte[])obj);
        }
        if (Short.TYPE.isAssignableFrom(type)) {
            return toArray((short[])obj);
        }
        if (Integer.TYPE.isAssignableFrom(obj.getClass().getComponentType())) {
            return toArray((int[])obj);
        }
        if (Long.TYPE.isAssignableFrom(obj.getClass().getComponentType())) {
            return toArray((long[])obj);
        }
        if (Boolean.TYPE.isAssignableFrom(obj.getClass().getComponentType())) {
            return toArray((boolean[])obj);
        }
        if (Character.TYPE.isAssignableFrom(obj.getClass().getComponentType())) {
            return toArray((char[])obj);
        }
        if (Float.TYPE.isAssignableFrom(obj.getClass().getComponentType())) {
            return toArray((float[])obj);
        }
        if (Double.TYPE.isAssignableFrom(obj.getClass().getComponentType())) {
            return toArray((double[])obj);
        }
        
        throw new IllegalArgumentException("Unsupported argument type: "+type.getName());
    }
}