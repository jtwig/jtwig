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

package org.jtwig;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.jtwig.compile.CompileContext;
import org.jtwig.exception.ResourceException;
import org.jtwig.loader.Loader;
import org.jtwig.loader.impl.ClasspathLoader;
import org.jtwig.loader.impl.StringLoader;
import org.jtwig.render.RenderContext;
import org.junit.Before;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public abstract class AbstractJtwigTest {
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
        env.setLoader(new ClasspathLoader());
        model = new JtwigModelMap();
        resource = mock(Loader.Resource.class);
        buildContexts();
    }
    protected Environment buildEnvironment() {
        return new Environment();
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
        env.compile(resource, compileContext)
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
    
    protected void withResource(String template) {
        this.resource = spy(stringResource(template));
    }
}
