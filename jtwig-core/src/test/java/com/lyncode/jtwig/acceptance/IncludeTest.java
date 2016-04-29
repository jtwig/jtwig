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

package com.lyncode.jtwig.acceptance;

import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import org.junit.Assert;
import org.junit.Test;

import static com.lyncode.jtwig.util.SyntacticSugar.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class IncludeTest extends AbstractJtwigTest {
    @Test
    public void basicExample() throws Exception {
        when(jtwigRenders(templateResource("templates/acceptance/include/main.twig")));
        then(theRenderedTemplate(), is(equalTo("test")));
    }
    
    @Test
    public void includeWithVars() throws Exception {
        when(jtwigRenders(templateResource("templates/acceptance/include/main-vars.twig")));
        then(theRenderedTemplate(), is(equalTo("hello, world")));
    }

    @Test
    public void includeWithPathParameters() throws Exception {
        when(jtwigRenders(templateResource("templates/acceptance/include/main-template-withpathparameters.twig")));
        then(theRenderedTemplate(), is(equalTo("Hi!")));
    }

    @Test
    public void includeWithPathParameter() throws Exception {
        when(jtwigRenders(templateResource("templates/acceptance/include/main-template-withpathparameter.twig")));
        then(theRenderedTemplate(), is(equalTo("test")));
    }

    @Test
    public void includeWithPathParametersError() throws Exception {
        try {
            when(jtwigRenders(templateResource("templates/acceptance/include/main-template-withpathparameterserror.twig")));
            Assert.fail("Should have received a render exception stating that the path should contain the placeholder.");
        } catch (RenderException e) {}

    }
    
    @Test
    public void includeOnlyCannotAccessContext() throws Exception {
        given(aModel().add("variable", "test"));
        when(jtwigRenders(templateResource("templates/acceptance/include/include-only.twig")));
        then(theRenderedTemplate(), is(equalTo("")));
    }
    
    @Test
    public void includeWithOnlyCannotAccessContext() throws Exception {
        given(aModel().add("variable", "pink"));
        when(jtwigRenders(templateResource("templates/acceptance/include/include-with-only.twig")));
        then(theRenderedTemplate(), is(equalTo("pink-purple-")));
    }
    
    @Test
    public void includeMissingWithoutFlagShouldThrowException() throws Exception {
        try {
            when(jtwigRenders(templateResource("templates/acceptance/include/ignore-missing-exception.twig")));
            Assert.fail("Should have received a compile exception stating that the resource could not be found.");
        } catch (CompileException e) {}
    }
    
    @Test
    public void includeMissingWithFlagShouldNotThrowException() throws Exception {
        when(jtwigRenders(templateResource("templates/acceptance/include/ignore-missing-working.twig")));
        then(theRenderedTemplate(), is(equalTo("start--end")));
    }
}