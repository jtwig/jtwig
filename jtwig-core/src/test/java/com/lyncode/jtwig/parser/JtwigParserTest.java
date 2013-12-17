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

import com.lyncode.jtwig.tree.content.*;
import com.lyncode.jtwig.tree.helper.ElementList;
import com.lyncode.jtwig.tree.structural.BlockExpression;
import com.lyncode.jtwig.tree.structural.ExtendsExpression;
import com.lyncode.jtwig.tree.structural.IncludeExpression;
import com.lyncode.jtwig.tree.value.*;
import org.junit.Test;
import org.parboiled.Rule;
import org.parboiled.errors.ParserRuntimeException;
import org.parboiled.parserunners.ReportingParseRunner;

import static com.lyncode.jtwig.matcher.ElementListMatcher.emptyElementList;
import static com.lyncode.jtwig.matcher.ElementListMatcher.hasElement;
import static com.lyncode.jtwig.matcher.ElementMapMatcher.emptyElementMap;
import static com.lyncode.jtwig.matcher.ElementMapMatcher.mapsElement;
import static com.lyncode.jtwig.matcher.OperationMatchers.*;
import static com.lyncode.jtwig.matcher.TreeMatchers.*;
import static com.lyncode.jtwig.tree.value.Operator.NOT;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.parboiled.Parboiled.createParser;

public class JtwigParserTest {

    private JtwigParser underTest = createParser(JtwigParser.class);

    @Test
    public void shouldMatchContent() throws Exception {
        Content exp = parse(underTest.Content(),
                "{% block asd %}{% endblock      %}testest", Content.class);
        assertThat(exp,
                allOf(
                        hasElement(block(is(equalTo("asd")))),
                        hasElement(text(is(equalTo("testest"))))
                )
        );
    }

    @Test
    public void shouldMatchBlockExpression() throws Exception {
        BlockExpression exp = parse(underTest.BlockExpression(),
                "{% block asd %}{% endblock      %}", BlockExpression.class);
        assertThat(exp.getName(), is("asd"));
    }

    @Test
    public void shouldMatchExtendsExpression() throws Exception {
        ExtendsExpression exp = parse(underTest.ExtendsExpression(),
                "{% extends 'asd' %}", ExtendsExpression.class);
        assertThat(exp.getPath(), is("asd"));
    }

    @Test
    public void shouldMatchIncludeExpression() throws Exception {
        IncludeExpression exp = parse(underTest.IncludeExpression(),
                "{% include 'asd' %}", IncludeExpression.class);
        assertThat(exp.getPath(), is("asd"));
    }

    @Test
    public void shouldMatchTextExpressionWithoutComment() throws Exception {
        Text exp = parse(underTest.TextExpression(),
                "O{# nothing #}A", Text.class);
        assertThat(exp.getText(), is("OA"));
    }

    @Test
    public void shouldMatchIfAndElseWithElseIfsExpression() throws Exception {
        IfExpression exp = parse(underTest.IfExpression(),
                "{% if a   %}" +
                        "{% elseif a %}" +
                        "{% elseif 'asd' %}" +
                "{% else %}" +
                "{% endif %}", IfExpression.class);
        assertThat((Variable) exp.getConditionalExpression(), is(variable(equalTo("a"))));
        assertThat(exp.getElseIfExpressions().size(), is(2));
        assertThat(exp.getElseExpression(), is(notNullValue()));
    }

    @Test
    public void shouldMatchIfAndElseExpression() throws Exception {
        IfExpression exp = parse(underTest.IfExpression(), "{% if a   %}" +
                "{% else %}{% endif %}", IfExpression.class);
        assertThat((Variable) exp.getConditionalExpression(), is(variable(equalTo("a"))));
        assertThat(exp.getElseExpression(), is(notNullValue()));
    }

    @Test
    public void shouldMatchSingleIfExpression() throws Exception {
        IfExpression exp = parse(underTest.IfExpression(), "{% if a   %}" +
                "{% endif %}", IfExpression.class);
        assertThat((Variable) exp.getConditionalExpression(), is(variable(equalTo("a"))));
        assertThat(exp.getElseExpression(), is(nullValue()));
    }

    @Test
    public void shouldMatchForExpression() throws Exception {
        ForExpression exp = parse(underTest.ForExpression(), "{% for a in [1:10] %}" +
                "{% endfor %}", ForExpression.class);
        assertThat(exp.getItem().getIdentifier(), is("a"));
        assertThat((ElementList) exp.getList(), hasElement(2));
    }

    @Test(expected = ParserRuntimeException.class)
    public void shouldNotMatchForExpression() throws Exception {
        ForExpression exp = parse(underTest.ForExpression(), "{% for a in [1:10] %}" +
                "{% endfora %}", ForExpression.class);
    }
    @Test(expected = ParserRuntimeException.class)
    public void unknownExpression() throws Exception {
        ForExpression exp = parse(underTest.Content(), "{% fora a in [1:10] %}" +
                "{% endfora %}", ForExpression.class);
    }

    @Test
    public void shouldMatchSetExpression() throws Exception {
        SetExpression exp = parse(underTest.SetExpression(), "{% set a = 'b' %}", SetExpression.class);
        assertThat(exp.getName().getIdentifier(), is("a"));
        assertThat((String) exp.getAssignment(), is("b"));
    }

    @Test
    public void shouldMatchFastExpression() throws Exception {
        FastExpression exp = parse(underTest.FastExpression(), "{{ 'test' |     translate | joao(joao) }}", FastExpression.class);
        assertThat(exp.getExpression(), is(notNullValue()));
        assertThat(exp.getFilters(), allOf(hasItem(function(equalTo("translate"))), hasItem(function(equalTo("joao")))));
    }

    @Test
    public void shouldMatchComplexExpression () {
        OperationBinary parse = parse(underTest.Expression(), "!2 && 2 + 2 * 3", OperationBinary.class);
        assertThat(parse, anOperation(binary()
                .whichOperand(0, is(anOperation(unary().withOperator(NOT).and().withOperand(is(2)))))
                .whichOperator(0, Operator.AND)
                .whichOperand(1, is(anOperation(binary()
                        .whichOperand(0, is(2))
                        .whichOperator(0, Operator.ADD)
                        .whichOperand(1, is(anOperation(binary()
                                .whichOperand(0, is(2))
                                .whichOperator(0, Operator.TIMES)
                                .whichOperand(1, is(3))
                        )))
                )))));
    }

    @Test
    public void shouldMatchAndExpression () {
        OperationBinary parse = parse(underTest.Expression(), "!2 && 2", OperationBinary.class);
        assertThat(parse, anOperation(binary()
                .whichOperand(0, is(anOperation(unary().withOperator(NOT).and().withOperand(is(2)))))
                .whichOperand(1, is(2))
                .whichOperator(0, Operator.AND)));
    }
    @Test
    public void shouldNotMatchModExpression () {
        Integer parse = parse(underTest.Expression(), "2 %", Integer.class);
        assertThat(parse, is(2));
    }
    @Test
    public void shouldNotMatchExpression () {
        OperationBinary parse = parse(underTest.Expression(), " ", OperationBinary.class);
        assertThat(parse, is(nullValue()));
    }

    @Test
    public void shouldMatchMinusExpression () {
        OperationBinary parse = parse(underTest.Expression(), "2 - 2", OperationBinary.class);
        assertThat(parse, binary().whichOperand(0, is(2))
                .whichOperand(1, is(2))
                .whichOperator(0, Operator.SUB).build());
    }

    @Test
    public void shouldMatchStringSingleQuoted () {
        String composition = parse(underTest.Primary(), "'hello'", String.class);
        assertThat(composition, equalTo("hello"));
    }

    @Test
    public void shouldMatchStringDoubleQuoted () {
        String composition = parse(underTest.Primary(), "\"hello\"", String.class);
        assertThat(composition, equalTo("hello"));
    }
    @Test
    public void shouldNotMatchCompositionWithNativeTypesAtTheEnd () {
        Composition composition = parse(underTest.Composition(), "hello.123", Composition.class);
        assertThat(composition, is(nullValue()));
    }

    @Test
    public void shouldMatchCompositionOfVariables () {
        Composition composition = parse(underTest.Composition(), "hello.dois", Composition.class);
        assertThat(composition, hasElement(variable(equalTo("hello"))));
        assertThat(composition, hasElement(variable(equalTo("dois"))));
    }

    @Test
    public void shouldMatchCompositionOfVariableAndFunction () {
        Composition composition = parse(underTest.Composition(), "hello.dois()", Composition.class);
        assertThat(composition, hasElement(variable(equalTo("hello"))));
        assertThat(composition, hasElement(function(equalTo("dois"))));
    }

    @Test
    public void shouldMatchCompositionFunction () {
        FunctionElement composition = parse(underTest.Primary(), "dois()", FunctionElement.class);
        assertThat(composition, function(equalTo("dois")));
    }

    @Test
    public void shouldMatchFunctionWithoutArgs () {
        FunctionElement value = parse(underTest.Function(), "joao()", FunctionElement.class);
        assertThat(value.getName(), is("joao"));
        assertThat(value.getArguments(), is(emptyElementList()));
    }

    @Test
    public void shouldMatchFunction () {
        FunctionElement value = parse(underTest.Function(), "joao(123,234)", FunctionElement.class);
        assertThat(value.getName(), is("joao"));
        assertThat(value.getArguments(), hasElement(123));
        assertThat(value.getArguments(), hasElement(234));
    }

    @Test
    public void shouldNotMatchInlineMapNeitherHaveMapOnTopOfTheStack () {
        ElementMap value = parse(underTest.MapExpression(), "{{", ElementMap.class);
        assertThat(value, is(nullValue()));
    }

    @Test
    public void shouldMatchInlineMap () {
        ElementMap value = parse(underTest.MapExpression(), "{ joao: 123 }", ElementMap.class);
        assertThat(value, mapsElement("joao", is(123)));
    }

    @Test
    public void shouldMatchInlineEmptyMap () {
        ElementMap value = parse(underTest.MapExpression(), "{}", ElementMap.class);
        assertThat(value, emptyElementMap());
    }

    @Test
    public void shouldMatchAsKeyWord () {
        for (JtwigKeyword keyword : JtwigKeyword.values()) {
            assertThat(matches(underTest.Keyword(), keyword.getKeyword()), is(true));
        }
    }

    @Test
    public void shouldMatchInlineNestedList () {
        ElementList value = parse(underTest.ListExpression(), "[[4],[]]", ElementList.class);
        assertThat(value, hasElement(hasElement(4)));
        assertThat(value, hasElement(emptyElementList()));
    }

    @Test
    public void shouldMatchInlineList () {
        ElementList value = parse(underTest.ListExpression(), "[1,2,3,4]", ElementList.class);
        assertThat(value, allOf(hasElement(1), hasElement(2), hasElement(3), hasElement(4)));
    }

    @Test
    public void shouldMatchIntegerList () {
        IntegerList value = parse(underTest.ListExpression(), "[1:10]", IntegerList.class);
        assertThat(value.getStart(), is(1));
        assertThat(value.getEnd(), is(10));
    }

    @Test
    public void shouldMatchInteger () {
        assertThat(matches(underTest.Integer(), "123"), is(true));
    }

    @Test
    public void shouldMatchBoolean () {
        assertThat(matches(underTest.Boolean(), "true"), is(true));
        assertThat(matches(underTest.Boolean(), "false"), is(true));
    }

    @Test
    public void shouldMatchDouble () {
        assertThat(matches(underTest.Double(), "123.123"), is(true));
        assertThat(matches(underTest.Double(), "123. 123"), is(false));
    }

    @Test
    public void shouldMatchIdentifier () {
        assertThat(matches(underTest.Identifier(), "abc"), is(true));
        assertThat(matches(underTest.Identifier(), "abc12"), is(true));
        assertThat(matches(underTest.Identifier(), "_abc"), is(true));
        assertThat(matches(underTest.Identifier(), "_12abc"), is(true));
    }

    @Test
    public void shouldNotMatchIdentifier () {
        assertThat(matches(underTest.Identifier(), "12abc"), is(false));
    }

    @Test
    public void mustNotMatchKeyword () {
        assertThat(matches(underTest.Keyword(), "nonsense"), is(false));
    }

    private boolean matches(Rule rule, String input) {
        return new ReportingParseRunner<Object>(rule).run(input).matched;
    }

    private <T> T parse(Rule rule, String input, Class<T> entity) {
        return (T) new ReportingParseRunner<Object>(rule).run(input).resultValue;
    }

    private OperationBinaryMatcherBuilder binary() {
        return new OperationBinaryMatcherBuilder();
    }
    private OperationUnaryMatcherBuilder unary() {
        return new OperationUnaryMatcherBuilder();
    }
}
