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
import org.jtwig.parser.model.JtwigKeyword;
import org.jtwig.parser.model.JtwigSymbol;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.DontLabel;
import org.parboiled.annotations.MemoMismatches;
import org.parboiled.annotations.SuppressNode;

import static org.jtwig.parser.model.JtwigSymbol.QUOTE;

public class JtwigBasicParser extends BaseParser<String> {
    final Environment env;

    public JtwigBasicParser(Environment env) {
        this.env = env;
    }

    @SuppressNode
    public Rule spacing() {
        return ZeroOrMore(FirstOf(
                // whitespace
                OneOrMore(AnyOf(" \t\r\n\f").label("Whitespace")),
                // traditional comment
                Sequence(
                        openComment(),
                        ZeroOrMore(TestNot(closeComment()), ANY),
                        closeComment()
                ).label("Comment")
        ));
    }

    public Rule closeCode() {
        return String(env.getConfiguration().getSymbols().endTag());
    }

    public Rule openCode() {
        return String(env.getConfiguration().getSymbols().beginTag());
    }

    public Rule openOutput() {
        return String(env.getConfiguration().getSymbols().beginOutput());
    }

    public Rule closeOutput() {
        return String(env.getConfiguration().getSymbols().endOutput());
    }

    public Rule openComment() {
        return String(env.getConfiguration().getSymbols().beginComment());
    }

    public Rule closeComment() {
        return String(env.getConfiguration().getSymbols().endComment());
    }

    @MemoMismatches
    public Rule anyKeyword() {
        return Sequence(
                FirstOf(JtwigKeyword.keywords()),
                TestNot(letterOrDigit())
        );
    }

    public Rule identifier() {
        return Sequence(
                TestNot(anyKeyword()),
                letter(),
                ZeroOrMore(letterOrDigit())
        );
    }

    @SuppressNode
    public Rule keyword(JtwigKeyword keyword) {
        return terminal(keyword.getKeyword(), letterOrDigit());
    }
    @SuppressNode
    public Rule keyword(String keyword) {
        return terminal(keyword, letterOrDigit());
    }

    @SuppressNode
    public Rule symbol(JtwigSymbol symbol) {
        return terminal(symbol.getSymbol());
    }

    @SuppressNode
    @DontLabel
    Rule terminal(String string, Rule mustNotFollow) {
        return Sequence(string, TestNot(mustNotFollow)).label('\'' + string + '\'');
    }

    @SuppressNode
    @DontLabel
    Rule terminal(String string) {
        return String(string).label('\'' + string + '\'');
    }

    public Rule onlyOneChar() {
        return Sequence(
                symbol(QUOTE),
                character(),
                push(match()),
                symbol(QUOTE)
        );
    }

    Rule character() {
        return FirstOf(
                CharRange('a', 'z'),
                CharRange('A', 'Z')
        );
    }

    Rule letter() {
        // switch to this "reduced" character space version for a ~10% parse performance speedup
        return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), '_', '$');
    }

    @MemoMismatches
    Rule letterOrDigit() {
        return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), CharRange('0', '9'), '_', '$');
    }

    StringBuilder stringBuilder;

    /*
     * Pushes the String (without quotes)
     */
    public Rule stringLiteral() {
        return FirstOf(
                Sequence(
                        '"',
                        startBuilder(),
                        ZeroOrMore(
                                FirstOf(
                                        escape("\""),
                                        Sequence(
                                                Sequence(TestNot(AnyOf("\r\n\"\\")), ANY),
                                                append(match())
                                        )
                                )
                        ).suppressSubnodes(),
                        push(stringBuilder.toString()),
                        '"'
                ),
                Sequence(
                        "'",
                        startBuilder(),
                        ZeroOrMore(
                                FirstOf(
                                        escape("'"),
                                        Sequence(
                                                Sequence(TestNot(AnyOf("\r\n'\\")), ANY),
                                                append(match())
                                        )
                                )
                        ).suppressSubnodes(),
                        push(stringBuilder.toString()),
                        "'"
                )
        );
    }

    boolean startBuilder() {
        stringBuilder = new StringBuilder();
        return true;
    }

    boolean append(String text) {
        stringBuilder.append(text);
        return true;
    }

    Rule escape(String value) {
        return FirstOf(
                Sequence(toRule('\\'), value, append(match())),
                Sequence(
                        Sequence('\\', FirstOf(AnyOf("btnfr\"\'\\"), octalEscape(), unicodeEscape())),
                        append(match())
                )
        );
    }

    Rule escape() {
        return Sequence('\\', FirstOf(AnyOf("btnfr\"\'\\"), octalEscape(), unicodeEscape()));
    }

    Rule octalEscape() {
        return FirstOf(
                Sequence(CharRange('0', '3'), CharRange('0', '7'), CharRange('0', '7')),
                Sequence(CharRange('0', '7'), CharRange('0', '7')),
                CharRange('0', '7')
        );
    }

    Rule unicodeEscape() {
        return Sequence(OneOrMore('u'), hexDigit(), hexDigit(), hexDigit(), hexDigit());
    }

    Rule hexDigit() {
        return FirstOf(CharRange('a', 'f'), CharRange('A', 'F'), CharRange('0', '9'));
    }

    Rule digit() {
        return CharRange('0', '9');
    }
}
