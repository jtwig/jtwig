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
import com.lyncode.jtwig.functions.annotations.Parameter;
import com.lyncode.jtwig.functions.builtin.*;
import com.lyncode.jtwig.functions.exceptions.FunctionNotFoundException;
import com.lyncode.jtwig.functions.parameters.GivenParameters;
import com.lyncode.jtwig.functions.parameters.convert.CompiledParameterConverter;
import com.lyncode.jtwig.functions.parameters.convert.api.ParameterConverter;
import com.lyncode.jtwig.functions.parameters.convert.exceptions.ConvertException;
import com.lyncode.jtwig.functions.parameters.resolve.CompiledParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.api.AnnotatedMethodParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.api.TypeMethodParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.exceptions.ResolveException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionResolver {
    private final Map<String, Map<Class[], Pair<FunctionReference,  Boolean>>> cachedFunctions = new HashMap<>();
    private final Map<String, List<FunctionReference>> functions = new HashMap<>();
    private final CompiledParameterResolver parameterResolver = new CompiledParameterResolver();
    private final CompiledParameterConverter parameterConverter = new CompiledParameterConverter();

    public FunctionResolver() {
        store(new StringFunctions());
        store(new MathFunctions());
        store(new NumberFunctions());
        store(new MapFunctions());
        store(new ListFunctions());
        store(new ObjectFunctions());
        store(new DateFunctions());
        store(new BooleanFunctions());
    }

    public CallableFunction get(String name, GivenParameters givenParameters) throws FunctionNotFoundException, ResolveException {
        if (!functions.containsKey(name))
            throw new FunctionNotFoundException("Function with name '"+name+"' not found.");
        if (!cachedFunctions.containsKey(name))
            cachedFunctions.put(name, new HashMap<Class[], Pair<FunctionReference,  Boolean>>());

        if (cachedFunctions.get(name).containsKey(givenParameters.types())) {
            Pair<FunctionReference, Boolean> pair = cachedFunctions.get(name).get(givenParameters.types());
            Object[] arguments = pair.getRight()
                    ? parameterResolver.resolveParameters(pair.getLeft(), givenParameters, parameterConverter)
                    : parameterResolver.resolveParameters(pair.getLeft(), givenParameters, emptyConverter());
            return new CallableFunction(pair.getLeft(), arguments);
        }
        List<FunctionReference> functionList = functions.get(name);
        for (FunctionReference function : functionList) {
            Object[] arguments = parameterResolver.resolveParameters(function, givenParameters, emptyConverter());
            if (arguments != null) {
                cachedFunctions.get(name).put(givenParameters.types(), new ImmutablePair<>(function, false));
                return new CallableFunction(function, arguments);
            }
        }
        for (FunctionReference function : functionList) {
            Object[] arguments = parameterResolver.resolveParameters(function, givenParameters, parameterConverter);
            if (arguments != null) {
                cachedFunctions.get(name).put(givenParameters.types(), new ImmutablePair<>(function, true));
                return new CallableFunction(function, arguments);
            }
        }


        throw new FunctionNotFoundException("Function with name '"+name+"' and given parameters not found. Available:\n"+listAvailable(name, functions.get(name)));
    }

    private String listAvailable(String name, List<FunctionReference> functionReferences) {
        List<String> list = new ArrayList<>();
        for (FunctionReference functionReference : functionReferences) {
            List<String> arguments = new ArrayList<>();
            List<Class<?>> parametersWithAnnotation = functionReference.getParameterTypesWithAnnotation(Parameter.class);
            for (Class<?> aClass : parametersWithAnnotation) {
                arguments.add(aClass.getName());
            }
            list.add("- "+name+"("+StringUtils.join(arguments, ", ")+")");
        }
        return StringUtils.join(list, "\n");
    }


    public FunctionResolver add(Class<?> from, Class<?> to, ParameterConverter converter) {
        parameterConverter.add(from, to, converter);
        return this;
    }

    public FunctionResolver add(AnnotatedMethodParameterResolver resolver) {
        parameterResolver.add(resolver);
        return this;
    }

    public FunctionResolver add(TypeMethodParameterResolver resolver) {
        parameterResolver.add(resolver);
        return this;
    }

    public FunctionResolver store(Object instance) {
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
        List<FunctionReference> list = functions.get(name);
        FunctionReference newFunction = new FunctionReference(method, instance);
        boolean added = false;
        for (int i = 0;i<list.size() && !added; i++) {
            if (list.get(i).compareTo(newFunction) > 0) {
                list.add(i, newFunction);
                added = true;
            }
        }
        if (!added)
            list.add(newFunction);
    }

    private static ParameterConverter emptyConverter() {
        return new ParameterConverter() {
            @Override
            public boolean canConvert(Object from, Class<?> to) {
                return false;
            }

            @Override
            public Object convert(Object from, Class<?> to) throws ConvertException {
                return null;
            }
        };
    }
}
