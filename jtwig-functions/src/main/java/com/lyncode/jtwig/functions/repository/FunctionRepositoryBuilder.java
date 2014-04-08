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

package com.lyncode.jtwig.functions.repository;

import com.lyncode.jtwig.functions.annotations.JtwigFunction;
import com.lyncode.jtwig.functions.builtin.*;
import com.lyncode.jtwig.functions.parameters.convert.api.ParameterConverter;
import com.lyncode.jtwig.functions.parameters.resolve.BaseParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.api.AnnotatedMethodParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.api.TypeMethodParameterResolver;

import java.lang.reflect.Method;
import java.util.*;

public class FunctionRepositoryBuilder {
    public FunctionRepositoryBuilder functionRepository () {
        return new FunctionRepositoryBuilder();
    }

    private Map<String, List<FunctionReference>> functions = new HashMap<>();
    private BaseParameterResolver parameterResolver = new BaseParameterResolver();
    private com.lyncode.jtwig.functions.parameters.convert.ParameterConverter parameterConverter = new com.lyncode.jtwig.functions.parameters.convert.ParameterConverter();

    public FunctionRepositoryBuilder() {
        store(new StringFunctions());
        store(new MathFunctions());
        store(new NumberFunctions());
        store(new MapFunctions());
        store(new ListFunctions());
        store(new ObjectFunctions());
        store(new DateFunctions());
        store(new BooleanFunctions());
    }

    public FunctionRepositoryBuilder add (Class<?> from, Class<?> to, ParameterConverter converter) {
        parameterConverter.add(from, to, converter);
        return this;
    }

    public FunctionRepositoryBuilder add (AnnotatedMethodParameterResolver resolver) {
        parameterResolver.add(resolver);
        return this;
    }

    public FunctionRepositoryBuilder add (TypeMethodParameterResolver resolver) {
        parameterResolver.add(resolver);
        return this;
    }

    public FunctionRepositoryBuilder store (Object instance) {
        for (Method method : instance.getClass().getDeclaredMethods()) {
            JtwigFunction annotation = method.getAnnotation(JtwigFunction.class);
            if (annotation != null) {
                addFunction(instance, method, annotation.name());
                for (String name : annotation.aliases()) {
                    addFunction(instance, method, name);
                }
            }

        }
        return this;
    }

    private void addFunction(Object instance, Method method, String name) {
        if (!functions.containsKey(name))
            functions.put(name, new ArrayList<FunctionReference>());
        functions.get(name).add(new FunctionReference(method, instance));
    }

    public FunctionResolver build () {
        for (String key : functions.keySet())
            Collections.sort(functions.get(key));
        return new FunctionResolver(functions, parameterResolver, parameterConverter);
    }
}
