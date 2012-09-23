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

import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.DontLabel;
import org.parboiled.annotations.MemoMismatches;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.support.ParsingResult;

import com.lyncode.jtwig.exceptions.JtwigParsingException;
import com.lyncode.jtwig.tree.JtwigBlock;
import com.lyncode.jtwig.tree.JtwigConstant;
import com.lyncode.jtwig.tree.JtwigContent;
import com.lyncode.jtwig.tree.JtwigElement;
import com.lyncode.jtwig.tree.JtwigElse;
import com.lyncode.jtwig.tree.JtwigExpression;
import com.lyncode.jtwig.tree.JtwigExtends;
import com.lyncode.jtwig.tree.JtwigFilter;
import com.lyncode.jtwig.tree.JtwigFor;
import com.lyncode.jtwig.tree.JtwigFunction;
import com.lyncode.jtwig.tree.JtwigIf;
import com.lyncode.jtwig.tree.JtwigInclude;
import com.lyncode.jtwig.tree.JtwigRoot;
import com.lyncode.jtwig.tree.JtwigText;
import com.lyncode.jtwig.tree.JtwigValue;
import com.lyncode.jtwig.tree.JtwigVariable;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class JtwigParser extends BaseParser<JtwigElement> {
	private static JtwigParser parser = null;
	
    public static JtwigRoot parse (String input) throws JtwigParsingException {
    	if (parser == null)
    		parser = Parboiled.createParser(JtwigParser.class);
        // String input = "{{ 'aaaa' | trans 'asdasd' | make | aaa }}            ";
        // String input = "<div asdda><asd> {% hello 'boy' %} <asdasd>";

        //ParsingResult<JtwigElement> result = new TracingParseRunner<JtwigElement>(parser.JtwigExpression()).run(input);
        ParsingResult<JtwigElement> result = new BasicParseRunner<JtwigElement>(parser.JtwigContentRoot()).run(input);
        
        JtwigElement e = result.valueStack.pop();
        if (e instanceof JtwigRoot)
        	return (JtwigRoot) e;
        else
        	throw new JtwigParsingException("A parsing error occurs");
    }
	
	Rule JtwigContentRoot () {
		return Sequence(
			push(new JtwigRoot()),
			ZeroOrMore(
					Sequence(
						FirstOf(
								JtwigExtendsRule(),
								JtwigTextRule(),
								JtwigFunctionRule(),
								JtwigExpressionRule(),
								JtwigIfRule(),
								JtwigForRule(),
								JtwigBlockRule(),
								JtwigIncludeRule()
						),
						((JtwigContent) peek(1)).add((JtwigElement)pop())
					)
			),
			EOI
		);
	}
	
	Rule JtwigContentRule() {
		return ZeroOrMore(
				Sequence(
					FirstOf(
							JtwigTextRule(),
							JtwigFunctionRule(),
							JtwigExpressionRule(),
							JtwigIfRule(),
							JtwigForRule(),
							JtwigIncludeRule()
					),
					((JtwigContent) peek(1)).add((JtwigElement)pop())
				)
		);
	}
	
	Rule JtwigIfRule () {
		return Sequence(
				LWING,
				PERCENT,
				Keyword(IF),
				JtwigValue(),
				push(new JtwigIf((JtwigValue)pop())),
				PERCENT,
				RWINGFINAL,
				JtwigContentRule(),
				Optional(
						Sequence(
								LWING,
								PERCENT,
								Keyword(ELSE),
								PERCENT,
								RWINGFINAL,
								push(new JtwigElse()),
								JtwigContentRule(),
								((JtwigIf) peek(1)).setElse((JtwigElse)pop())
						)
				),
				LWING,
				PERCENT,
				Keyword(ENDIF),
				PERCENT,
				RWINGFINAL
		);
	}
	
	Rule JtwigForRule () {
		return Sequence(
				LWING,
				PERCENT,
				Keyword(FOR),
				Identifier(),
				push(new JtwigFor(match())),
				Keyword(IN),
				JtwigVariable(),
				((JtwigFor) peek()).setContainer((JtwigVariable)pop()), 
				PERCENT,
				RWINGFINAL,
				JtwigContentRule(),
				LWING,
				PERCENT,
				Keyword(ENDBLOCK),
				PERCENT,
				RWINGFINAL
		);
	}
	
	Rule JtwigBlockRule () {
		return Sequence(
				LWING,
				PERCENT,
				Keyword(BLOCK),
				Identifier(),
				push(new JtwigBlock(match())),
				PERCENT,
				RWINGFINAL,
				JtwigContentRule(),
				LWING,
				PERCENT,
				Keyword(ENDBLOCK),
				PERCENT,
				RWINGFINAL
		);
	}
	
	Rule JtwigTextRule () {
		return Sequence(
				textRule(), 
				push(new JtwigText(match()))
		);
	}
	
	Rule textRule () {
		return OneOrMore(
                        FirstOf(
                                Escape(),
                                Sequence(TestNot(LWING), ANY)
                        )
                ).suppressSubnodes();
	}
	
	Rule JtwigExpressionRule () {
		return Sequence(
				LWING, LWING,
				JtwigValue(),
				push(new JtwigExpression((JtwigValue) pop())),
				ZeroOrMore(OR, Sequence(JtwigFilterRule(), ((JtwigExpression) peek(1)).add((JtwigFilter) pop()))),
				RWING,
				RWINGFINAL
		);
	}
	
	Rule JtwigVariable () {
		return Sequence(
				QualifiedIdentifier(),
				push(new JtwigVariable(match()))
		);
	}
	
	Rule JtwigStringValue () {
		return Sequence(
				StringLiteral(),
				push(new JtwigConstant<String>(match()))
		);
	}
	
	Rule JtwigIntegerValue () {
		return Sequence(
				OneOrMore(Digit()),
				push(new JtwigConstant<Integer>(Integer.parseInt(match())))
		);
	}
	
	Rule JtwigBooleanValue () {
		return Sequence(
				FirstOf(Keyword(TRUE), Keyword(FALSE)),
				push(new JtwigConstant<Boolean>(match().equals(TRUE)))
		);
	}
	
	Rule JtwigValue () {
		return FirstOf(
				JtwigVariable(),
				JtwigStringValue(),
				JtwigIntegerValue(),
				JtwigBooleanValue()
		);
	}
	
	Rule JtwigIncludeRule () {
		return Sequence (
			LWING,
			PERCENT,
			Keyword(INCLUDE),
			StringLiteral(),
			push(new JtwigInclude(match())),
			PERCENT,
			RWINGFINAL
		);
	}
	
	Rule JtwigExtendsRule () {
		return Sequence (
			LWING,
			PERCENT,
			Keyword(EXTENDS),
			StringLiteral(),
			push(new JtwigExtends(match())),
			PERCENT,
			RWINGFINAL
		);
	}

	Rule JtwigFunctionRule () {
		return Sequence (
			LWING,
			PERCENT,
			Identifier(),
			push(new JtwigFunction(match())),
			ZeroOrMore(Sequence(JtwigValue(), ((JtwigFunction) peek(1)).add((JtwigValue) pop()))),
			PERCENT,
			RWINGFINAL
		);
	}
	
	
	Rule JtwigFilterRule () {
		return Sequence (
			Identifier(),
			push(new JtwigFilter(match())),
			ZeroOrMore(Sequence(JtwigValue(), ((JtwigFilter) peek(1)).add((JtwigValue) pop())))
		);
	}
	
	Rule StringOrIdentifier () {
		return FirstOf(
				StringLiteral(), 
				QualifiedIdentifier()
		);
	} 
	
	Rule StringLiteral() {
        return FirstOf(Sequence(
                '"',
                ZeroOrMore(
                        FirstOf(
                                Escape(),
                                Sequence(TestNot(AnyOf("\r\n\"\\")), ANY)
                        )
                ).suppressSubnodes(),
                '"', Spacing()),
                Sequence(
                        "'",
                        ZeroOrMore(
                                FirstOf(
                                        Escape(),
                                        Sequence(TestNot(AnyOf("\r\n'\\")), ANY)
                                )
                        ).suppressSubnodes(),
               "'", Spacing())
        );
    }

    Rule Escape() {
        return Sequence('\\', FirstOf(AnyOf("btnfr\"\'\\"), OctalEscape(), UnicodeEscape()));
    }

    Rule OctalEscape() {
        return FirstOf(
                Sequence(CharRange('0', '3'), CharRange('0', '7'), CharRange('0', '7')),
                Sequence(CharRange('0', '7'), CharRange('0', '7')),
                CharRange('0', '7')
        );
    }

    Rule UnicodeEscape() {
        return Sequence(OneOrMore('u'), HexDigit(), HexDigit(), HexDigit(), HexDigit());
    }
    
    Rule HexDigit() {
        return FirstOf(CharRange('a', 'f'), CharRange('A', 'F'), CharRange('0', '9'));
    }
    
    Rule Digit() {
        return CharRange('0', '9');
    }

	Rule QualifiedIdentifier() {
        return Sequence(Identifier(), ZeroOrMore(DOT, Identifier()));
    }

	@SuppressSubnodes
    @MemoMismatches
    Rule Identifier() {
        return Sequence(TestNot(Keyword()), Letter(), ZeroOrMore(LetterOrDigit()), Spacing());
    }

	
	@SuppressNode
    @DontLabel
    Rule Keyword(String keyword) {
        return Terminal(keyword, LetterOrDigit());
    }
	
	@MemoMismatches
    Rule Keyword() {
        return Sequence(
                FirstOf("true", "false", "for",  "endfor", "if" , "elseif", "else", "endif", "block", "endblock", "include", "extends", "in"),
                TestNot(LetterOrDigit())
        );
    }
	
	Rule Letter() {
        // switch to this "reduced" character space version for a ~10% parser performance speedup
        return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), '_', '$');
    }

    @MemoMismatches
    Rule LetterOrDigit() {
        return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), CharRange('0', '9'), '_', '$');
    }
	
	@SuppressNode
    Rule Spacing() {
        return ZeroOrMore(FirstOf(

                // whitespace
                OneOrMore(AnyOf(" \t\r\n\f").label("Whitespace")),

                // traditional comment
                Sequence("/*", ZeroOrMore(TestNot("*/"), ANY), "*/"),

                // end of line comment
                Sequence(
                        "//",
                        ZeroOrMore(TestNot(AnyOf("\r\n")), ANY),
                        FirstOf("\r\n", '\r', '\n', EOI)
                )
        ));
    }
	
	@SuppressNode
    @DontLabel
    Rule Terminal(String string) {
        return Sequence(string, Spacing()).label('\'' + string + '\'');
    }

	@SuppressNode
    @DontLabel
    Rule Terminal(String string, boolean spacing) {
		if (spacing)
			return Sequence(string, Spacing()).label('\'' + string + '\'');
		else
			return String(string).label('\'' + string + '\'');
    }
	
    @SuppressNode
    @DontLabel
    Rule Terminal(String string, Rule mustNotFollow) {
        return Sequence(string, TestNot(mustNotFollow), Spacing()).label('\'' + string + '\'');
    }
	
    final String TRUE = "true";
    final String FALSE = "false";
    final String IF = "if";
    final String ELSE = "else";
    final String ENDIF = "endif";
    final String BLOCK = "block";
    final String ENDBLOCK = "endblock";
    final String FOR = "for";
    final String ENDFOR = "endfor";
    final String IN = "in";
    final String INCLUDE = "include";
    final String EXTENDS = "extends";

    final Rule RWING = Terminal("}");
    final Rule RWINGFINAL = Terminal("}", false);
    final Rule LWING = Terminal("{");
    final Rule OR = Terminal("|");
    final Rule DOT = Terminal(".");
    final Rule PERCENT = Terminal("%");
}
