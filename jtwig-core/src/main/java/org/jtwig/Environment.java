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

import org.jtwig.compile.CompileContext;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.jtwig.content.api.Compilable;
import org.jtwig.content.model.Template;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseBypassException;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.ResourceException;
import org.jtwig.loader.Loader;
import org.jtwig.loader.impl.EmptyLoader;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigConstantParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.parboiled.Parboiled;
import org.parboiled.common.FileUtils;
import org.parboiled.errors.ParserRuntimeException;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;

import java.nio.charset.Charset;

import static org.parboiled.Parboiled.createParser;

public class Environment {
    protected JtwigConfiguration configuration;
    
    //~ Construction ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Environment() {
        this(JtwigConfigurationBuilder.defaultConfiguration());
    }

    public Environment(JtwigConfiguration configuration) {
        this.configuration = configuration;
    }

    public JtwigConfiguration getConfiguration() {
        return configuration;
    }
    
    public JtwigBasicParser getBasicParser() {
        return createParser(JtwigBasicParser.class, this);
    }
    
    public JtwigTagPropertyParser getTagPropertyParser() {
        return createParser(JtwigTagPropertyParser.class, this);
    }
    
    public JtwigConstantParser getConstantParser() {
        return createParser(JtwigConstantParser.class, this);
    }
    
    //~ Operational methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Loader.Resource load(String name) throws ResourceException {
        if (configuration.getLoader().exists(name)) {
            return configuration.getLoader().get(name);
        }
        return new EmptyLoader.NoResource(name);
    }

    public Template parse(String name) throws ResourceException, ParseException {
        return parse(load(name));
    }
    public Template parse(Loader.Resource resource) throws ResourceException, ParseException {
        if (resource == null) {
            throw new IllegalArgumentException("No resource given");
        }
        Template cached = configuration.getTemplateCache().getParsed(resource.getCacheKey());
        if (cached != null) {
            return cached;
        }
        
        JtwigContentParser parser = Parboiled.createParser(JtwigContentParser.class, resource, this);
        
        try {
            ReportingParseRunner<Compilable> runner = new ReportingParseRunner<>(parser.start());
            ParsingResult<Compilable> result = runner.run(
                    FileUtils.readAllText(resource.source(), Charset.defaultCharset()));
            cached = (Template)result.resultValue;
            configuration.getTemplateCache().addParsed(resource.getCacheKey(), cached);
            return cached;
        } catch (ParserRuntimeException e) {
            if (e.getCause() instanceof ParseBypassException) {
                ParseException innerException = ((ParseBypassException) e.getCause()).getCause();
                innerException.setExpression(e.getMessage());
                throw innerException;
            } else {
                throw new ParseException(e);
            }
        } catch (ResourceException e) {
            throw new ParseException(e);
        }
    }
    
    public Template.Compiled compile(final String name)
            throws ResourceException, ParseException, CompileException {
        Loader.Resource resource = load(name);
        Template template = parse(resource);
        return compile(template, resource);
    }
    public Template.Compiled compile(final String name,
            final CompileContext context)
            throws ResourceException, ParseException, CompileException {
        Loader.Resource resource = load(name);
        Template template = parse(resource);
        return compile(template, resource, context);
    }
    public Template.Compiled compile(final Loader.Resource resource)
            throws ResourceException, ParseException, CompileException {
        Template template = parse(resource);
        return compile(template, resource);
    }
    public Template.Compiled compile(final Loader.Resource resource,
            final CompileContext context)
            throws ResourceException, ParseException, CompileException {
        Template template = parse(resource);
        return compile(template, resource);
    }
    public Template.Compiled compile(final Template template,
            final Loader.Resource resource) throws CompileException {
        CompileContext compileContext = new CompileContext(resource, this);
        return compile(template, resource, compileContext);
    }
    public Template.Compiled compile(final Template template,
            final Loader.Resource resource,
            final CompileContext context) throws CompileException {
        Template.Compiled cached = configuration.getTemplateCache().getCompiled(resource.getCacheKey());
        if (cached != null) {
            return cached;
        }
        cached = template.compile(context);
        configuration.getTemplateCache().addCompiled(resource.getCacheKey(), cached);
        return cached;
    }
}