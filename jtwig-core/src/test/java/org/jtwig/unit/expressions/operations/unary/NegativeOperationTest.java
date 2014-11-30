package org.jtwig.unit.expressions.operations.unary;

import org.jtwig.expressions.operations.unary.NegativeOperation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NegativeOperationTest {
    private NegativeOperation underTest = new NegativeOperation();

    @Test
    public void usingDoubles() throws Exception {
        assertEquals(1.0D, underTest.apply(-1.0D));
    }
}
