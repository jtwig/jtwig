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

package org.jtwig.unit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.jtwig.cache.impl.ExecutionCache;
import org.jtwig.compile.CompileContext;
import org.jtwig.compile.config.CompileConfiguration;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.RenderException;
import org.jtwig.exception.ResourceException;
import org.jtwig.parser.JtwigParser;
import org.jtwig.render.RenderContext;
import org.jtwig.render.stream.RenderStream;
import org.jtwig.resource.JtwigResource;
import org.junit.Before;
import static org.mockito.Mockito.*;

public abstract class AbstractUnitTest {
    protected JtwigResource resource;
    protected Map<String, JtwigResource> resources;
    protected JtwigModelMap model;
    protected CompileContext compileContext;
    protected RenderContext renderContext;
    protected JtwigTemplate underTest;
    protected JtwigParser parser;
    protected ByteArrayOutputStream outputStream;
    
    @Before
    public void before() {
        parser = mock(JtwigParser.class);
        
        resource = mock(JtwigResource.class);
        resources = new HashMap<>();
        model = new JtwigModelMap();
        compileContext = spy(new CompileContext(resource, parser, mock(CompileConfiguration.class)));
        renderContext = mock(RenderContext.class);
        when(renderContext.renderStream()).thenReturn(mock(RenderStream.class));
        underTest = new JtwigTemplate(resource, new JtwigConfiguration(new ExecutionCache()));
    }
    
    protected void withPrimaryResource(final String contents) throws ResourceException {
        withPrimaryResource("_primary_", contents);
    }
    protected void withPrimaryResource(final String name, final String contents) throws ResourceException {
        when(compileContext.retrieve(name)).thenReturn(resource);
        when(resource.retrieve()).thenReturn(new ByteArrayInputStream(contents.getBytes()));
        when(resource.path()).thenReturn(name);
        resources.put(name, resource);
    }
    protected void attachResource(final String name, final String contents) throws ResourceException {
        attachResource("_primary_", name, contents);
    }
    protected void attachResource(final String to, final String name, final String contents) throws ResourceException {
        JtwigResource tmp = mock(JtwigResource.class);
        when(tmp.retrieve()).thenReturn(new ByteArrayInputStream(contents.getBytes()));
        when(tmp.path()).thenReturn(name);
        when(resources.get(to).resolve(name)).thenReturn(tmp);
        resources.put(name, tmp);
    }
    
    protected String theResult() throws ParseException, CompileException, RenderException {
        underTest.output(toTheOutputStream(), model);
        return outputStream.toString();
    }
    private OutputStream toTheOutputStream() {
        outputStream = new ByteArrayOutputStream();
        return outputStream;
    }
}