package com.lyncode.jtwig.unit.expressions.operations.binary;

import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.expressions.operations.binary.IntDivOperation;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import org.junit.Test;

public class IntDivOperationTest {
    private static final JtwigPosition POSITION = new JtwigPosition(null, 1, 1);
    private IntDivOperation underTest = new IntDivOperation();

    @Test(expected = CalculateException.class)
    public void zeroDivision() throws Exception {
        underTest.apply(POSITION, 1, null);
    }
}