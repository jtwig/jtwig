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

package com.lyncode.jtwig.acceptance.issues;

import com.lyncode.jtwig.acceptance.AbstractJtwigTest;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import static com.lyncode.jtwig.util.SyntacticSugar.then;
import static com.lyncode.jtwig.util.SyntacticSugar.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;

public class Issue112Test extends AbstractJtwigTest {
    
    @Test(expected = ClassCastException.class)
    public void operationWithNonexistentVarThrowsException() throws Exception {
        when(jtwigRenders(template("{% set a = 5 %}{{ a - b  }}")));
        then(theRenderedTemplate(), is(equalTo("0.1")));
    }
    
    @Test(expected = NullPointerException.class)
    public void operationWithNullVarThrowsException() throws Exception {
        when(jtwigRenders(template("{% set a = 5 %}{% set b = null %}{{ a - b }}")));
        then(theRenderedTemplate(), is(equalTo("0.1")));
    }
    
    @Test
    public void outputNonexistentVarReturnsEmpty() throws Exception {
        theParser().withConfiguration(new ParserConfiguration(){{
            useStrictEvaluation(false);
        }});
        
        when(jtwigRenders(template("{{ nonexistent }}")));
        then(theRenderedTemplate(), is(equalTo("")));
    }
    
    @Test()
    public void outputNonexistentVarThrowsException() throws Exception {
        when(jtwigRenders(template("{{ nonexistent }}")));
        then(theRenderedTemplate(), is(equalTo("undefined")));
    }
    
    @Test
    public void outputNullVarReturnsEmpty() throws Exception {
        theParser().withConfiguration(new ParserConfiguration(){{
            useStrictEvaluation(false);
        }});
        
        when(jtwigRenders(template("{% set nothing = null %}{{ nothing }}")));
        then(theRenderedTemplate(), is(equalTo("")));
    }
    
    @Test
    public void outputNullVarThrowsException() throws Exception {
        when(jtwigRenders(template("{% set nothing = null %}{{ nothing }}")));
        then(theRenderedTemplate(), is(equalTo("null")));
    }
}