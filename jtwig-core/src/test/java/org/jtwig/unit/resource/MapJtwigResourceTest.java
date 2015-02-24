package org.jtwig.unit.resource;

import org.jtwig.resource.MapJtwigResource;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vitali Carbivnicii
 * Date: 2014-12-30
 * Time: 11:19
 */
public class MapJtwigResourceTest {

    private MapJtwigResource underTest;

    @Before
    public void setUp() throws Exception {

        Map<String, String> resources = new HashMap<>();
        resources.put("test1", "test1");
        resources.put("test2", "test2");

        underTest = new MapJtwigResource("test1", resources);
    }

    @Test
    public void testRetrieve() throws Exception {
        assertNotNull(underTest.retrieve());
    }

    @Test
    public void testResolve() throws Exception {
        assertNotNull(underTest.resolve("test2").retrieve());
    }

    @Test
    public void ensureExistsMethodWorksProperly() throws Exception {
        assertTrue(underTest.exists());
        assertFalse(underTest.resolve("invalid.twig").exists());
    }
}
