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

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.ParseBypassException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.functions.JtwigFunction;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.tree.api.Content;
import org.junit.Test;
import org.parboiled.errors.ParserRuntimeException;
import org.parboiled.parserunners.ReportingParseRunner;

import java.io.ByteArrayOutputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JtwigContentParserTest {
    private JtwigContext context = new JtwigContext();
    private JtwigParser underTest = new JtwigParser.Builder().build();


    @Test
    public void simpleOutput() throws Exception {
        context.withModelAttribute("out", "test");
        assertThat(theResult("{{ out }}"), is(equalTo("test")));
    }

    @Test
    public void outputWithMathExpression() throws Exception {
        context.withModelAttribute("out", "test");
        assertThat(theResult("{{ 2 * 3 }}"), is(equalTo("6")));
    }


    public String theResult(String input) throws ParseException, RenderException {
        ReportingParseRunner<Content> runner = new ReportingParseRunner<Content>(underTest.content());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            runner.run(input).resultValue.render(outputStream, context);
            return outputStream.toString();
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
