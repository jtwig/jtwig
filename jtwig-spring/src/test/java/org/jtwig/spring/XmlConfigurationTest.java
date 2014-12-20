package org.jtwig.spring;

import com.google.common.base.Function;
import org.jtwig.addons.Addon;
import org.jtwig.addons.tag.TagAddon;
import org.jtwig.functions.repository.api.FunctionRepository;
import org.jtwig.parser.config.ParserConfiguration;
import org.jtwig.parser.config.TagSymbols;
import org.jtwig.resource.JtwigResource;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nullable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import org.jtwig.cache.impl.ExecutionCache;
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
        underTest = new XmlConfiguration(functionRepository, new ExecutionCache());
    }

    @Test
    public void setStrictModeShouldSetTheRenderStrictMode() throws Exception {
        underTest.setRenderStrictMode(true);

        assertThat(underTest.render().strictMode(), equalTo(true));
    }

    @Test
    public void setJsonMapperShouldSetTheRenderJsonMapper() throws Exception {
        Function<Object, String> jsonMapper = new Function<Object, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Object input) {
                return null;
            }
        };
        underTest.setJsonMapper(jsonMapper);

        assertThat(underTest.render().jsonConfiguration().jsonMapper(), equalTo(jsonMapper));
    }

    @Test
    public void setCharsetShouldSetTheRenderCharset() throws Exception {
        underTest.setCharset("UTF-8");

        assertThat(underTest.render().charset().name(), equalTo("UTF-8"));
    }

    @Test
    public void logNonStrictModeShouldSetTheRenderLogNonStrictMode() throws Exception {
        underTest.setLogNonStrictMode(true);

        assertThat(underTest.render().logNonStrictMode(), equalTo(true));
    }

    @Test
    public void setExtraFunctionContainersShouldAddToTheFunctionRepository() throws Exception {
        Object container = new Object();
        underTest.setExtraFunctionContainers(new Object[] {container});

        verify(functionRepository).include(eq(container));
    }

    @Test
    public void setTagSymbols() throws Exception {
        underTest.setTagSymbols("JAVASCRIPT_COLLISION_FREE");

        assertEquals(TagSymbols.JAVASCRIPT_COLLISION_FREE, underTest.parse().symbols());
    }

    @Test
    public void setExtraSyntaticAddons() throws Exception {
        Class<? extends Addon> testAddonClass = TestAddon.class;
        underTest.setExtraSyntaticAddons(new Class[]{ testAddonClass });

        assertTrue(underTest.parse().addons().list().contains(testAddonClass));
    }

    public static class TestAddon extends TagAddon {

        public TestAddon(JtwigResource resource, ParserConfiguration configuration) {
            super(resource, configuration);
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