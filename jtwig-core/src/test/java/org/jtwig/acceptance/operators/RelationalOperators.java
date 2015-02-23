package org.jtwig.acceptance.operators;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

public class RelationalOperators {
    @Test
    public void diffOperatorWhenTrue() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if ('one' != 'two') %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(equalTo("Hi")));
    }

    @Test
    public void diffOperatorWhenFalse() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if ('one' != 'one') %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(equalTo("")));
    }
    
    @Test
    public void differentTypesWithSameStringValueAreEqual() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("var1", 5L).withModelAttribute("var2", "5");

        String result = JtwigTemplate
            .inlineTemplate("{{ var1 == var2 }}")
            .render(model);

        assertThat(result, is(equalTo("1")));
    }

    @Test
    public void differentTypesWithSameStringValueAreEqual2() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("var1", 'a').withModelAttribute("var2", "a");

        String result = JtwigTemplate
            .inlineTemplate("{{ var1 == var2 }}")
            .render(model);

        assertThat(result, is(equalTo("1")));
    }
}
