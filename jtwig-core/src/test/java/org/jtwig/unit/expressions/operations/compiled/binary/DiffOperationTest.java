package org.jtwig.unit.expressions.operations.compiled.binary;

import org.jtwig.expressions.operations.binary.DiffOperation;
import org.jtwig.parser.model.JtwigPosition;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class DiffOperationTest {
    private final JtwigPosition position = mock(JtwigPosition.class);
    private DiffOperation underTest = new DiffOperation();

    @Test
    public void apply() throws Exception {
        assertEquals(false, underTest.apply(position, 1, 1));
        assertEquals(true, underTest.apply(position, 1, 2));
        Object reference = new Object();
        assertEquals(false, underTest.apply(position, reference, reference));
    }
}
