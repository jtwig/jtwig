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

package org.jtwig.unit.expressions.model;

import org.jtwig.JtwigModelMap;
import org.jtwig.compile.CompileContext;
import org.jtwig.exception.ParseBypassException;
import org.jtwig.expressions.model.Constant;
import org.jtwig.expressions.model.ValueList;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.jtwig.render.config.RenderConfiguration;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.jtwig.expressions.model.ValueList.create;
import static org.mockito.Mockito.mock;

public class ValueListTest {
    private static final JtwigPosition POSITION = new JtwigPosition(null, 1, 1);

    @Test
    public void resultIsAList() throws Exception {
        ValueList list = create(null, new Constant(1), new Constant(2));
        CompileContext context = mock(CompileContext.class);
        JtwigModelMap modelMap = new JtwigModelMap();

        Object result = list.compile(context).calculate(RenderContext.create(new RenderConfiguration(), modelMap, null));

        assertThat(result, is(instanceOf(List.class)));
    }

    @Test(expected = ParseBypassException.class)
    public void invalidLimitTypes() throws Exception {
        create(POSITION, new Constant(1), new Constant('a'));
    }
    @Test(expected = ParseBypassException.class)
    public void invalidLimitTypes2() throws Exception {
        create(POSITION, new Constant('a'), new Constant(1));
    }
    @Test(expected = ParseBypassException.class)
    public void invalidLimitTypes3() throws Exception {
        create(POSITION, new Constant("a"), new Constant("b"));
    }
}
