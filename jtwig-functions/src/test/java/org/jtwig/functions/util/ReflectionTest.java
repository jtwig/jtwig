package org.jtwig.functions.util;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReflectionTest {
    @Test
    public void noVarArgsArgument() throws Exception {
        assertFalse(noVarArgsMethod().isVarArgs());
    }

    @Test
    public void varArgsArgument() throws Exception {
        assertTrue(varArgsMethod().isVarArgs());
    }

    private Method varArgsMethod() {
        try {
            return getClass().getDeclaredMethod("example", String[].class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Method noVarArgsMethod() {
        try {
            return getClass().getDeclaredMethod("example", String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public String example (String value) {
        return value;
    }

    public String example (String... value) {
        return "echo";
    }
}
