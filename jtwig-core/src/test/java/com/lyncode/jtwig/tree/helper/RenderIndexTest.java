package com.lyncode.jtwig.tree.helper;

import junit.framework.TestCase;

import java.util.HashMap;

import static com.lyncode.jtwig.tree.helper.RenderIndex.newIndex;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by rsilva on 3/26/14.
 */
public class RenderIndexTest extends TestCase {

    HashMap<RenderIndex, String> underTest = new HashMap<>();

    public void testName() throws Exception {
        underTest.put(newIndex(), "a");
        underTest.put(newIndex(), "b");

        assertEquals(newIndex().hashCode(), newIndex().hashCode());
        assertThat(underTest.get(newIndex()), equalTo("b"));
    }
}
