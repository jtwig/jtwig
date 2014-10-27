/*
 * Copyright 2014 thomas.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.jtwig.acceptance.issues;

import com.lyncode.jtwig.acceptance.AbstractJtwigTest;
import static com.lyncode.jtwig.util.SyntacticSugar.given;
import static com.lyncode.jtwig.util.SyntacticSugar.then;
import static com.lyncode.jtwig.util.SyntacticSugar.when;
import java.nio.charset.Charset;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;

/**
 * 
 */
public class Issue210Test extends AbstractJtwigTest {
    @Test
    public void testNonUTFEncoding() throws Exception {
        theConfiguration().render().charset(Charset.forName("ISO-8859-1"));
        given(aModel().withModelAttribute("text", "tête de bou  간편한 설치 및 사용"));
        when(jtwigRenders(template("{{ text }}")));
        then(theRenderedTemplate(), is(equalTo("t�te de bou  ??? ?? ? ??")));
    }
    @Test
    public void testUTFEncoding() throws Exception {
        given(aModel().withModelAttribute("text", "tête de bou  간편한 설치 및 사용"));
        when(jtwigRenders(template("{{ text }}")));
        then(theRenderedTemplate(), is(equalTo("tête de bou  간편한 설치 및 사용")));
    }
}