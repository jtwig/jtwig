package org.jtwig.acceptance.issues;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Issue246Test {
    @Test
    public void outputEscapedDoubleQuoteTest() throws Exception {
        JtwigTemplate template = JtwigTemplate.fromString("{{ \"\\\"\" }}");
        JtwigModelMap context = new JtwigModelMap();
        assertThat(template.output(context), is("\""));
    }

    @Test
    public void outputEscapedSingleQuoteTest() throws Exception {
        JtwigTemplate template = JtwigTemplate.fromString("{{ '\\'' }}");
        JtwigModelMap context = new JtwigModelMap();
        assertThat(template.output(context), is("'"));
    }
}
