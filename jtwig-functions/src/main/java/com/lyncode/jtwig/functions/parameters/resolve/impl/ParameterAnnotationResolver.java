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

package com.lyncode.jtwig.functions.parameters.resolve.impl;

import com.lyncode.jtwig.functions.annotations.Parameter;
import com.lyncode.jtwig.functions.parameters.GivenParameters;
import com.lyncode.jtwig.functions.parameters.convert.api.ParameterConverter;
import com.lyncode.jtwig.functions.parameters.convert.exceptions.ConvertException;
import com.lyncode.jtwig.functions.parameters.resolve.api.AnnotatedMethodParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.exceptions.ResolveException;
import com.lyncode.jtwig.functions.parameters.resolve.model.MethodParameter;
import com.lyncode.jtwig.types.Optional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ParameterAnnotationResolver implements AnnotatedMethodParameterResolver {
    @Override
    public Class<?> annotationType() {
        return Parameter.class;
    }

    @Override
    public boolean canResolveParameter(MethodParameter javaParameter, GivenParameters templateGivenParameters, ParameterConverter parameterConverter) {
        Parameter parameter = javaParameter.annotation(Parameter.class);
        int position = javaParameter.positionOf(Parameter.class);
        if (javaParameter.isVarArg()) {
            List<Object> parameters = new ArrayList<>();
            boolean stop = false;
            for (int i = 0; !stop; i++) {
                Optional<Object> result = templateGivenParameters.get(position + i);
                if (result.hasValue()) parameters.add(result.get());
                else stop = true;
            }

            if (parameters.isEmpty()) return true;

            for (int i = 0; i < parameters.size(); i++)
                if (!canResolveParameterByPosition(javaParameter.type().getComponentType(), templateGivenParameters, position + i, parameterConverter))
                    return false;

            return true;
        }
        // Use position strategy
        Optional<Object> resolvedParameter = templateGivenParameters.get(position);
        if (!resolvedParameter.hasValue()) return false;
        else {
            if (resolvedParameter.get() == null && javaParameter.isNullable()) return true;
            if (javaParameter.type().isAssignableFrom(resolvedParameter.get().getClass()))
                return true;
            if (parameterConverter.canConvert(resolvedParameter.get(), javaParameter.type()))
                return true;
            return false;
        }
    }

    @Override
    public Object resolveParameter(MethodParameter javaParameter, GivenParameters templateGivenParameters, ParameterConverter parameterConverter) throws ResolveException {
        Parameter parameter = javaParameter.annotation(Parameter.class);
        int position = javaParameter.positionOf(Parameter.class);


        if (javaParameter.isVarArg()) {
            List<Object> parameters = new ArrayList<>();
            boolean stop = false;
            for (int i = 0; !stop; i++) {
                Optional<Object> result = templateGivenParameters.get(position + i);
                if (result.hasValue()) parameters.add(result.get());
                else stop = true;
            }

            if (parameters.isEmpty()) return null;

            Class<?> type = javaParameter.type();
            Object[] array = (Object[]) Array.newInstance(type.getComponentType(), parameters.size());
            for (int i = 0; i < array.length; i++) {
                array[i] = getParameterByPosition(type.getComponentType(), templateGivenParameters, position + i, parameterConverter);
            }

            return array;
        }
        return getParameterByPosition(javaParameter.type(), templateGivenParameters, position, parameterConverter);
    }

    private Object getParameterByPosition(Class<?> type, GivenParameters templateGivenParameters, int position, ParameterConverter parameterConverter) throws ResolveException {
        Object resolvedParameter = templateGivenParameters.get(position).get();
        try {
            if (resolvedParameter == null) return null;
            if (type.isAssignableFrom(resolvedParameter.getClass()))
                return resolvedParameter;
            return parameterConverter.convert(resolvedParameter, type);
        } catch (ConvertException e) {
            throw new ResolveException("Unable to resolve parameter with index " + (position + 1), e);
        }
    }


    private boolean canResolveParameterByPosition(Class<?> type, GivenParameters templateGivenParameters, int position, ParameterConverter parameterConverter) {
        Optional<Object> resolvedParameter = templateGivenParameters.get(position);
        if (!resolvedParameter.hasValue()) return true;
        else {
            Object parameterObject = resolvedParameter.get();
            if (type.isAssignableFrom(parameterObject.getClass()))
                return true;
            return parameterConverter.canConvert(parameterObject, type);
        }
    }

}
