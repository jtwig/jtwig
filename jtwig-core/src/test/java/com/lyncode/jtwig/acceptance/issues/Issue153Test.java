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

package com.lyncode.jtwig.acceptance.issues;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.acceptance.AbstractJtwigTest;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.resource.JtwigResource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.lyncode.jtwig.util.SyntacticSugar.*;
import static com.lyncode.jtwig.util.matchers.ExceptionMatcher.exception;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Issue153Test extends AbstractJtwigTest {
    private JtwigResource resource = mock(JtwigResource.class);
    private JtwigContext context = new JtwigContext();
    private JtwigTemplate underTest = new JtwigTemplate(resource);
    private ByteArrayOutputStream outputStream;
    
    @Test
    public void supportSetTagInChildTemplate() throws Exception {
        JtwigResource joaoResource = mock(JtwigResource.class);

        when(resource.resolve("parent")).thenReturn(joaoResource);
        when(joaoResource.retrieve()).thenReturn(new ByteArrayInputStream("{{ value }}".getBytes()));

        when(resource.retrieve()).thenReturn(new ByteArrayInputStream(("{% extends 'parent' %}" +
                "{% set value = 'success' %}").getBytes()));

        underTest.output(toTheOutputStream(), context);

        assertThat(theOutput(), is("success"));
    }

    //~ Helpers ================================================================
    private String theOutput() {
        return outputStream.toString();
    }

    private OutputStream toTheOutputStream() {
        outputStream = new ByteArrayOutputStream();
        return outputStream;
    }
}