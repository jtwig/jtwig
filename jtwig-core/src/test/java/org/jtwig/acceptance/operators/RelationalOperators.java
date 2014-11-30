package org.jtwig.acceptance.operators;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.jtwig.acceptance.AbstractJtwigTest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jtwig.util.SyntacticSugar.*;

public class RelationalOperators extends AbstractJtwigTest {
    private JtwigModelMap context = new JtwigModelMap();

    @Test
    public void diffOperatorWhenTrue() throws Exception {
        JtwigTemplate template = JtwigTemplate.fromString("{% if ('one' != 'two') %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }

    @Test
    public void diffOperatorWhenFalse() throws Exception {
        JtwigTemplate template = JtwigTemplate.fromString("{% if ('one' != 'one') %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("")));
    }
    
    @Test
    public void differentTypesWithSameStringValueAreEqual() throws Exception {
        given(aModel().withModelAttribute("var1", 5L).withModelAttribute("var2", "5"));
        when(jtwigRenders(template("{{ var1 == var2 }}")));
        then(theRenderedTemplate(), is(equalTo("1")));
    }
    @Test
    public void differentTypesWithSameStringValueAreEqual2() throws Exception {
        given(aModel().withModelAttribute("var1", 'a').withModelAttribute("var2", "a"));
        when(jtwigRenders(template("{{ var1 == var2 }}")));
        then(theRenderedTemplate(), is(equalTo("1")));
    }
}
