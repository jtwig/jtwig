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

import com.lyncode.builder.MapBuilder;
import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.ParseBypassException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.functions.JtwigFunction;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.functions.internal.list.Join;
import com.lyncode.jtwig.tree.api.Expression;
import org.junit.Test;
import org.parboiled.Rule;
import org.parboiled.errors.ParserRuntimeException;
import org.parboiled.parserunners.ReportingParseRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.parboiled.Parboiled.createParser;

public class JtwigExpressionParserTest {
    private JtwigContext context = new JtwigContext();
    private JtwigExpressionParser underTest = createParser(JtwigExpressionParser.class);

    @Test
    public void variableTest() throws Exception {
        context.withModelAttribute("variable", "must");
        String value = theResult("variable    ", underTest.expression(), String.class);
        assertThat(value, is(equalTo("must")));
    }

    @Test(expected = ParseException.class)
    public void incompleteEnumeratedList() throws Exception {
        theResult("[1", underTest.expression(), List.class);
    }

    @Test
    public void completeEnumeratedList() throws Exception {
        List list = theResult("[1]", underTest.expression(), List.class);
        assertThat(list.size(), equalTo(1));
        int actual = (Integer) list.get(0);
        assertThat(actual, is(equalTo(1)));
    }
    @Test
    public void completeEnumeratedEmptyList() throws Exception {
        List list = theResult("[]", underTest.expression(), List.class);
        assertThat(list.size(), equalTo(0));
    }

    @Test
    public void comprehensionListInteger() throws Exception {
        List list = theResult("1..3", underTest.expression(), List.class);
        assertThat(list.size(), equalTo(3));
    }

    @Test
    public void comprehensionListCharacter() throws Exception {
        List list = theResult("'a'..'c'", underTest.expression(), List.class);
        assertThat(list.size(), equalTo(3));
    }

    @Test
    public void mapTest() throws Exception {
        Map map = theResult("{ a: 1 }", underTest.expression(), Map.class);
        assertThat(map.size(), equalTo(1));
        assertThat(map.get("a"), instanceOf(Integer.class));
    }

    @Test(expected = ParseException.class)
    public void incompleteMapTest() throws Exception {
        theResult("{ a: 1 ", underTest.expression(), Map.class);
    }

    @Test(expected = ParseException.class)
    public void incompleteFunctionTest() throws Exception {
        theResult("hello (abc", underTest.expression(), Map.class);
    }

    @Test
    public void completeFunctionTest() throws Exception {
        context.withModelAttribute("abc", "1");
        context.withFunction("hello", identityFunction());
        String value = theResult("hello (abc)", underTest.expression(), String.class);
        assertThat(value, is(equalTo("1")));
    }

    @Test
    public void completeFunctionWithoutBracketsTest() throws Exception {
        context.withModelAttribute("abc", "1");
        context.withFunction("hello", identityFunction());
        String value = theResult("hello abc", underTest.expression(), String.class);
        assertThat(value, is(equalTo("1")));
    }


    @Test(expected = ParseException.class)
    public void incompleteMapEntry() throws Exception {
        theResult("hello[a", underTest.expression(), Map.class);
    }

    @Test
    public void completeMapEntryTest() throws Exception {
        context.withModelAttribute("hello", new MapBuilder<Integer, String>().withPair(1, "1").build());
        String value = theResult("hello[1]", underTest.expression(), String.class);
        assertThat(value, is(equalTo("1")));
    }

    @Test(expected = ParseException.class)
    public void incompleteSelection() throws Exception {
        theResult("hello .", underTest.expression(), String.class);
    }

    @Test
    public void completeSelection() throws Exception {
        context.withModelAttribute("hello", new ArrayList<>());
        Integer value = theResult("hello .size", underTest.expression(), Integer.class);
        assertThat(value, is(equalTo(0)));
    }

    @Test
    public void compositionTest() throws Exception {
        context.withFunction("join", new Join());
        String value = theResult("1..5 | join(',')", underTest.expression(), String.class);
        assertThat(value, is(equalTo("1,2,3,4,5")));
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

    private JtwigFunction identityFunction() {
        return new JtwigFunction() {
            @Override
            public Object execute(Object... arguments) throws FunctionException {
                return arguments[0];
            }
        };
    }
}
