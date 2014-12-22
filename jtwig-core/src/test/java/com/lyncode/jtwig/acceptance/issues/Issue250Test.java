package com.lyncode.jtwig.acceptance.issues;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Issue250Test {

    @Test
    public void numberFormatShouldHandleNullAsZero() throws Exception {
        JtwigTemplate template = JtwigTemplate.fromString("{{ null | number_format(2, ',', '.') }}");
        JtwigModelMap context = new JtwigModelMap();
        assertThat(template.output(context), is("0,00"));
    }
}
