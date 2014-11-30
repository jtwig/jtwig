package org.jtwig.unit.expressions.operations.binary;

import org.jtwig.exception.CalculateException;
import org.jtwig.expressions.operations.binary.IntDivOperation;
import org.jtwig.parser.model.JtwigPosition;
import org.junit.Test;

public class IntDivOperationTest {
    private static final JtwigPosition POSITION = new JtwigPosition(null, 1, 1);
    private IntDivOperation underTest = new IntDivOperation();

    @Test(expected = CalculateException.class)
    public void zeroDivision() throws Exception {
        underTest.apply(POSITION, 1, null);
    }
}
