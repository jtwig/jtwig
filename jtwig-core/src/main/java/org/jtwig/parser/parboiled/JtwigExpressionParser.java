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

package org.jtwig.parser.parboiled;

import org.jtwig.Environment;
import org.jtwig.exception.ParseBypassException;
import org.jtwig.exception.ParseException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.model.*;
import org.jtwig.parser.model.JtwigKeyword;
import org.jtwig.parser.model.JtwigSymbol;
import org.parboiled.MatcherContext;
import org.parboiled.Rule;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.matchers.CustomMatcher;
import org.parboiled.support.ValueStack;

import static org.jtwig.expressions.model.Operator.*;
import org.jtwig.loader.Loader;
import static org.jtwig.parser.model.JtwigKeyword.NULL;
import static org.jtwig.parser.model.JtwigSymbol.*;
import static org.jtwig.parser.model.JtwigSymbol.DIV;

public class JtwigExpressionParser extends JtwigBaseParser<CompilableExpression> {
    final JtwigBasicParser basic;
    final JtwigConstantParser constants;
    final Environment env;

    public JtwigExpressionParser(Loader.Resource resource, Environment env) {
        super(resource);
        basic = env.getBasicParser();
        constants = env.getConstantParser();
        this.env = env;
    }

    public Rule expression() {
        return Sequence(
                specificJtwigOperators(),
                push(pop())
        );
    }


    Rule specificJtwigOperators() {
        return binary(
                orExpression(),
                Operator.STARTS_WITH,
                Operator.ENDS_WITH,
                Operator.MATCHES,
                Operator.IN,
                Operator.NOT_IN
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
                        addition(),
                        negative()
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

    Rule negative() {
        return unary(
                addition(),
                Operator.SUB
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
                concatenation(),
                Operator.INT_DIV,
                Operator.INT_TIMES,
                Operator.TIMES,
                Operator.DIV,
                Operator.MOD
        );
    }
    
    Rule concatenation() {
        return binary(
                composition(),
                Operator.CONCATENATION
        );
    }

    Rule composition() {
        return binary(
                isOperation(),
                FirstOf(
                        functionWithBrackets(),
                        variable()
                ),
                COMPOSITION
        );
    }


    Rule isOperation() {
        return Sequence(
                selection(),
                push(new OperationBinary(currentPosition(), pop())),
                ZeroOrMore(
                        operator(IS),
                        popValue(),
                        mandatory(
                                Sequence(
                                        FirstOf(
                                                Sequence(
                                                        operator(NOT),
                                                        popValue(),
                                                        action(peek(OperationBinary.class).add(Operator.IS_NOT))
                                                ),
                                                action(peek(OperationBinary.class).add(Operator.IS))
                                        ),
                                        FirstOf(
                                                functionWithBrackets(),
                                                functionWithTwoWordsAsName(),
                                                variable(),
                                                keywordAsVariable(NULL)
                                        ),
                                        action(peek(1, OperationBinary.class).add(pop()))
                                ),
                                new ParseException("Wrong binary operation syntax")
                        )

                )
        );
    }


    Rule selection() {
        return binary(
                primary(),
                FirstOf(
                        functionWithBrackets(),
                        mapEntry(),
                        variable()
                ),
                SELECTION
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
                push(new OperationTernary(currentPosition(), pop())),
                symbol(QUESTION),
                mandatory(
                        Sequence(
                                expression(),
                                action(peek(1, OperationTernary.class).withTrueExpression(pop())),
                                symbol(DIV),
                                expression(),
                                action(peek(1, OperationTernary.class).withFalseExpression(pop()))
                        ),
                        new ParseException("Wring ternary operation syntax")
                )
        );
    }


    Rule elementar() {
        return FirstOf(
                mapEntry(),
                blockFunction(),
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
                                push(new MapSelection(currentPosition(), pop(1, Variable.class), pop()))
                        ),
                        new ParseException("Wring map selection syntax")
                )
        );
    }

    public Rule function() {
        return FirstOf(
                functionWithBrackets(),
                functionWithoutBrackets()
        );
    }

    Rule nonExpressionFunction() {
        return FirstOf(
                functionWithBrackets(),
                nonExpressionFunctionWithoutBrackets()
        );
    }

    Rule functionWithTwoWordsAsName() {
        return Sequence(
                basic.identifier(),
                push(new Constant<>(match())),
                basic.spacing(),
                basic.identifier(),
                push(new Constant<>(match())),
                basic.spacing(),
                push(new FunctionElement(currentPosition(), popVariableName(1) + " " + popVariableName())),
                mandatory(
                        Sequence(
                                expression(),
                                action(peek(1, FunctionElement.class).add(pop()))
                        ),
                        new ParseException("Wrong function named with two words syntax")
                )
        );
    }

    Rule functionWithoutBrackets() {
        return Sequence(
                basic.identifier(),
                push(new Constant<>(match())),
                basic.spacing(),
                TestNot(
                        basic.spacing(),
                        basic.terminal(SUB.toString())
                ),
                expression(),
                push(new FunctionElement(currentPosition(), popVariableName(1))),
                action(peek(FunctionElement.class).add(pop(1)))
        );
    }

    Rule nonExpressionFunctionWithoutBrackets() {
        return Sequence(
                basic.identifier(),
                push(new Constant<>(match())),
                basic.spacing(),
                TestNot(
                        basic.spacing(),
                        basic.terminal(SUB.toString())
                ),
                FirstOf(
                        Sequence(
                                expression(),
                                push(new FunctionElement(currentPosition(), popVariableName(1))) ,
                                action(peek(FunctionElement.class).add(pop(1)))
                        ),
                        push(new FunctionElement(currentPosition(), popVariableName()))
                )
        );
    }

    public Rule functionWithBrackets() {
        return Sequence(
                identifierAsString(),
                symbol(OPEN_PARENT),
                push(new FunctionElement(currentPosition(), popIdentifierAsString())),
                mandatory(
                        Sequence(
                                Optional(
                                        expression(),
                                        action(peek(1, FunctionElement.class).add(pop())),
                                        ZeroOrMore(
                                                symbol(COMMA),
                                                expression(),
                                                action((peek(1, FunctionElement.class)).add(pop()))
                                        )
                                ),
                                symbol(CLOSE_PARENT)
                        ),
                        new ParseException("Wrong function syntax")
                )
        );
    }
    
    public Rule blockFunction() {
        return Sequence(
                "block",
                basic.spacing(),
                push(new BlockFunction(currentPosition())),
                symbol(OPEN_PARENT),
                mandatory(
                        Sequence(
                                expression(),
                                action(peek(1, BlockFunction.class).add(pop())),
                                ZeroOrMore(
                                        symbol(COMMA),
                                        expression(),
                                        action((peek(1, BlockFunction.class)).add(pop()))
                                ),
                                symbol(CLOSE_PARENT)
                        ),
                        new ParseException("Invalid block function syntax")
                )
        );
    }

    Rule map() {
        return Sequence(
                symbol(OPEN_CURLY_BRACKET),
                push(new ValueMap(currentPosition())),
                mandatory(
                        Sequence(
                                Optional(
                                        FirstOf(
                                                constants.string(),
                                                identifierAsString()
                                        ),
                                        basic.spacing(),
                                        symbol(DIV),
                                        expression(),
                                        action(peek(2, ValueMap.class).add(popIdentifierAsString(1), pop())),
                                        ZeroOrMore(
                                                symbol(COMMA),
                                                FirstOf(
                                                        constants.string(),
                                                        identifierAsString()
                                                ),
                                                basic.spacing(),
                                                symbol(DIV),
                                                expression(),
                                                action(peek(2, ValueMap.class).add(popIdentifierAsString(1), pop()))
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
                push(new ValueList(currentPosition())),
                mandatory(
                        Sequence(
                                Optional(
                                        expression(),
                                        action(peek(1, ValueList.class).add(pop())),
                                        ZeroOrMore(
                                                symbol(COMMA),
                                                expression(),
                                                action(peek(1, ValueList.class).add(pop()))
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
                TestNot(InValueStack(ValueRange.class)),
                push(new ValueRange()),
                expression(),
                action(peek(1, ValueRange.class).withStart(pop(CompilableExpression.class))),
                symbol(TWO_DOTS),
                mandatory(
                        Sequence(
                                expression(),
                                action(peek(1, ValueRange.class).withEnd(pop(CompilableExpression.class))),
                                basic.spacing()
                        ),
                        new ParseException("Invalid comprehension syntax")
                )
        );
    }

    public Rule variable() {
        return Sequence(
                basic.identifier(),
                push(new Variable(currentPosition(), match())),
                basic.spacing()
        );
    }


    public Rule variableAsFunction() {
        return Sequence(
                variable(),
                push(pop(Variable.class).toFunction())
        );
    }

    Rule identifierAsString() {
        return Sequence(
                basic.identifier(),
                push(new Constant<>(match())),
                basic.spacing()
        );
    }

    String popIdentifierAsString () {
        return popIdentifierAsString(0);
    }

    String popIdentifierAsString(int position) {
        return (String) pop(position, Constant.class).as(String.class);
    }

    Rule constant() {
        return Sequence(
                constants.anyConstant(),
                push(constants.pop()),
                basic.spacing()
        );
    }

    @Override
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
                push(new Variable(currentPosition(), basic.match())),
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
        return Sequence(
                TestNot(
                        FirstOf(
                                basic.closeCode(),
                                basic.closeOutput(),
                                Sequence(
                                        basic.symbol(MINUS),
                                        basic.closeCode()
                                ),
                                Sequence(
                                        basic.symbol(MINUS),
                                        basic.closeOutput()
                                )
                        )
                ),
                basic.terminal(operator.toString()),
                conditionalSpace(operator.toString()),
                push(new Constant<>(operator)),
                basic.spacing()
        );
    }

    Rule conditionalSpace(String string) {
        if (string.matches("[a-zA-Z_$][a-zA-Z0-9_$]*"))
            return AnyOf(" \t\r\n\f");
        return Test(true);
    }

    public Rule binary(Rule first, Rule rest, Operator... operators) {
        return Sequence(
                first,
                push(new OperationBinary(currentPosition(), pop())),
                ZeroOrMore(
                        firstOperatorOf(operators),
                        TestNot(firstOperatorOf(operators)),
                        action(peek(1, OperationBinary.class).add((Operator) pop(Constant.class).getValue())),
                        mandatory(
                                Sequence(
                                        rest,
                                        action(peek(1, OperationBinary.class).add(pop()))
                                ),
                                new ParseException("Wrong binary operation syntax")
                        )
                )
        );
    }

    public Rule binary(Rule innerExpression, Operator... operators) {
        return binary(innerExpression, innerExpression, operators);
    }

    public Rule unary(Rule innerRule, Operator... operators) {
        return Sequence(
                firstOperatorOf(operators),
                push(new OperationUnary.Builder().withPosition(currentPosition()).withOperator((Operator) pop(Constant.class).getValue())),
                mandatory(
                        Sequence(
                                innerRule,
                                action(peek(1, OperationUnary.Builder.class).withOperand(pop()))
                        ),
                        new ParseException("Wrong unary operator syntax")
                ),
                push(pop(OperationUnary.Builder.class).build())
        );
    }

    protected String popVariableName (int i) {
        return (String) pop(i, Constant.class).getValue();
    }

    protected String popVariableName () {
        return popVariableName(0);
    }

    boolean popValue() {
        pop();
        return true;
    }

    public <T> T pop (Class<T> typeClass) {
        return pop(0, typeClass);
    }

    public <T> T pop(int position, Class<T> type) {
        return type.cast(pop(position));
    }
    
    public Rule InValueStack(Class<?> cls) {
        return InValueStack(cls, 0);
    }
    public Rule InValueStack(Class<?> cls, int start) {
        return InValueStack(cls, start, start+1);
    }
    public Rule InValueStack(Class<?> cls, int start, int end) {
        return new InValueStack(Ch('*'), cls, start, end);
    }
    
    
    public static class InValueStack extends CustomMatcher {
        private final Class<?> cls;
        private final int start;
        private final int end;
        public InValueStack(Rule rule, Class<?> cls, int start, int end) {
            super(rule, "a");
            this.cls = cls;
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean isSingleCharMatcher() {
            return false;
        }

        @Override
        public boolean canMatchEmpty() {
            return true;
        }

        @Override
        public boolean isStarterChar(char c) {
            return false;
        }

        @Override
        public char getStarterChar() { return '^'; }

        @Override
        public <V> boolean match(MatcherContext<V> context) {
            ValueStack vs = context.getValueStack();
            for (int i = start; i < end; i++) {
                if (vs.size() > i && cls.isAssignableFrom(vs.peek(i).getClass())) {
                    return true;
                }
            }
            return false;
        }
        
    }
}
