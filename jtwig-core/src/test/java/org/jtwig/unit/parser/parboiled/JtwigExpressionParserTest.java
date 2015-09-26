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

package org.jtwig.unit.parser.parboiled;

import org.jtwig.Environment;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.unit.AbstractJtwigTest;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JtwigExpressionParserTest extends AbstractJtwigTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JtwigExpressionParserTest.class);
    private final Environment env = new Environment();
    private final JtwigExpressionParser exprParser = Parboiled.createParser(JtwigExpressionParser.class, null, env);
    
    @Test
    public void canParseNumericConstant() throws Exception {
        assertEquals(1, theResultOf(parse("1")));
        assertEquals(-1L, theResultOf(parse("-1")));
    }
    
    @Test
    public void canParseSimpleAddition() throws Exception {
        assertEquals(2L, theResultOf(parse("1+1")));
    }
    
    @Test
    public void canParseBooleanStatements() throws Exception {
        assertEquals(true, theResultOf(parse("true and true")));
        assertEquals(false, theResultOf(parse("true and false")));
        assertEquals(false, theResultOf(parse("false and true")));
        assertEquals(false, theResultOf(parse("false and false")));
    }
    
    @Test
    public void canParseBitwiseStatements() throws Exception {
        model.withModelAttribute("var", new Object());
        
        assertEquals(1L, theResultOf(parse("1 b-and 5")));
        assertEquals(5L, theResultOf(parse("1 b-or 5")));
//        assertEquals(5, theResultOf(parse("var b-or 5")));
//        assertEquals("uest3", theResultOf(parse("'test3' b-or 5")));
        assertEquals(4L, theResultOf(parse("1 b-xor 5"))); // 0001 ^ 0101 = 0100 (4)
//        assertEquals("ok", theResultOf(parse("(1 and 0 b-or 0) is same as(1 and (0 b-or 0)) ? 'ok' : 'ko'")));
    }
    
    @Test
    public void parsesVariables() throws Exception {
        model.withModelAttribute("var", "hello");
        
        assertEquals("hello", theResultOf(parse("var")));
    }
    
    
    protected CompilableExpression parse(final String parse) {
//        TracingParseRunner<CompilableExpression> runner = new TracingParseRunner<CompilableExpression>(exprParser.expression());
        ReportingParseRunner<CompilableExpression> runner = new ReportingParseRunner<>(exprParser.expression());
        ParsingResult<CompilableExpression> result = runner.run(parse);
//        LOGGER.error("{}",runner.getLog());
        return result.resultValue;
    }
}