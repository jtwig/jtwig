package org.jtwig.unit.addons.filter;

import org.jtwig.addons.filter.FilterAddon;
import org.jtwig.parser.config.ParserConfiguration;
import org.jtwig.resource.JtwigResource;
import org.junit.Test;
import org.parboiled.Parboiled;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class FilterAddonTest {
    private final JtwigResource jtwigResource = mock(JtwigResource.class);
    private FilterAddon underTest = Parboiled.createParser(FilterAddon.class, jtwigResource, new ParserConfiguration());

    @Test
    public void instanceMustBeNull() throws Exception {
        assertNull(underTest.instance());
    }

    @Test
    public void startRuleNotNull() throws Exception {
        assertNotNull(underTest.startRule());
    }
}
