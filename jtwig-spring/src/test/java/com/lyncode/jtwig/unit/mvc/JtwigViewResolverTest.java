package com.lyncode.jtwig.unit.mvc;

import com.lyncode.jtwig.mvc.JtwigViewResolver;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JtwigViewResolverTest {
    private JtwigViewResolver underTest = new JtwigViewResolver();

    @Test
    public void setThemeTest() throws Exception {
        assertThat(underTest.hasTheme(), is(false)); // By default
        underTest.setTheme("theme");

        assertTrue(underTest.hasTheme());
        assertThat(underTest.getTheme(), is(equalTo("theme")));
    }

    @Test
    public void setCacheTest() throws Exception {
        assertThat(underTest.isCached(), is(true)); // By default
        underTest.setCached(false);
        assertFalse(underTest.isCached());
    }

    @Test
    public void setEncodingTest() throws Exception {
        assertThat(underTest.getEncoding(), is(nullValue()));
        underTest.setEncoding("value");
        assertThat(underTest.getEncoding(), is("value"));
    }
}