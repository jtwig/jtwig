package org.jtwig.expressions.operations.binary;

import org.hamcrest.core.Is;
import org.jtwig.parser.model.JtwigPosition;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class NotInOperationTest {
    private NotInOperation underTest = new NotInOperation();
    private JtwigPosition jtwigPosition;

    @Test
    public void applyTest() throws Exception {
        jtwigPosition = mock(JtwigPosition.class);
        Object result = underTest.apply(jtwigPosition, 1, asList(2, 3));

        assertThat(result, Is.<Object>is(true));
    }
}
