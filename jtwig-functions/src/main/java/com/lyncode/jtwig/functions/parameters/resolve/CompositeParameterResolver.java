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

package com.lyncode.jtwig.functions.parameters.resolve;

import com.lyncode.jtwig.functions.annotations.Parameter;
import com.lyncode.jtwig.functions.parameters.GivenParameters;
import com.lyncode.jtwig.functions.parameters.convert.api.ParameterConverter;
import com.lyncode.jtwig.functions.parameters.resolve.api.AnnotatedMethodParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.api.TypeMethodParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.exceptions.ResolveException;
import com.lyncode.jtwig.functions.parameters.resolve.impl.ParameterAnnotationResolver;
import com.lyncode.jtwig.functions.parameters.resolve.model.MethodInformation;
import com.lyncode.jtwig.functions.parameters.resolve.model.MethodParameter;
import com.lyncode.jtwig.functions.repository.FunctionReference;
import com.lyncode.jtwig.types.Optional;

import java.util.ArrayList;
import java.util.List;

public class CompositeParameterResolver {
    private List<AnnotatedMethodParameterResolver> annotatedMethodParameterResolvers = new ArrayList<>();
    private List<TypeMethodParameterResolver> typeMethodParameterResolvers = new ArrayList<>();

    public CompositeParameterResolver() {
        add(new ParameterAnnotationResolver());
    }

    public CompositeParameterResolver add(AnnotatedMethodParameterResolver resolver) {
        annotatedMethodParameterResolvers.add(resolver);
        return this;
    }

    public CompositeParameterResolver add(TypeMethodParameterResolver resolver) {
        typeMethodParameterResolvers.add(resolver);
        return this;
    }

    private Optional<Object> resolveParameter(MethodParameter javaParameter, GivenParameters givenParameters, ParameterConverter converter) throws ResolveException {
        for (AnnotatedMethodParameterResolver resolver : annotatedMethodParameterResolvers) {
            if (javaParameter.hasAnnotation(resolver.annotationType()) && resolver.canResolveParameter(javaParameter, givenParameters, converter)) {
                return new Optional<>(resolver.resolveParameter(javaParameter, givenParameters, converter));
            }
        }
        for (TypeMethodParameterResolver resolver : typeMethodParameterResolvers) {
            if (javaParameter.hasType(resolver.resolveType()) && resolver.canResolveParameter(javaParameter, givenParameters, converter)) {
                return new Optional<>(resolver.resolveParameter(javaParameter, givenParameters, converter));
            }
        }

        return new Optional<>();
    }

    private boolean canResolve (FunctionReference function, GivenParameters givenParameters) {
        MethodInformation methodInformation = new MethodInformation(function.getMethod());
        if (methodInformation.hasVarArgParameter(Parameter.class))
           return methodInformation.countParametersWithAnnotation(Parameter.class) - 1 <= givenParameters.size() - 1;
        return methodInformation.countParametersWithAnnotation(Parameter.class) == givenParameters.size();
    }

    public Object[] resolveParameters(FunctionReference function, GivenParameters givenParameters, ParameterConverter converter) throws ResolveException {
        if (!canResolve(function, givenParameters))
            return null;
        Class<?>[] parameterTypes = function.getMethod().getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            MethodParameter methodParameter = new MethodParameter(function.getInstance(), function.getMethod(), i);
            Optional<Object> resolvedParameter = resolveParameter(methodParameter, givenParameters, converter);
            if (!resolvedParameter.hasValue()) return null;
            parameters[i] = resolvedParameter.get();
        }

        if (parameterTypes.length != parameters.length)
            return null;

        return parameters;
    }
}
