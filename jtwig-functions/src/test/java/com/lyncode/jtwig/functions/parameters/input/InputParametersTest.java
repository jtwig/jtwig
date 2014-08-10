package com.lyncode.jtwig.functions.parameters.input;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InputParametersTest {
    @Test
    public void valueAtPositionReturnsElementAtGivenPosition() throws Exception {
        InputParameters parameters = InputParameters.parameters(1, 2);

        assertEquals(2, parameters.valueAt(1));
    }
}