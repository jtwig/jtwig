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

package com.lyncode.jtwig.functions.parameters.convert;

import com.lyncode.jtwig.functions.parameters.convert.exceptions.ConvertException;
import com.lyncode.jtwig.functions.parameters.convert.impl.*;

import java.util.HashMap;
import java.util.Map;

import static com.lyncode.jtwig.functions.parameters.convert.impl.NativeConversions.toNativeInteger;

public class CompiledParameterConverter implements com.lyncode.jtwig.functions.parameters.convert.api.ParameterConverter {
    private Map<Class<?>, Map<Class<?>, com.lyncode.jtwig.functions.parameters.convert.api.ParameterConverter>> converters = new HashMap<>();

    public CompiledParameterConverter() {
        add(String.class, Integer.TYPE, new StringToIntegerConverter());
        add(String.class, Integer.class, new StringToIntegerConverter());
        add(String.class, Long.TYPE, new StringToLongConverter());
        add(String.class, Long.class, new StringToLongConverter());
        add(Object.class, String.class, new ObjectToStringConverter());
        add(String.class, Float.class, new StringToFloatConverter());
        add(String.class, Float.TYPE, new StringToFloatConverter());
        add(String.class, Double.class, new StringToDoubleConverter());
        add(String.class, Double.TYPE, new StringToDoubleConverter());
        add(Integer.class, Integer.TYPE, toNativeInteger());
    }

    public CompiledParameterConverter add(Class<?> from, Class<?> to, com.lyncode.jtwig.functions.parameters.convert.api.ParameterConverter converter) {
        if (!converters.containsKey(from))
            converters.put(from, new HashMap<Class<?>, com.lyncode.jtwig.functions.parameters.convert.api.ParameterConverter>());

        converters.get(from).put(to, converter);
        return this;
    }

    public boolean canConvert (Object from, Class<?> to) {
        if (from == null) return true;
        Class<?> fromClass = from.getClass();

        for (Class<?> type : converters.keySet()) {
            if (type.isAssignableFrom(fromClass)) {
                if (converters.get(type).containsKey(to) &&
                    converters.get(type).get(to).canConvert(from, to))
                    return true;
            }
        }
        return false;
    }

    public Object convert(Object from, Class<?> to) throws ConvertException {
        if (from == null) return null;
        Class<?> fromClass = from.getClass();

        for (Class<?> type : converters.keySet()) {
            if (type.isAssignableFrom(fromClass)) {
                if (converters.get(type).containsKey(to) &&
                        converters.get(type).get(to).canConvert(from, to))
                    return convert(from, to, type);
            }
        }

        throw new ConvertException("Cannot convert type " + fromClass.getName() + " to " + to.getName());
    }

    private Object convert(Object from, Class<?> to, Class<?> fromClass) throws ConvertException {
        Map<Class<?>, com.lyncode.jtwig.functions.parameters.convert.api.ParameterConverter> toMap = converters.get(fromClass);
        if (!toMap.containsKey(to)) throw new ConvertException("Cannot convert type " + fromClass.getName() + " to " + to.getName());
        else return toMap.get(to).convert(from, to);
    }
}
