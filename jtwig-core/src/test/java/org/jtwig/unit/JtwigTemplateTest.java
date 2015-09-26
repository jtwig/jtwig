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

package org.jtwig.unit;

import org.junit.Test;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JtwigTemplateTest extends MultiresourceUnitTest {
    @Test
    public void testRootDocument() throws Exception {
        withResource("joao");
        assertThat(theResult(), is("joao"));
    }

    @Test
    public void testSingleHierarchy() throws Exception {
        withResource("{% extends 'test' %}{% block joao %}joao{% endblock %}");
        withResource("test", "I am {% block joao %}no one{% endblock %}");

        assertThat(theResult(), is("I am joao"));
    }

    @Test
    public void testTwoLevelHierarchy() throws Exception {
        withResource("{% extends 'level-1' %}{% block two %}two{% endblock %}");
        withResource("level-1", "{% extends 'root' %}{% block one %}one{% endblock %}");
        withResource("root", "Block {% block one %}1{% endblock %} and {% block two %}2{% endblock %}");
        
//        env.parse("level-1");
        
        assertThat(theResult(), is("Block one and two"));
    }
    
    @Test
    public void testExtendsVariableDefinedTemplate() throws Exception {
        withResource("{% extends var %}");
        withResource("num-one", "first");
        withResource("num-two", "second");
        
        model.withModelAttribute("var", "num-two");
        assertThat(theResult(), is("second"));
    }
    
    @Test
    public void testExtendsExpression() throws Exception {
        withResource("{% extends var ? 'num-one' : 'num-two' %}");
        withResource("num-one", "first");
        withResource("num-two", "second");
        
        model.withModelAttribute("var", false);
        assertThat(theResult(), is("second"));
    }
    
    @Test
    public void testExtendsWithArrayOfTemplates() throws Exception {
        withResource("{% extends ['num-one','num-two','num-three'] %}");
        withResource("num-two", "second");
        
        assertThat(theResult(), is("second"));
    }
    
    @Test
    public void testSetVariableBeforeExtends() throws Exception {
        withResource("{% set value = \"open\" %} {% extends 'test' %} {% block testblock %}{{ value }}{% endblock %}");
        withResource("test", "{% block testblock %}{{ value }}{% endblock %}");
        
        assertThat(theResult(), is("open"));
    }
    
    @Test
    public void testSetVariableAfterExtends() throws Exception {
        withResource("{% extends 'test' %} {% set value = \"open\" %} {% block testblock %}{{ value }}{% endblock %}");
        withResource("test", "{% block testblock %}{{ value }}{% endblock %}");
        
        assertThat(theResult(), is("open"));
    }
}
