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

import com.lyncode.jtwig.elements.Block;
import com.lyncode.jtwig.elements.Extends;
import com.lyncode.jtwig.elements.FastExpression;
import com.lyncode.jtwig.elements.For;
import com.lyncode.jtwig.elements.Function;
import com.lyncode.jtwig.elements.If;
import com.lyncode.jtwig.elements.Include;
import com.lyncode.jtwig.elements.ObjectList;
import com.lyncode.jtwig.elements.ObjectMap;
import com.lyncode.jtwig.elements.Variable;
import com.lyncode.jtwig.exceptions.JtwigParsingException;

/**
 * 
 * @author "João Melo <jmelo@lyncode.com>"
 */
public class JtwigExtendedParser extends BaseParser<Object> {
	private static JtwigExtendedParser parser = null;
	
    @Override
	public Object pop(int down) {
    	System.out.println("Before Pop Stack Size: " + this.getContext().getValueStack().size());
    	Object e = super.pop(down);
    	System.out.println("Poping at "+down+": "+e.toString());
    	System.out.println("Stack Size: " + this.getContext().getValueStack().size());
		return e;
	}

	@Override
	public boolean push(Object value) {
		System.out.println("Before Push Stack Size: " + this.getContext().getValueStack().size());
    	boolean e = super.push(value);
    	System.out.println("Pushing: "+value.toString());
    	System.out.println("Stack Size: " + this.getContext().getValueStack().size());
		return e;
	}

	@Override
	public Object pop() {
		System.out.println("Before Pop Stack Size: " + this.getContext().getValueStack().size());
		Object e = super.pop();
    	System.out.println("Poping: "+e.toString());
    	System.out.println("Stack Size: " + this.getContext().getValueStack().size());
		return e;
	}

	public static ObjectList parse (String input) throws JtwigParsingException {
    	if (parser == null)
    		parser = Parboiled.createParser(JtwigExtendedParser.class);
        // String input = "{{ 'aaaa' | trans 'asdasd' | make | aaa }}            ";
        // String input = "<div asdda><asd> {% hello 'boy' %} <asdasd>";

        //ParsingResult<JtwigElement> result = new TracingParseRunner<JtwigElement>(parser.JtwigContentRoot()).run(input);
        ParsingResult<Object> result = new BasicParseRunner<Object>(parser.Start()).run(input);
        
        Object e = result.valueStack.pop();
        if (e instanceof ObjectList)
        	return (ObjectList) e;
        else
        	throw new JtwigParsingException("A parsing error occurs");
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

    final Rule LPARENT = Terminal("(");
    final Rule RPARENT = Terminal(")");
    final Rule OR = Terminal("|");
    final Rule DOT = Terminal(".");
    final Rule COMMA = Terminal(",");
    final Rule DIV = Terminal(":");
    final Rule OFAST = Terminal("{{");
    final Rule CFAST = Terminal("}}", false);
    final Rule LISTOPEN = Terminal("[");
    final Rule LISTCLOSE = Terminal("]");
    final Rule MAPOPEN = Terminal("{");
    final Rule MAPCLOSE = Terminal("}");
    final Rule CODEOPEN = Terminal("{%");
    final Rule CODECLOSE = Terminal("%}", false);
    
    Rule Start () {
    	return FirstOf(
    			ExtendingTemplate(),
    			NormalTemplate()
    	);
    }
    
    /**
     * Pushes one ObjectList
     * 
     * @return
     */
    Rule NormalTemplate () {
    	return Sequence(
    			Spacing(),
    			push(new ObjectList()),
    			Content(true),
    			EOI
    	);
    }
    
    /**
     * Pushes one ObjectList
     * 
     * @return
     */
    Rule ExtendingTemplate () {
    	return Sequence (
    			Spacing(),
    			ExtendsExpression(),
    			push(new ObjectList()),
    			((ObjectList) peek()).add(pop(1)),
    			ZeroOrMore(
    					Spacing(),
    					BlockExpression(),
    	    			((ObjectList) peek(1)).add(pop())
    			),
    			EOI
    	);
    }
    
    /**
     * Pushes a Block
     * 
     * @return
     */
    Rule BlockExpression () {
    	return Sequence(
    		CODEOPEN,
    		Keyword(BLOCK),
    		Identifier(),
    		push(new Block((String)pop())),
    		CODECLOSE,
    		Content(false),
    		CODEOPEN,
    		Keyword(ENDBLOCK),
    		CODECLOSE
    	);
    }
    
    /**
     * Pushes an Extends
     * 
     * @return
     */
    Rule ExtendsExpression () {
    	return Sequence(
    			CODEOPEN,
    			Keyword(EXTENDS),
    			StringLiteral(),
    			push(new Extends((String) pop())),
    			CODECLOSE
    	);
    }
    
    /**
     * Pushes an Include
     * 
     * @return
     */
    Rule IncludeExpression () {
    	return Sequence(
    			CODEOPEN,
    			Keyword(INCLUDE),
    			StringLiteral(),
    			push(new Include((String)pop())),
    			CODECLOSE
    	);
    }
    
    /**
     * No Pushes!!
     * 
     * @return
     */
    Rule Content (boolean allowBlocks) {
    	if (allowBlocks) {
	    	return ZeroOrMore(
	    			FirstOf(
	    					FastExpression(),
	    					ForExpression(allowBlocks),
	    					IfExpression(allowBlocks),
	    					BlockExpression(),
	    					IncludeExpression(),
	    					TextRule()
	    			),
	    			((ObjectList)peek(1)).add(pop())
	    	);
    	} else {
	    	return ZeroOrMore(
	    			FirstOf(
	    					TextRule(),
	    					FastExpression(),
	    					ForExpression(allowBlocks),
	    					IfExpression(allowBlocks),
	    					IncludeExpression()
	    			),
	    			((ObjectList)peek(1)).add(pop())
	    	);
    	}
    }

    /**
     * Pushes the text
     * 
     * @return
     */
	Rule TextRule () {
		return Sequence(
				OneOrMore(
                        FirstOf(
                                Escape(),
                                Sequence(TestNot(FirstOf(OFAST, CODEOPEN)), ANY)
                        )
				).suppressSubnodes(),
				push(match())
		);
	}
	
	/**
	 * Pushes on value
	 * 
	 * @return
	 */
	Rule BooleanExpression () {
		return FirstOf(
			Function(),
			Variable(),
			Boolean()
		);
	}
	
	/**
	 * Pushes one IF
	 * 
	 * @return
	 */
	Rule IfExpression (boolean allowblocks) {
		return Sequence(
				CODEOPEN,
				Keyword(IF),
				BooleanExpression(),
				push(new If(pop())),
				CODECLOSE,
				Content(allowblocks),
				Optional(
						Sequence(
							CODEOPEN,
							Keyword(ELSE),
							CODECLOSE,
							push(new ObjectList()),
							Content(allowblocks),
							((If)peek(1)).setElse((ObjectList)pop())
						)
				),
				CODEOPEN,
				Keyword(ENDIF),
				CODECLOSE
		);
	}
	
    /***
     * Pushes a For
     * 
     * @return
     */
    Rule ForExpression (boolean allowblocks) {
    	return Sequence(
    		CODEOPEN,
    		Keyword(FOR),
    		Identifier(),
    		Keyword(IN),
    		FirstOf(
    				ListExpression(),
    				Variable(),
    				Function()
    		),
    		CODECLOSE,
    		push(new For((String)pop(1), pop())),
    		Content(allowblocks),
    		CODEOPEN,
    		Keyword(ENDFOR),
    		CODECLOSE
    	);
    }
    
    /**
     * Pushes one fast expression
     * 
     * @return
     */
    Rule FastExpression () {
    	return Sequence(
    			OFAST,
    			Value(),
    			push(new FastExpression(pop())),
    			ZeroOrMore(Sequence(
    					OR,
    					Function(),
    					((FastExpression) peek(1)).add((Function)pop())
    			)),
    			CFAST
    	);
    }
    
    /**
     * Pushes one value (function or variable or constant)
     * 
     * @return
     */
    Rule Value () {
    	return FirstOf(
    			Integer(),
    			Boolean(),
    			StringLiteral(),
    			Variable(),
    			Function(),
    			ListExpression(),
    			MapExpression()
    	);
    }
    
    /**
     * No pushes!
     * 
     * @return
     */
    Rule FunctionArguments () {
    	return ZeroOrMore(
    			Sequence(
    					FirstOf(
    							Integer(),
    							Boolean(),
    							StringLiteral(),
    							Sequence(LPARENT, Function(), RPARENT),
    							ListExpression(),
    							MapExpression(),
    							Variable()
    					),
    					ACTION(((Function)peek(1)).add(pop()))
    			)
    	);
    }
    
    /**
     * Pushes a Function
     * 
     * @return
     */
    Rule Function () {
    	return Sequence(
    			Identifier(),
    			push(new Function((String) pop())),
    			FunctionArguments()
    	);
    }
    
    /**
     * Pushes a MAP
     * 
     * @return
     */
    Rule MapExpression () {
    	return Sequence(
    			MAPOPEN,
    			push(new ObjectMap()),
    			Optional(
    					Identifier(),
    					DIV,
    					Value(),
    					((ObjectMap)peek(2)).add((String)pop(1), pop()),
    					ZeroOrMore(
    							COMMA,
    	    					Identifier(),
    	    					DIV,
    	    					Value(),
    	    					((ObjectMap)peek(2)).add((String)pop(1), pop())
    					)
    			),
    			MAPCLOSE
    	);
    }
    
    /**
     * Pushes a list
     * 
     * @return
     */
	Rule ListExpression () {
    	return Sequence(
    			LISTOPEN,
    			push(new ObjectList()),
    			Optional(
    					Value(),
    					((ObjectList)peek(1)).add(pop()),
    					ZeroOrMore(
    							COMMA,
    	    					Value(),
    	    					((ObjectList)peek(1)).add(pop())
    					)
    			),
    			LISTCLOSE
    	);
    }
    
    /**
     * Pushes a boolean
     * 
     * @return
     */
    Rule Boolean () {
    	return FirstOf(
    			Sequence(TRUE, push(new Boolean(true))),
    			Sequence(FALSE, push(new Boolean(false)))
    	);
    }
    
    /**
     * Pushes the integer (as integer)
     * 
     * @return
     */
    Rule Integer () {
    	return Sequence(
    			OneOrMore(Digit()),
    			push(Integer.parseInt(match()))
    	);
    }
    
    /**
     * Pushes the variable (String)
     * 
     * @return
     */
    Rule Variable () {
    	return Sequence(
    			QualifiedIdentifier(),
    			push(new Variable((String) pop()))
    	);
    }
    
    /**
     * Pushes the String (without quotes)
     * 
     * @return
     */
    Rule StringLiteral() {
        return FirstOf(
	        		Sequence(
			                '"',
			                ZeroOrMore(
			                        FirstOf(
			                                Escape(),
			                                Sequence(TestNot(AnyOf("\r\n\"\\")), ANY)
			                        )
			                ).suppressSubnodes(),
			                push(match()),
			                '"', 
			                Spacing()
	                ),
	                Sequence(
	                        "'",
	                        ZeroOrMore(
	                                FirstOf(
	                                        Escape(),
	                                        Sequence(TestNot(AnyOf("\r\n'\\")), ANY)
	                                )
	                        ).suppressSubnodes(),
	     	               push(match()),
	     	               "'", 
	     	               Spacing()
	               )
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
    
    /**
     * Pushes the qualified identifier as String
     * 
     * @return
     */
	Rule QualifiedIdentifier() {
        return Sequence(
        		Identifier(), 
        		ZeroOrMore(
        				Sequence(
        						DOT, 
        						Identifier(),
        						push(pop(1) + "." + pop())
        				)
        		)
        );
    }
	
	/**
	 * Pushes the identifier as String
	 * 
	 * @return
	 */
	@SuppressSubnodes
    @MemoMismatches
    Rule Identifier() {
        return Sequence(
        		Sequence(TestNot(Keyword()), Letter(), ZeroOrMore(LetterOrDigit())),
        		push(match()),
        		Spacing()
        	);
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
}
