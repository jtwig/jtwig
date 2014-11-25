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

package com.lyncode.jtwig.render;

import com.google.common.base.Optional;
import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.content.model.compilable.Import;
import com.lyncode.jtwig.content.model.compilable.Macro;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.functions.exceptions.FunctionNotFoundException;
import com.lyncode.jtwig.functions.parameters.convert.DemultiplexerConverter;
import com.lyncode.jtwig.functions.parameters.convert.impl.ObjectToStringConverter;
import com.lyncode.jtwig.functions.parameters.input.InputParameters;
import com.lyncode.jtwig.functions.parameters.resolve.api.InputParameterResolverFactory;
import com.lyncode.jtwig.functions.parameters.resolve.api.ParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.impl.InputDelegateMethodParametersResolver;
import com.lyncode.jtwig.functions.parameters.resolve.impl.ParameterAnnotationParameterResolver;
import com.lyncode.jtwig.functions.resolver.api.FunctionResolver;
import com.lyncode.jtwig.functions.resolver.impl.CompoundFunctionResolver;
import com.lyncode.jtwig.functions.resolver.impl.DelegateFunctionResolver;
import com.lyncode.jtwig.functions.resolver.model.Executable;
import com.lyncode.jtwig.render.config.RenderConfiguration;
import com.lyncode.jtwig.render.stream.RenderStream;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.lyncode.jtwig.types.Undefined.UNDEFINED;

public class RenderContext {
    private static final String MODEL = "model";

    /**
     * NOTE: This method should only be used once (in JtwigTemplate)
     */
    public static RenderContext create(RenderConfiguration configuration, JtwigModelMap modelMap, OutputStream output) {
        return new RenderContext(configuration, modelMap, new CompoundFunctionResolver()
                .withResolver(new DelegateFunctionResolver(configuration.functionRepository(),
                        new InputDelegateMethodParametersResolver(annotationWithoutConversion())))
                .withResolver(new DelegateFunctionResolver(configuration.functionRepository(),
                        new InputDelegateMethodParametersResolver(annotationWithConversion()))), new RenderStream(output, configuration.renderThreadingConfig()));
    }

    public static RenderContext create(RenderConfiguration configuration, JtwigModelMap modelMap, FunctionResolver functionResolver, OutputStream output) {
        return new RenderContext(configuration, modelMap, functionResolver, new RenderStream(output, configuration.renderThreadingConfig()));
    }

    private final FunctionResolver functionResolver;

    private final RenderConfiguration configuration;

    private final JtwigModelMap modelMap;

    private final RenderStream renderStream;

    private RenderContext(RenderConfiguration configuration, JtwigModelMap modelMap, FunctionResolver functionResolver, RenderStream renderStream) {
        this.configuration = configuration;
        this.modelMap = modelMap;
        this.renderStream = renderStream;
        this.functionResolver = functionResolver;
    }

    public void write(byte[] bytes) throws IOException {
        renderStream.write(bytes);
    }

    public RenderStream renderStream() {
        return this.renderStream;
    }

    public RenderContext newRenderContext(OutputStream outputStream) {
        return new RenderContext(configuration, modelMap, functionResolver, new RenderStream(outputStream, configuration.renderThreadingConfig()));
    }

    public RenderConfiguration configuration() {
        return configuration;
    }

    public void renderConcurrent(Renderable content) throws IOException, RenderException {
        renderStream.renderConcurrent(content, fork());
    }

    private RenderContext fork() throws IOException {
        return new RenderContext(configuration, modelMap, functionResolver, renderStream.fork());
    }

    public RenderContext isolatedModel() {
        return new RenderContext(configuration, modelMap.clone(), functionResolver, renderStream);
    }

    public RenderContext with(Map calculate) {
        Set<Map.Entry> set = calculate.entrySet();
        for (Map.Entry entry : set) {
            modelMap.add(entry.getKey().toString(), entry.getValue());
        }
        return this;
    }

    public RenderContext with(String key, Object value) {
        modelMap.add(key, value);
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
            String message = "Unable to find function with name '" + name + "'";

            if (parameters.length() > 0) {
                message += ", and parameters: ";
                List<String> params = new ArrayList<>();
                for (int i = 0; i < parameters.length(); i++) {
                    params.add(parameters.valueAt(i).getClass().getName());
                }
                message += StringUtils.join(params, ", ");
            }

            throw new FunctionNotFoundException(message);

        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new FunctionException(e);
        }
    }


    private static InputParameterResolverFactory annotationWithoutConversion() {
        return new InputParameterResolverFactory() {
            @Override
            public ParameterResolver create(InputParameters parameters) {
                return new ParameterAnnotationParameterResolver(parameters, new DemultiplexerConverter());
            }
        };
    }

    private static InputParameterResolverFactory annotationWithConversion() {
        return new InputParameterResolverFactory() {
            @Override
            public ParameterResolver create(InputParameters parameters) {
                return new ParameterAnnotationParameterResolver(parameters, new DemultiplexerConverter()
                        .withConverter(String.class, new ObjectToStringConverter())
                );
            }
        };
    }
}
