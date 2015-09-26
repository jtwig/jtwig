package org.jtwig.acceptance.functions;

import org.jtwig.acceptance.AbstractViewAcceptanceTest;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class PathViewTest extends AbstractViewAcceptanceTest {
    @Test
    public void pathEndingInSlashShouldNotBeTrimmed() throws Exception {
        String result = renderString("{{ path('/one/') }}");

        assertThat(result, equalTo("/one/"));
    }

    @Test
    public void emptyPath() throws Exception {
        String result = renderString("{{ path() }}");

        assertThat(result, equalTo(""));
    }
}
