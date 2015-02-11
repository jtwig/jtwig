package org.jtwig.spring;

import com.google.common.base.Function;
import org.jtwig.addons.Addon;
import org.jtwig.addons.tag.TagAddon;
import org.jtwig.functions.repository.api.FunctionRepository;
import org.jtwig.parser.config.TagSymbols;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nullable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import org.jtwig.Environment;
import org.jtwig.cache.impl.ExecutionCache;
import org.jtwig.loader.Loader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class XmlConfigurationTest {
    private XmlConfiguration underTest;
    private FunctionRepository functionRepository;

    @Before
    public void setUp() throws Exception {
        functionRepository = mock(FunctionRepository.class);
        underTest = new XmlConfiguration();
        underTest.setFunctionRepository(functionRepository);
    }

    @Test
    public void setExtraFunctionContainersShouldAddToTheFunctionRepository() throws Exception {
        Object container = new Object();
        underTest.setExtraFunctionContainers(new Object[] {container});

        verify(functionRepository).include(eq(container));
    }

    @Test
    public void setSymbols() throws Exception {
        underTest.setSymbols("JAVASCRIPT_COLLISION_FREE");

        assertEquals(TagSymbols.JAVASCRIPT_COLLISION_FREE, underTest.getSymbols());
    }

    @Test
    public void setExtraSyntaticAddons() throws Exception {
        Class<? extends Addon> testAddonClass = TestAddon.class;
        underTest.setExtraSyntaticAddons(new Class[]{ testAddonClass });

        assertTrue(underTest.getAddonParserList().list().contains(testAddonClass));
    }

    public static class TestAddon extends TagAddon {

        public TestAddon(Loader.Resource resource, Environment env) {
            super(resource, env);
        }

        @Override
        protected Function<String, String> transformation() {
            return null;
        }

        @Override
        protected String keyword() {
            return null;
        }
    }
}