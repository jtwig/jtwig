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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.jtwig.Environment;
import org.jtwig.exception.ParseBypassException;
import org.jtwig.exception.ParseException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.model.Constant;
import org.jtwig.expressions.model.MapSelection;
import org.jtwig.expressions.model.OperationBinary;
import org.jtwig.expressions.model.OperationTernary;
import org.jtwig.expressions.model.OperationUnary;
import org.jtwig.expressions.model.ValueList;
import org.jtwig.expressions.model.ValueMap;
import org.jtwig.expressions.model.Variable;
import org.jtwig.extension.api.operator.BinaryOperator;
import org.jtwig.extension.api.operator.Operator;
import org.jtwig.extension.api.operator.UnaryOperator;
import org.jtwig.extension.model.Callable;
import org.jtwig.extension.model.FunctionCall;
import org.jtwig.loader.Loader;
import org.jtwig.parser.model.JtwigKeyword;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.parser.model.JtwigSymbol;
import static org.jtwig.parser.model.JtwigSymbol.CLOSE_BRACKET;
import static org.jtwig.parser.model.JtwigSymbol.CLOSE_CURLY_BRACKET;
import static org.jtwig.parser.model.JtwigSymbol.CLOSE_PARENT;
import static org.jtwig.parser.model.JtwigSymbol.COMMA;
import static org.jtwig.parser.model.JtwigSymbol.DIV;
import static org.jtwig.parser.model.JtwigSymbol.MINUS;
import static org.jtwig.parser.model.JtwigSymbol.OPEN_BRACKET;
import static org.jtwig.parser.model.JtwigSymbol.OPEN_CURLY_BRACKET;
import static org.jtwig.parser.model.JtwigSymbol.OPEN_PARENT;
import static org.jtwig.parser.model.JtwigSymbol.QUESTION;
import org.parboiled.MatcherContext;
import org.parboiled.Rule;
import org.parboiled.annotations.Label;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.matchers.CustomMatcher;
import org.parboiled.support.ValueStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JtwigExpressionParser extends JtwigBaseParser<CompilableExpression> {
    static final Logger LOGGER = LoggerFactory.getLogger(JtwigExpressionParser.class);
    final JtwigBasicParser basic;
    final JtwigConstantParser constants;
    final Environment env;

    public JtwigExpressionParser(Loader.Resource resource, Environment env) {
        super(resource);
        basic = env.getBasicParser();
        constants = env.getConstantParser();
        this.env = env;
    }
    
    @Label("Expression")
    public Rule expression() {
        return Sequence(
                operators(),
                push(pop())
        );
    }
    
    public Rule operators() {
        return binary(
                operatorsHierarchy(),
                tests()
        );
    }
    
    Rule operatorsHierarchy() {
        List<Operator> operators = new ArrayList<>(env.getConfiguration().getExtensions().getBinaryOperators().values());
        operators.addAll(env.getConfiguration().getExtensions().getUnaryOperators().values());
        Collections.sort(operators, Collections.reverseOrder());
        
        Rule previous = primary();
        for (Operator op : operators) {
            if (op instanceof UnaryOperator) {
                previous = unary(
                        previous,
                        op.getName()
                );
            } else if (op instanceof BinaryOperator) {
                previous = binary(
                        previous,
                        ObjectUtils.defaultIfNull(((BinaryOperator)op).getRightSideRule(this, env), previous),
                        op.getName()
                );
            } else {
                throw new UnsupportedOperationException("Operations of types other than unary and binary are not yet supported. "+op.getClass().getName()+" given.");
            }
        }
        return previous;
    }

    String[] tests() {
        return env.getConfiguration().getExtensions().getTests().keySet().toArray(new String[0]);
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
                        new ParseException("Wrong ternary operation syntax")
                )
        );
    }

    Rule elementar() {
        return FirstOf(
                mapEntry(),
                function(),
                map(),
                list(),
                Sequence(
                        constants.booleanValue(),
                        push(constants.pop()),
                        basic.spacing()
                ),
                variable(),
                constant(),
                Sequence(
                        symbol(OPEN_PARENT),
                        expression(),
                        symbol(CLOSE_PARENT)
                )
        );
    }

    public Rule mapEntry() {
        return Sequence(
                variable(),
                symbol(OPEN_BRACKET),
                mandatory(
                        Sequence(
                                expression(),
                                symbol(CLOSE_BRACKET),
                                push(new MapSelection(currentPosition(), pop(1), pop()))
                        ),
                        new ParseException("Wrong map selection syntax")
                )
        );
    }

    public Rule function() {
        return callableWithBrackets(FunctionCall.class);
    }
    
    public Rule callable(final Class<? extends Callable> model) {
        return callable(model, identifierAsString());
    }
    public Rule callable(final Class<? extends Callable> model,
            final Rule identifierRule) {
        return FirstOf(
                callableWithBrackets(model, identifierRule),
                callableWithoutBrackets(model, identifierRule)
        );
    }
    public Rule callable(final Class<? extends Callable> model,
            final String[] identifiers) {
        return callable(model, Sequence(
                FirstOf(identifiers),
                push(new Constant<>(match())),
                basic.spacing()
        ));
    }
    public Rule callableWithBrackets(final Class<? extends Callable> model) {
        return callableWithBrackets(model, identifierAsString());
    }
    public Rule callableWithBrackets(final Class<? extends Callable> model,
            final Rule identifierRule) {
        return Sequence(
                identifierRule,
                symbol(OPEN_PARENT),
                push(createCallableModel(model)),
                mandatory(
                        Sequence(
                                Optional(
                                        expression(),
                                        action(peek(1, Callable.class).add(pop())),
                                        ZeroOrMore(
                                                symbol(COMMA),
                                                expression(),
                                                action((peek(1, Callable.class)).add(pop()))
                                        )
                                ),
                                symbol(CLOSE_PARENT)
                        ),
                        new ParseException("Wrong function syntax")
                )
        );
    }
    public Rule callableWithoutBrackets(final Class<? extends Callable> model) {
        return callableWithoutBrackets(model, identifierAsString());
    }
    public Rule callableWithoutBrackets(final Class<? extends Callable> model,
            final Rule identifierRule) {
        return Sequence(
                identifierRule,
                push(createCallableModel(model)),
                basic.spacing()
        );
    }
    Callable createCallableModel(final Class<? extends Callable> model) {
        try {
            return model.getConstructor(JtwigPosition.class, String.class).newInstance(currentPosition(), popIdentifierAsString());
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new ParseBypassException(new ParseException(ex));
        }
    }

    public Rule map() {
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
        return enumeratedList();
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

    public Rule variable() {
        return Sequence(
                basic.identifier(),
                push(new Variable(currentPosition(), match())),
                basic.spacing()
        );
    }

    public Rule identifierAsString() {
        return Sequence(
                basic.identifier(),
                push(new Constant<>(match())),
                basic.spacing()
        );
    }

    public String popIdentifierAsString () {
        return popIdentifierAsString(0);
    }

    public String popIdentifierAsString(int position) {
        return (String) pop(position, Constant.class).as(String.class);
    }

    public Rule constant() {
        return Sequence(
                constants.anyConstant(),
                push(constants.pop()),
                basic.spacing()
        );
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
    Rule firstOperatorOf(String... operators) {
        if (operators == null || operators.length == 0) {
            return EMPTY;
        }
        Rule[] rules = new Rule[operators.length];
        int i = 0;
        for (String operator : operators)
            rules[i++] = operator(operator);
        return FirstOf(rules);
    }

    Rule operator(String operator) {
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
                basic.terminal(operator),
                conditionalSpace(operator),
                push(new Constant<>(operator)),
                basic.spacing()
        );
    }

    Rule conditionalSpace(String string) {
        if (string.matches("[a-zA-Z_$][a-zA-Z0-9_$]*"))
            return AnyOf(" \t\r\n\f");
        return Test(true);
    }

    public Rule binary(Rule first, Rule rest, String... operators) {
        return Sequence(
                first,
                push(new OperationBinary(currentPosition(), pop())),
                ZeroOrMore(
                        firstOperatorOf(operators),
                        TestNot(firstOperatorOf(operators)),
                        action(peek(1, OperationBinary.class).addOperator(pop(Constant.class).as(String.class).toString())),
                        mandatory(
                                Sequence(
                                        rest,
                                        action(peek(1, OperationBinary.class).addOperand(pop()))
                                ),
                                new ParseException("Wrong binary operation syntax")
                        )
                )
        );
    }

    public Rule binary(Rule innerExpression, String... operators) {
        return binary(innerExpression, innerExpression, operators);
    }

    public Rule unary(Rule innerRule, String operator) {
        return FirstOf(
                Sequence(
                        firstOperatorOf(operator),
                        push(new OperationUnary(currentPosition(), pop(Constant.class).as(String.class).toString())),
                        mandatory(
                                Sequence(
                                        innerRule,
                                        action(peek(1, OperationUnary.class).withOperand(pop()))
                                ),
                                new ParseException("Wrong unary operator syntax")
                        )
                ),
                innerRule
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
