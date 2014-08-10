package com.lyncode.jtwig.functions.parameters.convert.impl;

import com.lyncode.jtwig.functions.parameters.convert.api.ParameterConverter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObjectToStringConverterTest {
    private ParameterConverter underTest = new ObjectToStringConverter();

    @Test
    public void convertFromObjectToString() throws Exception {
        assertEquals("1", underTest.convert(1).get());
    }
}