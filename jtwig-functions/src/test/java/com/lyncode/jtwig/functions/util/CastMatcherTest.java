package com.lyncode.jtwig.functions.util;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class CastMatcherTest {
    private CastMatcher<Integer> underTest = new CastMatcher<>(Integer.class, equalTo(0));

    @Test
    public void assertionTrue() throws Exception {
        assertTrue(underTest.matches(0));
    }
    @Test
    public void assertionFalse() throws Exception {
        assertFalse(underTest.matches(1));
    }
    @Test
    public void descriptionFine() throws Exception {
        Description description = new StringDescription();
        underTest.describeTo(description);

        Description expected = new StringDescription();
        equalTo(0).describeTo(expected);

        assertEquals(expected.toString(), description.toString());
    }
}