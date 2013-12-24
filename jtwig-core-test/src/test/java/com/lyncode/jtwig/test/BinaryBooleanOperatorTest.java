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

package com.lyncode.jtwig.test;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ExpectingExpressionException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BinaryBooleanOperatorTest extends AbstractJtwigTest {

    @Test(expected = ExpectingExpressionException.class)
    public void AndBadSyntax () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if (items && true) %}Hi{% endif %}");
        JtwigContext context = new JtwigContext();
        template.output(context);
    }

    @Test
    public void AndGoodSyntax () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if (items and true) %}Hi{% endif %}");
        JtwigContext context = new JtwigContext();
        ArrayList<String> value = new ArrayList<String>();
        value.add("a");
        context.withModelAttribute("items", value);
        assertThat(template.output(context), is("Hi"));
    }

    @Test(expected = ExpectingExpressionException.class)
    public void OrBadSyntax () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if (items || true) %}Hi{% endif %}");
        JtwigContext context = new JtwigContext();
        template.output(context);
    }

    @Test
    public void OrGoodSyntax () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if (false or items) %}Hi{% endif %}");
        JtwigContext context = new JtwigContext();
        ArrayList<String> value = new ArrayList<String>();
        value.add("a");
        context.withModelAttribute("items", value);
        assertThat(template.output(context), is("Hi"));
    }

    @Test
    public void StartsWith () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if ('Hello' starts with 'H') %}Hi{% endif %}");
        assertThat(template.output(new JtwigContext()), is("Hi"));
    }

    @Test
    public void StartsWithFail () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if ('Hello' starts with 'e') %}Hi{% endif %}");
        assertThat(template.output(new JtwigContext()), is(""));
    }

    @Test
    public void EndsWith () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if ('Hello' ends with 'llo') %}Hi{% endif %}");
        assertThat(template.output(new JtwigContext()), is("Hi"));
    }

    @Test
    public void EndsWithFail () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if ('Hello' ends with 'a') %}Hi{% endif %}");
        assertThat(template.output(new JtwigContext()), is(""));
    }

    @Test
    public void Matches () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if ('Hello' matches 'H.*') %}Hi{% endif %}");
        assertThat(template.output(new JtwigContext()), is("Hi"));
    }

    @Test
    public void MatchesFail () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if ('Hello' matches '^A.*') %}Hi{% endif %}");
        assertThat(template.output(new JtwigContext()), is(""));
    }
}
