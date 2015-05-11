package org.jtwig.acceptance.issues;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;

import org.hamcrest.MatcherAssert;

import static org.junit.Assert.assertThat;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

public class Issue250Test {
    @Test
    public void numberFormatShouldHandleNullAsZero() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ null | number_format(2, ',', '.') }}")
            .render(model);

        MatcherAssert.assertThat(result, is(equalTo("0,00")));
    }
}
