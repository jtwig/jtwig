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

package com.lyncode.jtwig;

import com.google.common.base.Optional;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.functions.exceptions.FunctionNotFoundException;
import com.lyncode.jtwig.functions.parameters.convert.DemultiplexerConverter;
import com.lyncode.jtwig.functions.parameters.convert.impl.ObjectToStringConverter;
import com.lyncode.jtwig.functions.parameters.input.InputParameters;
import com.lyncode.jtwig.functions.parameters.resolve.api.InputParameterResolverFactory;
import com.lyncode.jtwig.functions.parameters.resolve.api.ParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.impl.InputDelegateMethodParametersResolver;
import com.lyncode.jtwig.functions.parameters.resolve.impl.ParameterAnnotationParameterResolver;
import com.lyncode.jtwig.functions.repository.api.FunctionRepository;
import com.lyncode.jtwig.functions.repository.impl.MapFunctionRepository;
import com.lyncode.jtwig.functions.resolver.api.FunctionResolver;
import com.lyncode.jtwig.functions.resolver.impl.CompoundFunctionResolver;
import com.lyncode.jtwig.functions.resolver.impl.DelegateFunctionResolver;
import com.lyncode.jtwig.functions.resolver.model.Executable;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static com.lyncode.jtwig.types.Undefined.UNDEFINED;

public class JtwigContext {

    private static final String MODEL = "model";

    public static JtwigContext context() {
        return new JtwigContext();
    }

    private final JtwigModelMap modelMap;
    private final FunctionResolver functionResolver;

    public JtwigContext(JtwigModelMap modelMap, FunctionRepository functionRepository) {
        this.modelMap = modelMap;
        functionResolver = new CompoundFunctionResolver()
            .withResolver(new DelegateFunctionResolver(functionRepository,
                    new InputDelegateMethodParametersResolver(annotationWithoutConversion())))
            .withResolver(new DelegateFunctionResolver(functionRepository,
                    new InputDelegateMethodParametersResolver(annotationWithConversion())));
    }

    private InputParameterResolverFactory annotationWithoutConversion() {
        return new InputParameterResolverFactory() {
            @Override
            public ParameterResolver create(InputParameters parameters) {
                return new ParameterAnnotationParameterResolver(parameters, new DemultiplexerConverter());
            }
        };
    }

    private InputParameterResolverFactory annotationWithConversion() {
        return new InputParameterResolverFactory() {
            @Override
            public ParameterResolver create(InputParameters parameters) {
                return new ParameterAnnotationParameterResolver(parameters, new DemultiplexerConverter()
                        .withConverter(String.class, new ObjectToStringConverter())
                );
            }
        };
    }

    public JtwigContext(JtwigModelMap modelMap, FunctionResolver functionResolver) {
        this.modelMap = modelMap;
        this.functionResolver = functionResolver;
    }

    public JtwigContext(JtwigModelMap modelMap) {
        this(modelMap, new MapFunctionRepository());
    }

    public JtwigContext() {
        this(new JtwigModelMap(), new MapFunctionRepository());
    }

    public JtwigContext withModelAttribute(String key, Object value) {
        this.modelMap.add(key, value);
        return this;
    }

    public Object map(String key) {
        if (MODEL.equals(key)) {
            return modelMap;
        } else {
            if (modelMap.containsKey(key)) {
                return modelMap.get(key);
            } else {
                return UNDEFINED;
            }
        }
    }

    public Object executeFunction(String name, InputParameters parameters) throws FunctionException {
        try {
            Optional<Executable> resolve = functionResolver.resolve(name, parameters);
            if (resolve.isPresent()) return resolve.get().execute();
            throw new FunctionNotFoundException("Unable to find function with name '"+ name +"'");
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new FunctionException(e);
        }
    }

    public JtwigContext with(String key, Object value) {
        modelMap.add(key, value);
        return this;
    }

    public JtwigContext with(Map<Object, Object> calculate) {
        for (Map.Entry entry : calculate.entrySet()) {
            modelMap.add(entry.getKey().toString(), entry.getValue());
        }
        return this;
    }

    public JtwigContext clone() {
        return new JtwigContext(modelMap.clone(), functionResolver);
    }
}
