package org.jtwig.unit.parser.model;

import org.jtwig.parser.model.JtwigPosition;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JtwigPositionTest {
    @Test
    public void rowAndColumn() throws Exception {
        JtwigPosition position = new JtwigPosition(null, 1, 2);

        assertEquals(1, position.getRow());
        assertEquals(2, position.getColumn());
    }
}
