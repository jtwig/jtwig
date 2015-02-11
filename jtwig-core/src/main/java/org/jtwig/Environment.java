/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig;

import com.google.common.base.Function;
import java.nio.charset.Charset;
import org.jtwig.cache.TemplateCache;
import org.jtwig.cache.impl.ExecutionCache;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Compilable;
import org.jtwig.content.model.Template;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseBypassException;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.ResourceException;
import org.jtwig.functions.config.JsonConfiguration;
import org.jtwig.functions.repository.api.FunctionRepository;
import org.jtwig.functions.repository.impl.MapFunctionRepository;
import org.jtwig.loader.Loader;
import org.jtwig.parser.config.AddonParserList;
import org.jtwig.parser.config.Symbols;
import org.jtwig.parser.config.TagSymbols;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigConstantParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.parboiled.Parboiled;
import static org.parboiled.Parboiled.createParser;
import org.parboiled.common.FileUtils;
import org.parboiled.errors.ParserRuntimeException;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;

public class Environment {
    protected Charset charset = Charset.forName("UTF-8");
    protected boolean strictMode = false;
    protected boolean logNonStrictMode = true;
    protected Symbols symbols = TagSymbols.DEFAULT;
    protected AddonParserList addonParserList = new AddonParserList();
    
    protected int minThreads = 20;
    protected int maxThreads = 100;
    protected long keepAliveTime = 60L;
    
    protected JsonConfiguration jsonConfiguration = new JsonConfiguration();
    protected FunctionRepository functionRepository
            = new MapFunctionRepository(jsonConfiguration);
    protected TemplateCache cache = new ExecutionCache();
    protected Loader loader;
    
    protected JtwigBasicParser basicParser;
    protected JtwigTagPropertyParser tagPropertyParser;
    protected JtwigConstantParser constantParser;
    
    //~ Construction ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Environment() {
        basicParser = createParser(JtwigBasicParser.class, this);
        tagPropertyParser = createParser(JtwigTagPropertyParser.class, this);
        constantParser = createParser(JtwigConstantParser.class, this);
    }
    
    //~ Getters & Setters ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Charset getCharset() {
        return charset;
    }
    public Environment setCharset(final Charset charset) {
        this.charset = charset;
        return this;
    }
    
    public boolean isStrictMode() {
        return strictMode;
    }
    public Environment setStrictMode(final boolean strictMode) {
        this.strictMode = strictMode;
        return this;
    }
    
    public boolean isLogNonStrictMode() {
        return logNonStrictMode;
    }
    public Environment setLogNonStrictMode(final boolean logNonStrictMode) {
        this.logNonStrictMode = logNonStrictMode;
        return this;
    }
    
    public Symbols getSymbols() {
        return symbols;
    }
    public Environment setSymbols(final Symbols symbols) {
        this.symbols = symbols;
        return this;
    }
    
    public AddonParserList getAddonParserList() {
        return addonParserList;
    }
    
    public int getMinThreads() {
        return minThreads;
    }
    public Environment setMinThreads(final int minThreads) {
        this.minThreads = minThreads;
        return this;
    }

    public int getMaxThreads() {
        return maxThreads;
    }
    public Environment setMaxThreads(final int maxThreads) {
        this.maxThreads = maxThreads;
        return this;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }
    public Environment setKeepAliveTime(final long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
        return this;
    }

    public JsonConfiguration getJsonConfiguration () {
        return jsonConfiguration;
    }
    public Environment setJsonConfiguration(
            final JsonConfiguration jsonConfiguration) {
        this.jsonConfiguration = jsonConfiguration;
        return this;
    }
    public Environment setJsonMapper(final Function<Object, String> mapper) {
        getJsonConfiguration().jsonMapper(mapper);
        return this;
    }

    public FunctionRepository getFunctionRepository() {
        return functionRepository;
    }
    public Environment setFunctionRepository(
            final FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
        return this;
    }
    
    public TemplateCache getCache() {
        return cache;
    }
    public Environment setCache(final TemplateCache cache) {
        this.cache = cache;
        return this;
    }
    
    public Loader getLoader() {
        return loader;
    }
    public Environment setLoader(final Loader loader) {
        this.loader = loader;
        return this;
    }
    
    public JtwigBasicParser getBasicParser() {
        return basicParser;
    }
    public Environment setBasicParser(final JtwigBasicParser basicParser) {
        this.basicParser = basicParser;
        return this;
    }
    
    public JtwigTagPropertyParser getTagPropertyParser() {
        return tagPropertyParser;
    }
    public Environment setTagPropertyParser(final JtwigTagPropertyParser tagPropertyParser) {
        this.tagPropertyParser = tagPropertyParser;
        return this;
    }
    
    public JtwigConstantParser getConstantParser() {
        return constantParser;
    }
    public Environment setConstantParser(final JtwigConstantParser constantParser) {
        this.constantParser = constantParser;
        return this;
    }
    
    //~ Operational methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Loader.Resource load(final String name) throws ResourceException {
        if (loader.exists(name)) {
            return loader.get(name);
        }
        return null;
    }
    public Template parse(final String name) throws ResourceException, ParseException {
        return parse(load(name));
    }
    public Template parse(final Loader.Resource resource) throws ResourceException, ParseException {
        if (resource == null) {
            throw new IllegalArgumentException("No resource given");
        }
        Template cached = getCache().getParsed(resource.getCacheKey());
        if (cached != null) {
            return cached;
        }
        
        JtwigContentParser parser = Parboiled.createParser(JtwigContentParser.class, resource, this);
        
        try {
            ReportingParseRunner<Compilable> runner = new ReportingParseRunner<>(parser.start());
            ParsingResult<Compilable> result = runner.run(
                    FileUtils.readAllText(resource.source(), Charset.defaultCharset()));
            cached = (Template)result.resultValue;
            getCache().addParsed(resource.getCacheKey(), cached);
            return cached;
        } catch (ParserRuntimeException e) {
            if (e.getCause() instanceof ParseBypassException) {
                ParseException innerException = ((ParseBypassException) e.getCause()).getInnerException();
                innerException.setExpression(e.getMessage());
                throw innerException;
            } else {
                throw new ParseException(e);
            }
        } catch (ResourceException e) {
            throw new ParseException(e);
        }
    }
    
    public Template.CompiledTemplate compile(final String name)
            throws ResourceException, ParseException, CompileException {
        Loader.Resource resource = load(name);
        Template template = parse(resource);
        return compile(template, resource);
    }
    public Template.CompiledTemplate compile(final String name,
            final CompileContext context)
            throws ResourceException, ParseException, CompileException {
        Loader.Resource resource = load(name);
        Template template = parse(resource);
        return compile(template, resource, context);
    }
    public Template.CompiledTemplate compile(final Loader.Resource resource)
            throws ResourceException, ParseException, CompileException {
        Template template = parse(resource);
        return compile(template, resource);
    }
    public Template.CompiledTemplate compile(final Loader.Resource resource,
            final CompileContext context)
            throws ResourceException, ParseException, CompileException {
        Template template = parse(resource);
        return compile(template, resource);
    }
    public Template.CompiledTemplate compile(final Template template,
            final Loader.Resource resource)
            throws ResourceException, ParseException, CompileException {
        CompileContext compileContext = new CompileContext(resource, this);
        return compile(template, resource, compileContext);
    }
    public Template.CompiledTemplate compile(final Template template,
            final Loader.Resource resource,
            final CompileContext context) throws CompileException {
        Template.CompiledTemplate cached = getCache().getCompiled(resource.getCacheKey());
        if (cached != null) {
            return cached;
        }
        cached = template.compile(context);
        getCache().addCompiled(resource.getCacheKey(), cached);
        return cached;
    }
}