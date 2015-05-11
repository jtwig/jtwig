package org.jtwig.functions.parameters.input;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.jtwig.functions.parameters.input.InputParameters.parameters;
import static org.junit.Assert.assertEquals;

public class InputParametersTest {
    @Test
    public void valueAtPositionReturnsElementAtGivenPosition() throws Exception {
        InputParameters parameters = parameters(1, 2);

        assertEquals(2, parameters.valueAt(1));
    }

    @Test
    public void lengthReturnsTheNumberOfArguments() throws Exception {
        assertThat(parameters("one").length(), equalTo(1));
    }
}
