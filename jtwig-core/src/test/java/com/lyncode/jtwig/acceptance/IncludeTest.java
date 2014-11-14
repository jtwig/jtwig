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
import org.junit.Test;

import static com.lyncode.jtwig.util.SyntacticSugar.then;
import static com.lyncode.jtwig.util.SyntacticSugar.when;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.Assert;

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