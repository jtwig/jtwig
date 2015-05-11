package org.jtwig.acceptance.issues;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Issue246Test {
    @Test
    public void outputEscapedDoubleQuoteTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ \"\\\"\" }}")
            .render(model);

        assertThat(result, is(equalTo("\"")));
    }

    @Test
    public void outputEscapedSingleQuoteTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ '\\'' }}")
            .render(model);

        assertThat(result, is(equalTo("'")));
    }
}
