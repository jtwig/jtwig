package org.jtwig.acceptance.issues;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.jtwig.AbstractJtwigTest;

public class Issue246Test extends AbstractJtwigTest {
    @Test
    public void outputEscapedDoubleQuoteTest() throws Exception {
        withResource("{{ \"\\\"\" }}");;
        assertThat(theResult(), is("\""));
    }

    @Test
    public void outputEscapedSingleQuoteTest() throws Exception {
        withResource("{{ '\\'' }}");
        assertThat(theResult(), is("'"));
    }
}
