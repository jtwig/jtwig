package org.jtwig.acceptance.functions;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NullVarArgsTest {
    @Test
    public void canExecuteWithNullVarArgsPassed() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ concat('foo', 'bar', null) }}")
            .render(model);

        assertThat(result, is(equalTo("foobar")));
    }
}
