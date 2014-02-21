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
import com.lyncode.jtwig.tree.api.Expression;
import com.lyncode.jtwig.tree.expressions.*;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.SuppressNode;

import static com.lyncode.jtwig.parser.JtwigKeyword.NULL;
import static com.lyncode.jtwig.parser.JtwigSymbol.*;
import static com.lyncode.jtwig.parser.JtwigSymbol.DIV;
import static com.lyncode.jtwig.tree.expressions.Operator.*;
import static com.lyncode.jtwig.tree.expressions.Operator.NOT;
import static com.lyncode.jtwig.util.Simplifier.simplify;
import static org.parboiled.Parboiled.createParser;

public class JtwigExpressionParser extends BaseParser<Expression> {
    JtwigBasicParser basic = createParser(JtwigBasicParser.class);
    JtwigConstantParser constants = createParser(JtwigConstantParser.class);

    public Rule expression() {
        return Sequence(
                specificJtwigOperators(),
                push(simplify(pop()))
        );
    }


    Rule specificJtwigOperators() {
        return binary(
                orExpression(),
                Operator.STARTS_WITH,
                Operator.ENDS_WITH,
                Operator.MATCHES,
                Operator.IN
        );
    }


    protected Rule orExpression() {
        return binary(
                andExpression(),
                Operator.OR
        );
    }

    Rule andExpression() {
        return binary(
                equalityExpression(),
                Operator.AND
        );
    }

    Rule equalityExpression() {
        return binary(
                relationalExpression(),
                Operator.EQUAL,
                Operator.DIFF
        );
    }

    Rule relationalExpression() {
        return binary(
                FirstOf(
                        negation(),
                        addition()
                ),
                Operator.LTE,
                Operator.GTE,
                Operator.LT,
                Operator.GT
        );
    }


    Rule negation() {
        return unary(
                addition(),
                Operator.NOT
        );
    }

    Rule addition() {
        return binary(
                multiplication(),
                Operator.ADD,
                Operator.SUB
        );
    }

    Rule multiplication() {
        return binary(
                composition(),
                Operator.INT_DIV,
                Operator.INT_TIMES,
                Operator.TIMES,
                Operator.DIV,
                Operator.MOD
        );
    }

    Rule composition() {
        return binary(
                selection(),
                FirstOf(
                        functionWithBrackets(),
                        variable()
                ),
                COMPOSITION
        );
    }

    Rule selection() {
        return binary(
                isOperation(),
                FirstOf(
                        functionWithBrackets(),
                        mapEntry(),
                        variable()
                ),
                SELECTION
        );
    }

    Rule isOperation() {
        return Sequence(
                primary(),
                push(new OperationBinary(simplify(pop()))),
                ZeroOrMore(
                        operator(IS),
                        popValue(),
                        mandatory(
                                Sequence(
                                        FirstOf(
                                                Sequence(
                                                        operator(NOT),
                                                        popValue(),
                                                        ((OperationBinary) peek()).addOperator(Operator.IS_NOT)
                                                ),
                                                ((OperationBinary) peek()).addOperator(Operator.IS)
                                        ),
                                        FirstOf(
                                                functionWithBrackets(),
                                                functionWithTwoWordsAsName(),
                                                variable(),
                                                keywordAsVariable(NULL)
                                        ),
                                        ((OperationBinary) peek(1)).add(simplify(pop()))
                                ),
                                new ParseException("Wrong binary operation syntax")
                        )

                )
        );
    }

    Rule primary() {
        return FirstOf(
                ternaryOperation(),
                elementar()
        );
    }

    Rule ternaryOperation() {
        return Sequence(
                elementar(),
                push(new OperationTernary(pop())),
                symbol(QUESTION),
                mandatory(
                        Sequence(
                                expression(),
                                ((OperationTernary) peek(1)).setIfTrueExpression(pop()),
                                symbol(DIV),
                                expression(),
                                ((OperationTernary) peek(1)).setIfFalseExpression(pop())
                        ),
                        new ParseException("Wring ternary operation syntax")
                )
        );
    }


    Rule elementar() {
        return FirstOf(
                mapEntry(),
                function(),
                map(),
                list(),
                variable(),
                constant(),
                Sequence(
                        symbol(OPEN_PARENT),
                        expression(),
                        symbol(CLOSE_PARENT)
                )
        );
    }

    Rule mapEntry() {
        return Sequence(
                variable(),
                symbol(OPEN_BRACKET),
                mandatory(
                        Sequence(
                                expression(),
                                symbol(CLOSE_BRACKET),
                                push(new MapSelection((Variable) pop(1), pop()))
                        ),
                        new ParseException("Wring map selection syntax")
                )
        );
    }

    Rule function() {
        return FirstOf(
                functionWithBrackets(),
                functionWithoutBrackets()
        );
    }

    Rule functionWithTwoWordsAsName() {
        return Sequence(
                variable(),
                variable(),
                push(new FunctionElement(popVariableName(1)+" "+popVariableName())),
                mandatory(
                        Sequence(
                                expression(),
                                ((FunctionElement) peek(1)).add(pop())
                        ),
                        new ParseException("Wrong function named with two words syntax")
                )
        );
    }

    Rule functionWithoutBrackets() {
        return Sequence(
                variable(),
                expression(),
                push(new FunctionElement(popVariableName(1), pop()))
        );
    }

    Rule functionWithBrackets() {
        return Sequence(
                variable(),
                symbol(OPEN_PARENT),
                push(new FunctionElement(popVariableName())),
                mandatory(
                        Sequence(
                                expression(),
                                ((FunctionElement) peek(1)).add(pop()),
                                ZeroOrMore(
                                        symbol(COMMA),
                                        expression(),
                                        ((FunctionElement) peek(1)).add(pop())
                                ),
                                symbol(CLOSE_PARENT)
                        ),
                        new ParseException("Wrong function syntax")
                )
        );
    }

    Rule map() {
        return Sequence(
                symbol(OPEN_CURLY_BRACKET),
                push(new ValueMap()),
                mandatory(
                        Sequence(
                                Optional(
                                        variable(),
                                        symbol(DIV),
                                        expression(),
                                        ((ValueMap) peek(2)).add(popVariableName(1), pop()),
                                        ZeroOrMore(
                                                symbol(COMMA),
                                                variable(),
                                                symbol(DIV),
                                                expression(),
                                                ((ValueMap) peek(2)).add(popVariableName(1), pop())
                                        )
                                ),
                                symbol(CLOSE_CURLY_BRACKET)
                        ),
                        new ParseException("Wrong map syntax")
                )
        );
    }

    Rule list() {
        return FirstOf(
                comprehensionList(),
                enumeratedList()
        );
    }

    Rule enumeratedList() {
        return Sequence(
                symbol(OPEN_BRACKET),
                push(new ValueList()),
                mandatory(
                        Sequence(
                                Optional(
                                        expression(),
                                        ((ValueList) peek(1)).add(pop()),
                                        ZeroOrMore(
                                                symbol(COMMA),
                                                expression(),
                                                ((ValueList) peek(1)).add(pop())
                                        )
                                ),
                                symbol(CLOSE_BRACKET)
                        ),
                        new ParseException("Wrong list syntax")
                )
        );
    }

    Rule comprehensionList() {
        return Sequence(
                constants.anyConstant(),
                basic.symbol(TWO_DOTS),
                constants.anyConstant(),
                push(ValueList.create(constants.pop(1), constants.pop())),
                basic.spacing()
        );
    }

    Rule variable() {
        return Sequence(
                basic.identifier(),
                push(new Variable(match())),
                basic.spacing()
        );
    }

    Rule constant() {
        return Sequence(
                constants.anyConstant(),
                push(constants.pop()),
                basic.spacing()
        );
    }

    Rule mandatory(Rule rule, ParseException exception) {
        return FirstOf(
                rule,
                throwException(exception)
        );
    }

    boolean throwException(ParseException exception) throws ParseBypassException {
        throw new ParseBypassException(exception);
    }

    Rule symbol(JtwigSymbol symbol) {
        return Sequence(
                basic.symbol(symbol),
                basic.spacing()
        );
    }

    Rule keywordAsVariable(JtwigKeyword keyword) {
        return Sequence(
                basic.keyword(keyword),
                push(new Variable(basic.match())),
                basic.spacing()
        );
    }

    @SuppressNode
    Rule firstOperatorOf(Operator... operators) {
        Rule[] rules = new Rule[operators.length];
        int i = 0;
        for (Operator operator : operators)
            rules[i++] = operator(operator);
        return FirstOf(rules);
    }

    Rule operator(Operator operator) {
        if (operator == MOD) {
            return Sequence(
                    basic.terminal(operator.toString()),
                    TestNot(basic.symbol(CLOSE_CURLY_BRACKET)),
                    push(new Constant<Operator>(operator)),
                    basic.spacing()
            );
        } else {
            return Sequence(
                    basic.terminal(operator.toString()),
                    push(new Constant<Operator>(operator)),
                    basic.spacing()
            );
        }
    }

    Rule binary(Rule first, Rule rest, Operator... operators) {
        return Sequence(
                first,
                push(new OperationBinary(simplify(pop()))),
                ZeroOrMore(
                        firstOperatorOf(operators),
                        ((OperationBinary) peek(1)).addOperator(((Constant<Operator>) pop()).getValue()),
                        mandatory(
                                Sequence(
                                        rest,
                                        ((OperationBinary) peek(1)).add(simplify(pop()))
                                ),
                                new ParseException("Wrong binary operation syntax")
                        )
                )
        );
    }

    Rule binary(Rule innerExpression, Operator... operators) {
        return binary(innerExpression, innerExpression, operators);
    }

    Rule unary(Rule innerRule, Operator... operators) {
        return Sequence(
                firstOperatorOf(operators),
                push(new OperationUnary(((Constant<Operator>) pop()).getValue())),
                mandatory(
                        Sequence(
                                innerRule,
                                ((OperationUnary) peek(1)).setOperand(simplify(pop()))
                        ),
                        new ParseException("Wrong unary operator syntax")
                )
        );
    }

    protected String popVariableName (int i) {
        return ((Variable) pop(i)).getIdentifier();
    }

    protected String popVariableName () {
        return popVariableName(0);
    }

    boolean popValue() {
        pop();
        return true;
    }
}
