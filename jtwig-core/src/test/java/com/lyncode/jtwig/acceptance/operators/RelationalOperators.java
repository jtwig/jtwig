package com.lyncode.jtwig.acceptance.operators;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.acceptance.AbstractJtwigTest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
}
