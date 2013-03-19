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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.DontLabel;
import org.parboiled.annotations.MemoMismatches;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;

import com.lyncode.jtwig.elements.Block;
import com.lyncode.jtwig.elements.EncapsuledIdentifier;
import com.lyncode.jtwig.elements.ExpressionOperator;
import com.lyncode.jtwig.elements.Extends;
import com.lyncode.jtwig.elements.FastExpression;
import com.lyncode.jtwig.elements.For;
import com.lyncode.jtwig.elements.FunctionExpr;
import com.lyncode.jtwig.elements.If;
import com.lyncode.jtwig.elements.Include;
import com.lyncode.jtwig.elements.Invoke;
import com.lyncode.jtwig.elements.JtwigExpression;
import com.lyncode.jtwig.elements.ObjectList;
import com.lyncode.jtwig.elements.ObjectMap;
import com.lyncode.jtwig.elements.Set;
import com.lyncode.jtwig.elements.Variable;
import com.lyncode.jtwig.exceptions.JtwigParsingException;
import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.render.Argumentable;

/**
 * 
 * @author "João Melo <jmelo@lyncode.com>"
 */
public class JtwigExtendedParser extends BaseParser<Object> {
	private static Logger log = LogManager.getLogger(JtwigExtendedParser.class);
	private static JtwigExtendedParser parser = null;

	public boolean WriteIt (Object obj) {
		log.debug(obj);
		return true;
	}
	
	@Override
	public Object pop(int down) {
    	Object e = super.pop(down);
    	log.debug("Popping at "+down+": "+e.toString());
		return e;
	}

	@Override
	public boolean push(Object value) {
    	boolean e = super.push(value);
    	log.debug("Pushing: "+value.toString());
		return e;
	}

	@Override
	public Object pop() {
		Object e = super.pop();
    	log.debug("Poping: "+e.toString());
		return e;
	}
	
	public static void main (String... str) throws JtwigParsingException, JtwigRenderException {
		BasicConfigurator.configure();
		String input = "{% for a in [1..16] %}{{ a }}{{ test }}{% set test = test + 1 %}{% endfor %}";
		log.debug(input);
		ObjectList l = parse(input);
		Map<String, Object> model = new TreeMap<String, Object>();
		List<String> t = new ArrayList<String>();
		t.add("joao");
		t.add("melo");
		t.add("test");
		model.put("test", 1);
		model.put("list", t);
		
		System.out.println(l.render(model));
		log.debug("'"+l+"'");
		log.debug(model.get("test"));
		
		
	}

	public static ObjectList parse (String input) throws JtwigParsingException {
    	if (parser == null)
    		parser = Parboiled.createParser(JtwigExtendedParser.class);
        // String input = "{{ path('aaaa') }}            ";
        // 
    	if (input == null) throw new JtwigParsingException();
    	// treat the input
    	input = input.replaceAll("\\n\\s*"+Pattern.quote("{%"), "{%");
    	input = input.replaceAll(Pattern.quote("{%")+"\\s*\\n", "%}");
    	
    	
    	
    	try {
    		log.debug("Start new parse");
    		//ParsingResult<JtwigElement> result = new TracingParseRunner<JtwigElement>(parser.JtwigContentRoot()).run(input);
    		//ParsingResult<Object> result = new BasicParseRunner<Object>(parser.Start()).run(input);
    		ParsingResult<Object> result = new RecoveringParseRunner<Object>(parser.Start()).run(input);
    		
            Object e = result.valueStack.pop();
            if (e instanceof ObjectList)
            	return (ObjectList) e;
            else
            	throw new JtwigParsingException("A parsing error occurred");
    	} catch (IllegalArgumentException e) {
    		throw new JtwigParsingException("Template is not well-formed", e);
    	}
    }
	
	final String TRUE = "true";
    final String FALSE = "false";
    final String IF = "if";
    final String ELSE = "else";
    final String ELSEIF = "elseif";
    final String ENDIF = "endif";
    final String BLOCK = "block";
    final String ENDBLOCK = "endblock";
    final String FOR = "for";
    final String ENDFOR = "endfor";
    final String IN = "in";
    final String INCLUDE = "include";
    final String EXTENDS = "extends";
    final String INVOKE = "invoke";
    final String CALL = "call";
    final String WITH = "with";
    final String START_FIRST = "first";
    final String START_LAST = "last";
    final String END_FIRST = "endfirst";
    final String END_LAST = "endlast";
    final String SET = "set";
    final String FILTER = "filter";

    final Rule LPARENT = Terminal("(");
    final Rule RPARENT = Terminal(")");
    final Rule OR = Terminal("|");
    final Rule DOT = Terminal(".");
    final Rule COMMA = Terminal(",");
    final Rule DIV = Terminal(":");
    final Rule OFAST = Terminal("{{");
    final Rule COMMENTSTART = Terminal("{#");
    final Rule COMMENTEND = Terminal("#}", false);
    final Rule CFAST = Terminal("}}", false);
    final Rule LISTOPEN = Terminal("[");
    final Rule LISTCLOSE = Terminal("]");
    final Rule MAPOPEN = Terminal("{");
    final Rule MAPCLOSE = Terminal("}");
    final Rule CODEOPEN = Terminal("{%");
    final Rule TWODOTS = Terminal("..");
    final Rule CODECLOSE = Terminal("%}", false);
    final Rule ATTR = Terminal("=");

    final Rule STAR = Terminal("*");
    final Rule MOD = Terminal("%");
    final Rule PLUS = Terminal("+");
    final Rule MINUS = Terminal("-");
    final Rule LE = Terminal("<=");
    final Rule GE = Terminal(">=");
    final Rule GT = Terminal(">");
    final Rule LT = Terminal("<");
    final Rule EQUAL = Terminal("==");
    final Rule NOTEQUAL = Terminal("!=");
    final Rule ANDAND = Terminal("&&");
    final Rule OROR = Terminal("||");
    
    
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
    			WriteIt("Entering Rule (NormalTemplate)"),
    			Spacing(),
    			push(new ObjectList()),
    			Content(),
    			EOI,
    			WriteIt("Exiting Rule (NormalTemplate)")
    	);
    }
    
    /**
     * Pushes one ObjectList
     * 
     * @return
     */
    Rule ExtendingTemplate () {
    	return Sequence (
    			WriteIt("Entering Rule (ExtendingTemplate)"),
    			Spacing(),
    			ExtendsExpression(),
    			push(new ObjectList()),
    			((ObjectList) peek()).add(pop(1)),
    			ZeroOrMore(
    					Spacing(),
    					BlockExpression(),
    	    			((ObjectList) peek(1)).add(pop())
    			),
    			Spacing(),
    			EOI,
    			WriteIt("Exiting Rule (ExtendingTemplate)")
    	);
    }
    
    Rule InvokeExpression () {
    	return Sequence (
    		WriteIt("Entering Rule (InvokeExpression)"),
    		CODEOPEN,
    		FirstOf(
    				Keyword(INVOKE),
    				Keyword(CALL)
    		),
    		QualifiedIdentifier(),
    		Identifier(),
    		push(new Invoke((String)pop(1), (String)pop())),
    		Optional(
    				Keyword(WITH),
    				MAPOPEN,
        			Optional(
        					Identifier(),
        					DIV,
        					Expression(),
        					((Invoke)peek(2)).add((String)pop(1), pop()),
        					ZeroOrMore(
        							COMMA,
        	    					Identifier(),
        	    					DIV,
        	    					Expression(),
        	    					((ObjectMap)peek(2)).add((String)pop(1), pop())
        					)
        			),
        			MAPCLOSE
    		),
    		CODECLOSE,
    		WriteIt("Exiting Rule (InvokeExpression)")
    	);
    }
    
    /**
     * Pushes a Block
     * 
     * @return
     */
    Rule BlockExpression () {
    	return Sequence(
        	WriteIt("Entering Rule (BlockExpression)"),
    		CODEOPEN,
    		Keyword(BLOCK),
    		Identifier(),
    		push(new Block((String)pop())),
    		CODECLOSE,
    		Content(),
    		CODEOPEN,
    		Keyword(ENDBLOCK),
    		CODECLOSE,
    		WriteIt("Exiting Rule (BlockExpression)")
    	);
    }
    
    /**
     * Pushes an Extends
     * 
     * @return
     */
    Rule ExtendsExpression () {
    	return Sequence(
            	WriteIt("Entering Rule (ExtendsExpression)"),
    			CODEOPEN,
    			Keyword(EXTENDS),
    			StringLiteral(),
    			push(new Extends((String) pop())),
    			CODECLOSE,
        		WriteIt("Exiting Rule (ExtendsExpression)")
    	);
    }
    
    /**
     * Pushes an Include
     * 
     * @return
     */
    Rule IncludeExpression () {
    	return Sequence(
            	WriteIt("Entering Rule (IncludeExpression)"),
    			CODEOPEN,
    			Keyword(INCLUDE),
    			StringLiteral(),
    			push(new Include((String)pop())),
    			CODECLOSE,
        		WriteIt("Exiting Rule (IncludeExpression)")
    	);
    }
    
    /**
     * No Pushes!!
     * 
     * @return
     */
    Rule Content () {
    	return Sequence(
    			WriteIt("Entering Rule (Content)"),
	    			ZeroOrMore(
		    			FirstOf(
		    					SetExpression(),
		    					FastExpression(),
		    					InvokeExpression(),
		    					ForExpression(),
		    					IfExpression(),
		    					BlockExpression(),
		    					IncludeExpression(),
		    					TextRule()
		    			),
		    			((ObjectList)peek(1)).add(pop())
	    			),
	    		WriteIt("Leaving Rule (Content)")
    	
    	);
    }

	/**
	* Pushes the text
	* 
	* @return
	*/
	Rule TextRule () {
		return Sequence(
				WriteIt("Entering Rule (TextRule)"),
				push(""),
				OneOrMore(
						FirstOf(
								Sequence("{#", ZeroOrMore(TestNot("#}"), ANY), "#}"),
                                Sequence(
                                		Escape(),
                                		push(pop()+match())
                                ),
                                Sequence(
                                		TestNot(FirstOf(OFAST, CODEOPEN)), 
                                		ANY,
                                		push(pop()+match())
                                )
                        )
				).suppressSubnodes(),
		    	WriteIt("Leaving Rule (TextRule)")
		);
	}
	
	
	/**
	 * Pushes one IF
	 * 
	 * @return
	 */
	Rule IfExpression () {
		return Sequence(
            	WriteIt("Entering Rule (IfExpression)"),
				CODEOPEN,
				Keyword(IF),
				Expression(),
				push(new If(pop())),
				CODECLOSE,
				Content(),
				ZeroOrMore(
						Sequence(
								WriteIt("Entering Elseif"),
								CODEOPEN,
								Keyword(ELSEIF),
								Expression(),
								push(new If(pop())),
								CODECLOSE,
								Content(),
								((If)peek(1)).addElseIf((If)pop()),
								WriteIt("Leaving Elseif")
						)
				),
				Optional(
						Sequence(
							WriteIt("Entering Else (IfExpression)"),
							CODEOPEN,
							Keyword(ELSE),
							CODECLOSE,
							push(new ObjectList()),
							Content(),
							((If)peek(1)).setElse((ObjectList)pop()),
							WriteIt("Leaving Else (IfExpression)")
						)
				),
				CODEOPEN,
				Keyword(ENDIF),
				CODECLOSE,
				WriteIt("Leaving Rule (IfExpression)")
		);
	}
	
    /***
     * Pushes a For
     * 
     * @return
     */
    Rule ForExpression () {
    	return Sequence(
            WriteIt("Entering Rule (ForExpression)"),
    		CODEOPEN,
    		Keyword(FOR),
    		Identifier(),
    		Keyword(IN),
    		Expression(),
    		push(new For((String)pop(1), pop())),
    		Optional(
    				ForFilters()
    		),
    		CODECLOSE,
    		// There is no first/last expressions
    		// Optional(FirstExpression(), ((For)peek(1)).setFirst(((ObjectList)pop()))),
    		Content(),
    		// Optional(LastExpression(), ((For)peek(1)).setLast(((ObjectList)pop()))),
    		CODEOPEN,
    		Keyword(ENDFOR),
    		CODECLOSE,
			WriteIt("Leaving Rule (ForExpression)")
    	);
    }
    
    Rule ForFilters () {
    	return ZeroOrMore(
    			Sequence(
    					OR,
    					Keyword(FILTER),
    					Expression(),
    					((For) peek(1)).addFilter(((JtwigExpression) pop()))
    			)
    	);
    }
    
    /**
     * Pushes one Set
     * 
     * @return
     */
    Rule SetExpression () {
    	return Sequence(
                WriteIt("Entering Rule (SetExpression)"),
    			CODEOPEN,
    			Keyword(SET),
    			Identifier(),
    			ATTR,
    			Expression(),
    			CODECLOSE,
    			push(new Set((String)pop(1), pop())),
    			WriteIt("Leaving Rule (SetExpression)")
    	);
    }
    /**
     * Pushes one fast expression
     * 
     * @return
     */
    Rule FastExpression () {
    	return Sequence(
                WriteIt("Entering Rule (FastExpression)"),
    			OFAST,
    			Expression(),
    			push(new FastExpression(pop())),
    			ZeroOrMore(Sequence(
    					OR,
    					Function(),
    					((FastExpression) peek(1)).add((FunctionExpr)pop())
    			)),
    			CFAST,
    			WriteIt("Leaving Rule (FastExpression)")
    	);
    }
    
    /**
     * Pushes one expression
     * 
     * @return
     */
    Rule Expression() {
        return ConditionalOrExpression();
    }
    
    Rule ConditionalOrExpression() {
        return Sequence(
        		ConditionalAndExpression(),
        		push(new JtwigExpression(ExpressionOperator.OR)),
        		((Argumentable)peek()).add(pop(1)),
                ZeroOrMore(
                		OROR, 
                		ConditionalAndExpression(),
                		((Argumentable)peek(1)).add(pop())
                )
        );
    }
    
    Rule ConditionalAndExpression() {
        return Sequence(
        		EqualityExpression(),
        		push(new JtwigExpression(ExpressionOperator.AND)),
        		((Argumentable)peek()).add(pop(1)),
                ZeroOrMore(
                		ANDAND, 
                		EqualityExpression(),
                		((Argumentable)peek(1)).add(pop())
                )
        );
    }
    


    Rule EqualityExpression() {
        return Sequence(
        		NotEqualityExpression(),
        		push(new JtwigExpression(ExpressionOperator.EQUAL)),
        		((Argumentable)peek()).add(pop(1)),
        		ZeroOrMore(
                		EQUAL, 
                		NotEqualityExpression(),
                		((Argumentable)peek(1)).add(pop())
                )
        );
    }
    
    Rule NotEqualityExpression() {
        return Sequence(
        		RelationalExpression(),
        		push(new JtwigExpression(ExpressionOperator.NOTEQUAL)),
                ((Argumentable)peek()).add(pop(1)),
                ZeroOrMore(
                		NOTEQUAL, 
                		RelationalExpression(),
                		((Argumentable)peek(1)).add(pop())
                )
        );
    }

    Rule RelationalExpression() {
        return Sequence(
        		AdditiveExpression(),
        		push(new JtwigExpression()),
        		((Argumentable)peek()).add(pop(1)),
                ZeroOrMore(
                        Sequence(
                        		FirstOf(
                        				Sequence(LE, ((JtwigExpression) peek()).setOperator(ExpressionOperator.LE)), 
                        				Sequence(GE, ((JtwigExpression) peek()).setOperator(ExpressionOperator.GE)),
                        				Sequence(LT, ((JtwigExpression) peek()).setOperator(ExpressionOperator.LT)),
                        				Sequence(GT, ((JtwigExpression) peek()).setOperator(ExpressionOperator.GT))
                        		), 
                        		AdditiveExpression(),
                        		((Argumentable)peek(1)).add(pop())
                        )
                )
        );
    }
    

    Rule AdditiveExpression() {
        return Sequence(
        		MultiplicativeExpression(),
        		push(new JtwigExpression()),
        		((Argumentable)peek()).add(pop(1)),
                ZeroOrMore(
                		FirstOf(
                				Sequence(PLUS, ((JtwigExpression) peek()).setOperator(ExpressionOperator.PLUS)),
                				Sequence(MINUS, ((JtwigExpression) peek()).setOperator(ExpressionOperator.MINUS))
                		), 
                		MultiplicativeExpression(),
                		((Argumentable)peek(1)).add(pop())
                )
        );
    }

    Rule MultiplicativeExpression() {
        return Sequence(
        		UnaryExpression(),
        		push(new JtwigExpression()),
        		((Argumentable)peek()).add(pop(1)),
                ZeroOrMore(
                	FirstOf(
                			Sequence(STAR, ((JtwigExpression) peek()).setOperator(ExpressionOperator.STAR)),
                			Sequence(DIV, ((JtwigExpression) peek()).setOperator(ExpressionOperator.DIV)),
                			Sequence(MOD, ((JtwigExpression) peek()).setOperator(ExpressionOperator.MOD))
                	),
                	UnaryExpression(),
                	((Argumentable)peek(1)).add(pop())
                )
        );
    }
    
    Rule UnaryExpression() {
        return Primary();
    }
    
    Rule Primary() {
        return FirstOf(
                ParExpression(),
        		FunctionExpression(),
        		FunctionOneArgumentExpression(),
        		StringLiteral(),
                CallExpression(),
                Variable(),
                Integer(),
                Boolean(),
                MapExpression(),
                ListExpression()
        );
    }
    
    Rule ParExpression() {
        return Sequence(LPARENT, Expression(), RPARENT);
    }

    Rule Arguments () {
    	return Sequence(
    			WriteIt("Entering Rule (Arguments)"),
    			Expression(),
    			((Argumentable) peek(1)).add(pop()),
    			ZeroOrMore(
    				COMMA,
    				Expression(),
        			((Argumentable) peek(1)).add(pop())
    			),
    			WriteIt("Leaving Rule (Arguments)")
    	);
    }
    
    /**
     * Pushes one call
     * 
     * @return
     */
    Rule FunctionExpression () {
    	return Sequence(
    			WriteIt("Entering Rule (FunctionExpression)"),
    			Identifier(),
    			push(new FunctionExpr((String)pop())),
    			LPARENT,
    			Arguments(),
    			RPARENT,
    			WriteIt("Leaving Rule (FunctionExpression)")
    	);
    }

    /**
     * Pushes one call
     * 
     * @return
     */
    Rule FunctionOneArgumentExpression () {
    	return Sequence(
    			WriteIt("Entering Rule (FunctionExpression)"),
    			Identifier(),
    			push(new FunctionExpr((String)pop())),
    			Expression(),
    			((Argumentable) peek(1)).add(pop()),
    			WriteIt("Leaving Rule (FunctionExpression)")
    	);
    }
    
    /**
     * Pushes one call
     * 
     * @return
     */
    Rule CallExpression () {
    	return Sequence(
    			WriteIt("Entering Rule (CallExpression)"),
    			Identifier(),
    			push(new EncapsuledIdentifier((String) pop())),
    			Sequence(
						DOT,
    					FirstOf(
    							Sequence(
    									Identifier(),
    									push(new EncapsuledIdentifier((String) pop())),
    									LPARENT,
    					    			Arguments(),
    					    			RPARENT
    							),
    							Sequence(Identifier(), push(new EncapsuledIdentifier((String)pop())))
    					)
				),
    			((EncapsuledIdentifier)peek(1)).setNext((EncapsuledIdentifier)peek()),
    			ZeroOrMore(
    					Sequence(
    						DOT,
	    					FirstOf(
	    							Sequence(
	    									Identifier(),
	    									push(new EncapsuledIdentifier((String) pop())),
	    									LPARENT,
	    					    			Arguments(),
	    					    			RPARENT
	    							),
	    							Sequence(Identifier(), push(new EncapsuledIdentifier((String)pop())))
	    					),
	    					((EncapsuledIdentifier)pop(1)).setNext((EncapsuledIdentifier)peek())
    					)
    			),
    			ACTION(pop() != null),
    			WriteIt("Leaving Rule (CallExpression)")
    	);
    }
    
    
    
    
        
    /**
     * Pushes a Function
     * 
     * @return
     */
    Rule Function () {
    	return Sequence(
    			WriteIt("Entering Rule (Function)"),
    			Identifier(),
    			push(new FunctionExpr((String) pop())),
    			Optional(LPARENT, Arguments(), RPARENT),
    			WriteIt("Leaving Rule (Function)")
    	);
    }
    
    /**
     * Pushes a MAP
     * 
     * @return
     */
    Rule MapExpression () {
    	return Sequence(
    			WriteIt("Entering Rule (MapExpression)"),
    			MAPOPEN,
    			push(new ObjectMap()),
    			Optional(
    					Identifier(),
    					DIV,
    					Expression(),
    					((ObjectMap)peek(2)).add((String)pop(1), pop()),
    					ZeroOrMore(
    							COMMA,
    	    					Identifier(),
    	    					DIV,
    	    					Expression(),
    	    					((ObjectMap)peek(2)).add((String)pop(1), pop())
    					)
    			),
    			MAPCLOSE,
    			WriteIt("Leaving Rule (MapExpression)")
    	);
    }
    
    /**
     * Pushes a list
     * 
     * @return
     */
	Rule ListExpression () {
    	return Sequence(
    			WriteIt("Entering Rule (ListExpression)"),
    			LISTOPEN,
    			FirstOf(
			    		Sequence(
			    			Integer(),
			    			TWODOTS,
			    			Integer(),
			    			push(new ObjectList((Integer)pop(1), (Integer)pop()))
			    		),
    					Sequence(push(new ObjectList()),
			    			Optional(
			    					Expression(),
			    					((ObjectList)peek(1)).add(pop()),
			    					ZeroOrMore(
			    							COMMA,
			    	    					Expression(),
			    	    					((ObjectList)peek(1)).add(pop())
			    					)
			    			)
			    		)
			    ),
    			
    			LISTCLOSE,
    			WriteIt("Leaving Rule (ListExpression)")
    	);
    }
    
    /**
     * Pushes a boolean
     * 
     * @return
     */
    Rule Boolean () {
    	return Sequence(
    		WriteIt("Entering Rule (Boolean)"),
    		FirstOf(
    			Sequence(TRUE, push(new Boolean(true))),
    			Sequence(FALSE, push(new Boolean(false)))
    		),
    		WriteIt("Leaving Rule (Boolean)")
    	);
    }
    
    /**
     * Pushes the integer (as integer)
     * 
     * @return
     */
    Rule Integer () {
    	return Sequence(
    			WriteIt("Entering Rule (Integer)"),
    			OneOrMore(Digit()),
    			push(Integer.parseInt(match())),
    			WriteIt(peek()),
    			WriteIt("Entering Rule (Integer)")
    	);
    }
    
    /**
     * Pushes the variable (String)
     * 
     * @return
     */
    Rule Variable () {
    	return Sequence(
    			WriteIt("Entering Rule (Variable)"),
    			QualifiedIdentifier(),
    			push(new Variable((String) pop())),
    			WriteIt("Leaving Rule (Variable)")
    	);
    }
    
    /**
     * Pushes the String (without quotes)
     * 
     * @return
     */
    Rule StringLiteral() {
        return Sequence(
        		WriteIt("Entering Rule (StringLiteral)"),
        		FirstOf(
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
	         ),
	         WriteIt("Leaving Rule (StringLiteral)")
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
        		WriteIt("Entering Rule (QualifiedIdentifier)"),
        		Identifier(), 
        		ZeroOrMore(
        				Sequence(
        						DOT, 
        						Identifier(),
        						push(pop(1) + "." + pop())
        				)
        		),
        		WriteIt("Leaving Rule (QualifiedIdentifier)")
        );
    }
	
	/**
     * Pushes the qualified identifier as String
     * 
     * @return
     */
	Rule QualifiedMethodIdentifier() {
        return Sequence(
        		WriteIt("Entering Rule (QualifiedMethodIdentifier)"),
        		Identifier(), 
        		OneOrMore(
        				Sequence(
        						DOT, 
        						Identifier(),
        						push(pop(1) + "." + pop())
        				)
        		),
        		WriteIt("Leaving Rule (QualifiedMethodIdentifier)")
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
        		WriteIt("Entering Rule (Identifier)"),
        		Sequence(TestNot(Keyword()), Letter(), ZeroOrMore(LetterOrDigit())),
        		push(match()),
        		Spacing(),
        		WriteIt("Leaving Rule (Identifier)")
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
			return Sequence(Spacing(), String(string)).label('\'' + string + '\'');
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
                Sequence("{#", ZeroOrMore(TestNot("#}"), ANY), "#}")

                // end of line comment
                /*Sequence(
                        "//",
                        ZeroOrMore(TestNot(AnyOf("\r\n")), ANY),
                        FirstOf("\r\n", '\r', '\n', EOI)
                )*/
        ));
    }
}
