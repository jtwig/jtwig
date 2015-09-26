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

package org.jtwig.render;

import com.google.common.base.Optional;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.apache.commons.lang3.StringUtils;
import org.jtwig.Environment;
import org.jtwig.JtwigModelMap;
import org.jtwig.cache.TemplateCache;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.Template;
import org.jtwig.exception.RenderException;
import org.jtwig.extension.core.tokenparsers.model.Block;
import org.jtwig.functions.exceptions.FunctionException;
import org.jtwig.functions.exceptions.FunctionNotFoundException;
import org.jtwig.functions.parameters.convert.DemultiplexerConverter;
import org.jtwig.functions.parameters.convert.impl.ObjectToStringConverter;
import org.jtwig.functions.parameters.input.InputParameters;
import org.jtwig.functions.parameters.resolve.api.InputParameterResolverFactory;
import org.jtwig.functions.parameters.resolve.api.ParameterResolver;
import org.jtwig.functions.parameters.resolve.impl.InputDelegateMethodParametersResolver;
import org.jtwig.functions.parameters.resolve.impl.ParameterAnnotationParameterResolver;
import org.jtwig.functions.resolver.api.FunctionResolver;
import org.jtwig.functions.resolver.impl.CompoundFunctionResolver;
import org.jtwig.functions.resolver.impl.DelegateFunctionResolver;
import org.jtwig.functions.resolver.model.Executable;
import org.jtwig.render.stream.RenderStream;
import static org.jtwig.types.Undefined.UNDEFINED;

public class RenderContext {
    private static final String MODEL = "model";

    public static RenderContext create(Environment env, JtwigModelMap modelMap, OutputStream output) {
        return new RenderContext(env, modelMap, new CompoundFunctionResolver()
                .withResolver(new DelegateFunctionResolver(env.getConfiguration().getFunctionRepository(),
                        new InputDelegateMethodParametersResolver(annotationWithoutConversion())))
                .withResolver(new DelegateFunctionResolver(env.getConfiguration().getFunctionRepository(),
                        new InputDelegateMethodParametersResolver(annotationWithConversion()))), new RenderStream(output, env));
    }

    public static RenderContext create(Environment env, JtwigModelMap modelMap, FunctionResolver functionResolver, OutputStream output) {
        return new RenderContext(env, modelMap, functionResolver, new RenderStream(output, env));
    }

    private final FunctionResolver functionResolver;

    private final Environment env;

    private final JtwigModelMap modelMap;

    private final RenderStream renderStream;
    
    private Stack<Template.Compiled> templateStack = new Stack<>();
    
    private Stack<Block.CompiledBlock> blockStack = new Stack<>();

    private RenderContext(
            Environment env,
            JtwigModelMap modelMap,
            FunctionResolver functionResolver,
            RenderStream renderStream) {
        this.env = env;
        this.modelMap = modelMap;
        this.renderStream = renderStream;
        this.functionResolver = functionResolver;
    }
    private RenderContext(
            Environment env,
            JtwigModelMap modelMap,
            FunctionResolver functionResolver,
            RenderStream renderStream,
            Stack<Template.Compiled> templateStack) {
        this(env, modelMap, functionResolver, renderStream);
        this.templateStack = templateStack;
    }

    public void write(byte[] bytes) throws IOException {
        renderStream.write(bytes);
    }

    public RenderStream renderStream() {
        return this.renderStream;
    }

    public RenderContext newRenderContext(OutputStream outputStream) {
        return new RenderContext(env, modelMap, functionResolver,
                new RenderStream(outputStream, env),
                templateStack);
    }

    public Environment environment() {
        return env;
    }
    
    public TemplateCache cache() {
        return env.getConfiguration().getTemplateCache();
    }
    
    public RenderContext clearTemplateStack() {
        templateStack = new Stack<>();
        return this;
    }
    public Stack<Template.Compiled> getTemplateStack() {
        return templateStack;
    }
    public Stack<Block.CompiledBlock> getBlockStack() {
        return blockStack;
    }

    public void renderConcurrent(Renderable content) throws IOException, RenderException {
        renderStream.renderConcurrent(content, fork());
    }

    private RenderContext fork() throws IOException {
        return new RenderContext(env, modelMap, functionResolver,
                renderStream.fork(), templateStack);
    }

    public RenderContext isolatedModel() {
        return new RenderContext(env, modelMap.clone(),
                functionResolver, renderStream, templateStack);
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

        } catch (InvocationTargetException e) {
            throw new FunctionException(e.getTargetException());
        } catch (IllegalAccessException e) {
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
