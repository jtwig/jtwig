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

package com.lyncode.jtwig.unit.expressions.model;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.expressions.model.FunctionElement;
import com.lyncode.jtwig.functions.parameters.GivenParameters;
import com.lyncode.jtwig.render.RenderContext;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FunctionElementTest {
    private FunctionElement functionElement = new FunctionElement(null, "name");

    @Test
    public void expressionCalculationQueriesContext() throws Exception {
        JtwigContext context = mock(JtwigContext.class);
        functionElement
                .compile(null)
                .calculate(RenderContext.create(null, context, null));

        verify(context).executeFunction(eq("name"), any(GivenParameters.class));
    }
}
