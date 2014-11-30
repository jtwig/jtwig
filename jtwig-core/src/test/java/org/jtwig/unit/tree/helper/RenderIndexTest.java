package org.jtwig.unit.tree.helper;

import org.jtwig.render.stream.RenderIndex;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.jtwig.render.stream.RenderIndex.newIndex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by rsilva on 3/26/14.
 */
public class RenderIndexTest {

    HashMap<RenderIndex, String> underTest = new HashMap<>();

    @Test
    public void indexesAreImmutable() throws Exception {
        underTest.put(newIndex(), "a");
        underTest.put(newIndex(), "b");

        assertEquals(newIndex().hashCode(), newIndex().hashCode());
        assertThat(underTest.get(newIndex()), equalTo("b"));
    }
}
