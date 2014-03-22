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

package com.lyncode.jtwig.parser;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.ParseBypassException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.tree.api.Expression;
import org.junit.Test;
import org.parboiled.Rule;
import org.parboiled.errors.ParserRuntimeException;
import org.parboiled.parserunners.ReportingParseRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.parboiled.Parboiled.createParser;

public class JtwigConstantParserTest {
    private JtwigContext context = new JtwigContext();
    private JtwigConstantParser underTest = createParser(JtwigConstantParser.class);

    @Test
    public void integerValue() throws Exception {
        Integer integer = theResult("-1", underTest.integerValue(), Integer.class);
        assertThat(integer, is(equalTo(-1)));
    }

    @Test
    public void charValue() throws Exception {
        Character value = theResult("'a'", underTest.charValue(), Character.class);
        assertThat(value, is(equalTo('a')));
    }

    @Test
    public void nullValueTest() throws Exception {
        Object value = theResult("null", underTest.nullValue(), Character.class);
        assertThat(value, is(nullValue()));
    }

    @Test
    public void booleanValue() throws Exception {
        Boolean value = theResult("true", underTest.booleanValue(), Boolean.class);
        assertThat(value, is(true));
    }

    @Test
    public void doubleValue() throws Exception {
        Double value = theResult("1.1", underTest.doubleValue(), Double.class);
        assertThat(value, is(equalTo(1.1)));
    }

    @Test
    public void stringValueWithDoubleQuotes() throws Exception {
        String value = theResult("\"One two\"", underTest.string(), String.class);
        assertThat(value, is(equalTo("One two")));
    }

    @Test
    public void stringValueWithSingleQuotes() throws Exception {
        String value = theResult("'One two'", underTest.string(), String.class);
        assertThat(value, is(equalTo("One two")));
    }

    public <T> T theResult (String input, Rule rule, Class<T> returnClass) throws ParseException {
        ReportingParseRunner<Expression> runner = new ReportingParseRunner<Expression>(rule);
        try {
            return returnClass.cast(runner.run(input).resultValue.calculate(context));
        } catch (CalculateException e) {
            throw new RuntimeException(e);
        } catch (ParserRuntimeException e) {
            if (e.getCause() instanceof ParseBypassException) {
                throw ((ParseBypassException) e.getCause()).getInnerException();
            } else throw e;
        }
    }
}
