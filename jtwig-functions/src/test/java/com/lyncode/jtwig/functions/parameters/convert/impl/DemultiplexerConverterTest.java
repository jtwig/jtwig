package com.lyncode.jtwig.functions.parameters.convert.impl;

import com.google.common.base.Optional;
import com.lyncode.jtwig.functions.parameters.convert.DemultiplexerConverter;
import com.lyncode.jtwig.functions.parameters.convert.api.ParameterConverter;
import org.junit.Test;

import static com.google.common.base.Optional.absent;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class DemultiplexerConverterTest {
    private static final String VALUE = "value";
    private static final Class<?> TO_TYPE = String.class;
    private ParameterConverter firstConverter = mock(ParameterConverter.class);
    private ParameterConverter secondConverter = mock(ParameterConverter.class);
    private DemultiplexerConverter underTest = new DemultiplexerConverter()
            .withConverter(String.class, firstConverter)
            .withConverter(String.class, secondConverter);

    @Test
    public void noDelegateCapableOfConvert() throws Exception {
        given(firstConverter.convert(anyObject())).willReturn(absent());
        given(secondConverter.convert(anyObject())).willReturn(absent());

        assertFalse(underTest.convert(VALUE, TO_TYPE).isPresent());
    }

    @Test
    public void ifFirstConverterHandlers() throws Exception {
        given(firstConverter.convert(anyObject())).willReturn(Optional.of("test"));
        given(secondConverter.convert(anyObject())).willReturn(absent());

        assertTrue(underTest.convert(VALUE, TO_TYPE).isPresent());
        verifyZeroInteractions(secondConverter);
    }

    @Test
    public void ifSecondConverterHandlers() throws Exception {
        given(firstConverter.convert(anyObject())).willReturn(absent());
        given(secondConverter.convert(anyObject())).willReturn(Optional.of("test"));

        assertTrue(underTest.convert(VALUE, TO_TYPE).isPresent());
    }
}