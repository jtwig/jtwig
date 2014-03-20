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

import com.lyncode.jtwig.resource.JtwigResource;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JtwigTemplateTest {
    private JtwigResource resource = mock(JtwigResource.class);
    private JtwigContext context = new JtwigContext();
    private JtwigTemplate underTest = new JtwigTemplate(resource);
    private ByteArrayOutputStream outputStream;

    @Test
    public void testRootDocument() throws Exception {
        when(resource.retrieve()).thenReturn(new ByteArrayInputStream("joao".getBytes()));
        underTest.output(toTheOutputStream(), context);

        assertThat(theOutput(), is("joao"));
    }

    @Test
    public void testSingleHierarchy() throws Exception {
        JtwigResource joaoResource = mock(JtwigResource.class);

        when(resource.retrieve()).thenReturn(new ByteArrayInputStream(("{% extends 'test' %}" +
                "{% block joao %}joao{% endblock %}").getBytes()));
        when(resource.resolve("test")).thenReturn(joaoResource);
        when(joaoResource.retrieve()).thenReturn(new ByteArrayInputStream("I am {% block joao %}no one{% endblock %}".getBytes()));

        underTest.output(toTheOutputStream(), context);

        assertThat(theOutput(), is("I am joao"));
    }

    @Test
    public void testTwoLevelHierarchy() throws Exception {
        JtwigResource oneResource = mock(JtwigResource.class);
        JtwigResource twoResource = mock(JtwigResource.class);

        when(resource.retrieve()).thenReturn(new ByteArrayInputStream(("{% extends 'level-1' %}" +
                "{% block two %}two{% endblock %}").getBytes()));

        when(resource.resolve("level-1")).thenReturn(oneResource);
        when(oneResource.retrieve()).thenReturn(new ByteArrayInputStream(("{% extends 'root' %}" +
                "{% block one %}one{% endblock %}").getBytes()));

        when(oneResource.resolve("root")).thenReturn(twoResource);
        when(twoResource.retrieve()).thenReturn(new ByteArrayInputStream("Block {% block one %}1{% endblock %} and {% block two %}2{% endblock %}".getBytes()));

        underTest.output(toTheOutputStream(), context);

        assertThat(theOutput(), is("Block one and two"));
    }

    private String theOutput() {
        return outputStream.toString();
    }

    private OutputStream toTheOutputStream() {
        outputStream = new ByteArrayOutputStream();
        return outputStream;
    }
}
