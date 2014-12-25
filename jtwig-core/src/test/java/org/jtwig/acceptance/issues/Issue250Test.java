package org.jtwig.acceptance.issues;

import static org.hamcrest.core.Is.is;
import org.jtwig.AbstractJtwigTest;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class Issue250Test extends AbstractJtwigTest {
    @Test
    public void numberFormatShouldHandleNullAsZero() throws Exception {
        withResource("{{ null | number_format(2, ',', '.') }}");
        assertThat(theResult(), is("0,00"));
    }
}
