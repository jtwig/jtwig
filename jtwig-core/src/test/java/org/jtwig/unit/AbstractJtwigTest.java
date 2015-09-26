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

package org.jtwig.unit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.jtwig.Environment;
import org.jtwig.JtwigModelMap;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Compilable;
import org.jtwig.exception.ResourceException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.loader.Loader;
import org.jtwig.loader.impl.ClasspathLoader;
import org.jtwig.loader.impl.StringLoader;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.render.RenderContext;
import org.jtwig.util.LoaderUtil;
import org.junit.Before;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import org.mockito.internal.stubbing.answers.ReturnsArgumentAt;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.parserunners.TracingParseRunner;
import org.parboiled.support.ParsingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractJtwigTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJtwigTest.class);
    protected Environment env;
    protected JtwigModelMap model;
    protected CompileContext compileContext;
    protected RenderContext renderContext;
    protected Loader.Resource resource;
    protected OutputStream output;
    
    @Before
    public void before() throws Exception {
        output = new ByteArrayOutputStream();
        env = buildEnvironment();
//        env.setLoader(new ClasspathLoader());
        model = new JtwigModelMap();
        resource = mock(Loader.Resource.class);
        buildContexts();
    }
    protected Environment buildEnvironment() {
        return spy(new Environment());
    }
    protected void buildContexts() {
        compileContext = spy(new CompileContext(resource, env));
        renderContext = spy(RenderContext.create(env, model, output));
    }
    
    //~ Getters and setters ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Environment theEnvironment() {
        return env;
    }
    public JtwigModelMap theModel() {
        return model;
    }
    public CompileContext theCompileContext() {
        return compileContext;
    }
    public RenderContext theRenderContext() {
        return renderContext;
    }
    public Loader.Resource theResource() {
        return resource;
    }
    public OutputStream theOutputStream() {
        return output;
    }
    
    //~ Operational ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void render() throws Exception {
        env.compile(resource)
                .render(renderContext);
    }
    public String theResult() throws Exception {
        output = new ByteArrayOutputStream();
        buildContexts();
        render();
        return output.toString();
    }
    public String theResultOf(Loader.Resource resource) throws Exception {
        this.resource = resource;
        return theResult();
    }
    public Object theResultOf(CompilableExpression expr) throws Exception {
        output = new ByteArrayOutputStream();
        buildContexts();
        return expr.compile(compileContext).calculate(renderContext);
    }
    public String theResultOf(Compilable compilable) throws Exception {
        output = new ByteArrayOutputStream();
        buildContexts();
        compilable.compile(compileContext).render(renderContext);
        return output.toString();
    }
    
    //~ Template builders ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    protected Loader.Resource classpathResource(String resource) throws ResourceException {
        return new ClasspathLoader().get(resource);
    }

    protected Loader.Resource stringResource(String template) {
        return new StringLoader(template).get(template);
    }

    protected Loader.Resource theTemplate(String template) {
        return stringResource(template);
    }
    
    protected void withResource(Loader.Resource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("No resource given");
        }
        this.resource = spy(resource);
    }
    
    protected void withResource(String template) throws ResourceException {
        this.resource = mock(Loader.Resource.class);
        when(resource.source()).thenReturn(new ByteArrayInputStream(template.getBytes()));
        when(resource.relativePath()).thenReturn("<primary>");
        when(resource.canonicalPath()).thenReturn("<primary>");
        when(resource.getCacheKey()).thenReturn(LoaderUtil.getCacheKey("<primary>"));
        when(resource.toString()).thenReturn("<primary>");
        when(env.load("<primary>")).thenReturn(resource);
        doAnswer(new ReturnsArgumentAt(0)).when(resource).resolve(any(String.class));
    }
    
    protected Compilable traceParse(String template) {
        JtwigContentParser parser = Parboiled.createParser(JtwigContentParser.class, stringResource(template), env);
        return traceParse(template, parser.start(), Compilable.class);
    }
    protected <T> T traceParse(String template, Rule rule, Class<T> cls) {
        TracingParseRunner<T> runner = new TracingParseRunner<>(rule);
        ParsingResult<T> result = runner.run(template);
        LOGGER.debug("{}", runner.getLog());
        return result.resultValue;
    }
}
