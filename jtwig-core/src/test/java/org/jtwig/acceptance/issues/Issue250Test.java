package org.jtwig.acceptance.issues;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Issue250Test {
    @Test
    public void numberFormatShouldHandleNullAsZero() throws Exception {
        JtwigTemplate template = JtwigTemplate.fromString("{{ null | number_format(2, ',', '.') }}");
        JtwigModelMap context = new JtwigModelMap();
        assertThat(template.output(context), is("0,00"));
    }
}
