package org.jtwig.unit.util;

import org.jtwig.util.UrlPath;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class UrlPathTest {

    @Test
    public void append() throws Exception {
        UrlPath empty = new UrlPath();
        UrlPath one = new UrlPath().append("/");
        UrlPath two = new UrlPath().append("/").append("/");

        assertThat(empty.toString(), equalTo(""));
        assertThat(one.toString(), equalTo("/"));
        assertThat(two.toString(), equalTo("/"));
    }
    @Test
    public void addThePathSeparatorIfMissing() throws Exception {
        UrlPath underTest = new UrlPath().append("/one").append("two/");

        assertThat(underTest.toString(), equalTo("/one/two/"));
    }
}