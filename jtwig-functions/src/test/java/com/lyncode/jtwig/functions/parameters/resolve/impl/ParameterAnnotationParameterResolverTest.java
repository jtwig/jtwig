package com.lyncode.jtwig.functions.parameters.resolve.impl;

import com.google.common.base.Optional;
import com.lyncode.jtwig.functions.annotations.Parameter;
import com.lyncode.jtwig.functions.parameters.convert.DemultiplexerConverter;
import com.lyncode.jtwig.functions.parameters.input.InputParameters;
import com.lyncode.jtwig.functions.parameters.resolve.api.ParameterResolver;
import com.lyncode.jtwig.functions.reflection.JavaMethodParameter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParameterAnnotationParameterResolverTest {
    private static final Object VALUE = null;

    private InputParameters inputParameters = mock(InputParameters.class);
    private DemultiplexerConverter converter = mock(DemultiplexerConverter.class);
    private ParameterResolver underTest = new ParameterAnnotationParameterResolver(inputParameters, converter);

    @Before
    public void setUp() throws Exception {
        when(converter.convert(anyObject(), any(Class.class))).thenReturn(Optional.absent());
    }

    @Test
    public void javaParameterNotAnnotatedWithParameterAttributeShouldReturnAbsent() throws Exception {
        JavaMethodParameter parameter = mock(JavaMethodParameter.class);
        assertFalse(underTest.resolve(parameter).isPresent());
    }

    @Test
    public void ifParameterIsAnnotatedResolveIt() throws Exception {
        JavaMethodParameter parameter = mock(JavaMethodParameter.class);
        when(parameter.hasAnnotation(Parameter.class)).thenReturn(true);
        when(parameter.annotationIndex(Parameter.class)).thenReturn(1);
        when(inputParameters.valueAt(1)).thenReturn(VALUE);

        assertThat(underTest.resolve(parameter).get().value(), equalTo(VALUE));
    }

    @Test
    public void ifParameterIsAnnotatedButTypesDoNotMatchAndConverterUnableToConvert() throws Exception {
        JavaMethodParameter parameter = mock(JavaMethodParameter.class);
        when(parameter.hasAnnotation(Parameter.class)).thenReturn(true);
        when(parameter.annotationIndex(Parameter.class)).thenReturn(1);
        when(parameter.type()).then(returnValue(String.class));
        when(inputParameters.valueAt(1)).thenReturn(1);

        assertFalse(underTest.resolve(parameter).isPresent());
    }

    @Test
    public void ifParameterIsAnnotatedButTypesDoNotMatchAndConverterAbleToConvert() throws Exception {
        JavaMethodParameter parameter = mock(JavaMethodParameter.class);
        when(parameter.hasAnnotation(Parameter.class)).thenReturn(true);
        when(parameter.annotationIndex(Parameter.class)).thenReturn(1);
        when(parameter.type()).then(returnValue(String.class));
        when(inputParameters.valueAt(1)).thenReturn(1);
        when(converter.convert(1, String.class)).thenReturn(Optional.of("1"));

        assertTrue(underTest.resolve(parameter).isPresent());
        assertEquals("1", underTest.resolve(parameter).get().value());
    }

    @Test
    public void arePrimitivesAssignableToItsWrappers() throws Exception {
        JavaMethodParameter parameter = mock(JavaMethodParameter.class);
        when(parameter.hasAnnotation(Parameter.class)).thenReturn(true);
        when(parameter.annotationIndex(Parameter.class)).thenReturn(1);
        when(parameter.type()).then(returnValue(Integer.class));
        when(inputParameters.valueAt(1)).thenReturn(1);

        assertTrue(underTest.resolve(parameter).isPresent());
        assertEquals(1, underTest.resolve(parameter).get().value());
    }

    @Test
    public void areWrappersAssignableToItsPrimitives() throws Exception {
        JavaMethodParameter parameter = mock(JavaMethodParameter.class);
        when(parameter.hasAnnotation(Parameter.class)).thenReturn(true);
        when(parameter.annotationIndex(Parameter.class)).thenReturn(1);
        when(parameter.type()).then(returnValue(Integer.TYPE));
        when(inputParameters.valueAt(1)).thenReturn(new Integer(1));

        assertTrue(underTest.resolve(parameter).isPresent());
        assertEquals(1, underTest.resolve(parameter).get().value());
    }

    @Test
    public void resolveVarArgsAsArrayIntoVarArgParameter() throws Exception {
        JavaMethodParameter parameter = mock(JavaMethodParameter.class);
        when(parameter.hasAnnotation(Parameter.class)).thenReturn(true);
        when(parameter.annotationIndex(Parameter.class)).thenReturn(1);
        when(parameter.isVarArg()).thenReturn(true);
        when(parameter.type()).then(returnValue(Integer[].class));
        when(inputParameters.length()).thenReturn(3);
        when(inputParameters.valueAt(1)).thenReturn(new Integer(1));
        when(inputParameters.valueAt(2)).thenReturn(new Integer(2));

        assertTrue(underTest.resolve(parameter).isPresent());
        Integer[] result = (Integer[]) underTest.resolve(parameter).get().value();
        assertTrue(Arrays.deepEquals(result, new Integer[]{1, 2}));
    }

    private Answer<Object> returnValue(final Object value) {
        return new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return value;
            }
        };
    }
}