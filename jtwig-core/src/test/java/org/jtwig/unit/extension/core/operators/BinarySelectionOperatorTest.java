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

package org.jtwig.unit.extension.core.operators;

import org.jtwig.Environment;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.jtwig.exception.CalculateException;
import org.jtwig.expressions.model.Constant;
import org.jtwig.expressions.model.Variable;
import org.jtwig.extension.core.operators.BinarySelectionOperator;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.types.Undefined;
import org.jtwig.unit.AbstractJtwigTest;
import org.jtwig.util.ObjectExtractor;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import static org.mockito.Mockito.*;
import org.junit.Test;

public class BinarySelectionOperatorTest extends AbstractJtwigTest {
    BinarySelectionOperator underTest;
    JtwigPosition position;
    
    @Before
    @Override
    public void before() throws Exception {
        super.before();
        underTest = new BinarySelectionOperator(".", 0);
        position = new JtwigPosition(null, 1, 1);
    }
    
    @Test
    public void generalTest() throws Exception {
        assertEquals(Undefined.UNDEFINED, underTest.render(renderContext, position, new TestClass(), new Variable.Compiled(position, "priv")));
        assertEquals("good", underTest.render(renderContext, position, new TestClass(),  new Variable.Compiled(position, "priv2")));
        assertEquals(Undefined.UNDEFINED, underTest.render(renderContext, position, new TestClass(),  new Variable.Compiled(position, "prot")));
        assertEquals("good", underTest.render(renderContext, position, new TestClass(),  new Variable.Compiled(position, "prot2")));
        assertEquals(Undefined.UNDEFINED, underTest.render(renderContext, position, new TestClass(),  new Variable.Compiled(position, "pack")));
        assertEquals("good", underTest.render(renderContext, position, new TestClass(),  new Variable.Compiled(position, "pack2")));
        assertEquals("good", underTest.render(renderContext, position, new TestClass(),  new Variable.Compiled(position, "pub")));
    }
    
    @Test(expected = CalculateException.class)
    public void extractFromUndefined() throws Exception {
        JtwigConfiguration config = JtwigConfigurationBuilder.newConfiguration()
                .withStrictMode(true)
                .build();
        env = new Environment(config);
        buildContexts();
        underTest.render(renderContext, position, Undefined.UNDEFINED, new Variable.Compiled(position, "test"));
    }
    
    @Test(expected = CalculateException.class)
    public void throwsExceptionOnInvalidRightArg() throws Exception {
        underTest.render(renderContext, position, new Object(), new Object());
    }
    
    @Test(expected = CalculateException.class)
    public void throwsExceptionOnExtractionError() throws Exception {
        Variable.Compiled right = mock(Variable.Compiled.class);
        doThrow(ObjectExtractor.ExtractException.class)
                .when(right)
                .extract(eq(renderContext), any(ObjectExtractor.class));
        underTest.render(renderContext, position, new Object(), right);
    }
    
    public static class TestClass {
        private final String priv = "bad";
        private final String priv2 = "good";
        protected final String prot = "bad";
        protected final String prot2 = "good";
        final String pack = "bad";
        final String pack2 = "good";
        public final String pub = "good";
        
        public String getPriv2() {
            return priv2;
        }
        public String getProt2() {
            return prot2;
        }
        public String getPack2() {
            return pack2;
        }
    }
}