package org.jtwig.functions.reflection;

import org.jtwig.functions.annotations.Parameter;
import org.junit.Test;

import static org.junit.Assert.*;

public class JavaMethodParameterTest {
    @Test
    public void hasAnnotationShouldReturnTrueWhenParameterAnnotatedWithIt() throws Exception {
        JavaMethodParameter parameter = new JavaMethodParameter(0, this.getClass().getDeclaredMethod("example", String.class));
        assertTrue(parameter.hasAnnotation(Parameter.class));
    }

    @Test
    public void hasAnnotationShouldReturnFalseWhenParameterNotAnnotatedWithIt() throws Exception {
        JavaMethodParameter parameter = new JavaMethodParameter(0, this.getClass().getDeclaredMethod("exampleWithoutParameter", String.class));
        assertFalse(parameter.hasAnnotation(Parameter.class));
    }

    @Test
    public void returnsTypeOfMethodParameter() throws Exception {
        JavaMethodParameter parameter = new JavaMethodParameter(0, this.getClass().getDeclaredMethod("example", String.class));

        assertEquals(String.class, parameter.type());
    }

    @Test
    public void annotationIndex() throws Exception {
        JavaMethodParameter parameter = new JavaMethodParameter(1, this.getClass().getDeclaredMethod("example", Integer.class, String.class));

        assertEquals(String.class, parameter.type());
        assertEquals(0, parameter.annotationIndex(Parameter.class));
    }

    public void example (@Parameter String parameter) {

    }

    public void example (Integer hello, @Parameter String parameter) {

    }

    public void exampleWithoutParameter (String parameter) {

    }
}
