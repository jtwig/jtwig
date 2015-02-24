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

import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import org.jtwig.unit.AbstractJtwigTest;
import org.jtwig.exception.ParseBypassException;
import org.jtwig.expressions.model.Constant;
import org.jtwig.expressions.model.ValueList;
import static org.jtwig.expressions.model.ValueList.create;
import org.jtwig.parser.model.JtwigPosition;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValueListTest extends AbstractJtwigTest {
    private static final JtwigPosition POSITION = new JtwigPosition(null, 1, 1);

    @Test
    public void resultIsAList() throws Exception {
        ValueList list = create(null, new Constant(1), new Constant(2));
        Object result = list.compile(compileContext).calculate(renderContext);
        assertThat(result, is(instanceOf(List.class)));
    }
    
    @Test
    public void ensureCharacterLimitsWork() throws Exception {
        ValueList list = create(null, new Constant('a'), new Constant('c'));
        List result = (List)list.compile(compileContext).calculate(renderContext);
        assertEquals('a', result.get(0));
        assertEquals('b', result.get(1));
        assertEquals('c', result.get(2));
    }
    
    @Test
    public void ensureInvertedNumericLimitsWork() throws Exception {
        ValueList list = create(null, new Constant(5), new Constant(1));
        List result = (List)list.compile(compileContext).calculate(renderContext);
        assertEquals(5, result.get(0));
        assertEquals(3, result.get(2));
        assertEquals(1, result.get(4));
    }
    
    @Test
    public void ensureInvertedCharacterLimitsWork() throws Exception {
        ValueList list = create(null, new Constant('e'), new Constant('a'));
        List result = (List)list.compile(compileContext).calculate(renderContext);
        assertEquals('e', result.get(0));
        assertEquals('c', result.get(2));
        assertEquals('a', result.get(4));
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
    @Test(expected = ParseBypassException.class)
    public void invalidLimitTypes4() throws Exception {
        create(POSITION, new Constant(new Object()), new Constant(new Object()));
    }
}