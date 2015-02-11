package org.jtwig.unit.util;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.jtwig.util.FilePath.path;
import static org.jtwig.util.FilePath.parentOf;

public class FilePathTest {
    @Test
    public void pathTest() throws Exception {
        assertThat(path("/test/test").parent().toString(), equalTo("/test"));
        assertThat(parentOf("/test/test").append("one").toString(), equalTo("/test/one"));
        assertThat(path("/test", "test").parent().toString(), equalTo("/test"));
    }

    @Test
    public void normalize() throws Exception {
        assertThat(path("/test/../hello").normalize(), equalTo("/hello"));
    }
}
