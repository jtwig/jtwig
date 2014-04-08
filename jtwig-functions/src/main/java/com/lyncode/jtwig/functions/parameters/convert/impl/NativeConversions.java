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

package com.lyncode.jtwig.functions.parameters.convert.impl;

import com.lyncode.jtwig.functions.parameters.convert.api.ParameterConverter;
import com.lyncode.jtwig.functions.parameters.convert.exceptions.ConvertException;

public class NativeConversions {
    public static ParameterConverter toNativeInteger() {
        return new ParameterConverter() {
            @Override
            public boolean canConvert(Object from, Class<?> to) {
                return (from instanceof Number);
            }

            @Override
            public Object convert(Object from, Class<?> to) throws ConvertException {
                return ((Number) from).intValue();
            }
        };
    }
}
