/**
 * Copyright 2012 Lyncode
 *
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

import com.lyncode.jtwig.exception.ParseBypassException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.tree.api.Content;
import com.lyncode.jtwig.tree.content.ForLoop;
import com.lyncode.jtwig.tree.content.JtwigContent;
import org.junit.Test;
import org.parboiled.Rule;
import org.parboiled.errors.ParserRuntimeException;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;

import static com.lyncode.jtwig.matcher.ElementListMatcher.hasElement;
import static com.lyncode.jtwig.matcher.OperationMatchers.OperationBinaryMatcherBuilder;
import static com.lyncode.jtwig.matcher.OperationMatchers.OperationUnaryMatcherBuilder;
import static com.lyncode.jtwig.matcher.TreeMatchers.block;
import static com.lyncode.jtwig.matcher.TreeMatchers.text;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.parboiled.Parboiled.createParser;

public class JtwigParserTest {

    private JtwigParser underTest = createParser(JtwigParser.class);

    @Test
    public void shouldMatchContent() throws Exception {
        JtwigContent exp = parse(underTest.content(),
                "{% block asd %}{% endblock      %}testest", JtwigContent.class);
        assertThat(exp,
                allOf(
                        hasElement(block(is(equalTo("asd")))),
                        hasElement(text(is(equalTo("testest"))))
                )
        );
    }

    @Test
    public void shouldMatchForElement () throws Exception {
        ForLoop loop = parse(underTest.forEach(), "{% for i in items %}{% endfor %}", ForLoop.class);

    }

    private boolean matches(Rule rule, String input) {
        return new ReportingParseRunner<Object>(rule).run(input).matched;
    }

    private <T> T parse(Rule rule, String input, Class<T> entity) throws ParseException {
        try{
            ParsingResult<Content> run = new ReportingParseRunner<Content>(rule).run(input);
            return entity.cast(run.resultValue);
        } catch (ParserRuntimeException e) {
            if (e.getCause() instanceof ParseBypassException) {
                throw ((ParseBypassException) e.getCause()).getInnerException();
            } else throw e;
        }
    }

    private OperationBinaryMatcherBuilder binary() {
        return new OperationBinaryMatcherBuilder();
    }
    private OperationUnaryMatcherBuilder unary() {
        return new OperationUnaryMatcherBuilder();
    }
}
