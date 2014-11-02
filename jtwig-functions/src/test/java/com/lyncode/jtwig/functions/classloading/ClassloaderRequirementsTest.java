package com.lyncode.jtwig.functions.classloading;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

// This tests is required to make sure Json mapper works properly
public class ClassloaderRequirementsTest {
    private static int number = 0;

    @Test
    public void makeSureClassesAreOnlyLoadedWhenFirstInstanceCreated() throws Exception {
        TestClass.class.getName();
        assertThat(number, equalTo(0));
        TestClass t;
        assertThat(number, equalTo(0));
        t = new TestClass();
        assertThat(number, equalTo(1));
    }

    public static class TestClass {
        static {
            number++;
        }
    }
}
