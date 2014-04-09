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

import com.lyncode.jtwig.functions.exceptions.FunctionNotFoundException;
import com.lyncode.jtwig.functions.parameters.GivenParameters;
import com.lyncode.jtwig.functions.parameters.convert.api.ParameterConverter;
import com.lyncode.jtwig.functions.parameters.convert.exceptions.ConvertException;
import com.lyncode.jtwig.functions.parameters.resolve.BaseParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.exceptions.ResolveException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionResolver {

    private Map<String, Map<Class[], Pair<FunctionReference,  Boolean>>> cachedFunctions = new HashMap<>();
    private Map<String, List<FunctionReference>> functions;
    private BaseParameterResolver parameterResolver;
    private com.lyncode.jtwig.functions.parameters.convert.ParameterConverter parameterConverter;

    public FunctionResolver(Map<String, List<FunctionReference>> functions, BaseParameterResolver parameterResolver, com.lyncode.jtwig.functions.parameters.convert.ParameterConverter parameterConverter) {
        this.functions = functions;
        this.parameterResolver = parameterResolver;
        this.parameterConverter = parameterConverter;
    }

    public CallableFunction get(String name, GivenParameters givenParameters) throws FunctionNotFoundException, ResolveException {
        if (!functions.containsKey(name))
            throw new FunctionNotFoundException("Function with name '"+name+"' not found");
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

        throw new FunctionNotFoundException("Function with name '"+name+"' not found");
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
