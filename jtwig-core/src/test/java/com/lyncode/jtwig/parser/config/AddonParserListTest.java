package com.lyncode.jtwig.parser.config;

import com.lyncode.jtwig.addons.Addon;
import com.lyncode.jtwig.addons.AddonModel;
import com.lyncode.jtwig.addons.concurrent.ConcurrentAddon;
import com.lyncode.jtwig.addons.filter.FilterAddon;
import com.lyncode.jtwig.addons.spaceless.SpacelessAddon;
import com.lyncode.jtwig.resource.JtwigResource;
import org.junit.Test;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

public class AddonParserListTest {
    @Test
    public void addonParsersContainsByDefault() throws Exception {
        AddonParserList underTest = new AddonParserList();

        assertThat(underTest.list(), hasItem(SpacelessAddon.class));
        assertThat(underTest.list(), hasItem(FilterAddon.class));
        assertThat(underTest.list(), hasItem(ConcurrentAddon.class));
    }

    @Test
    public void addonParserAdded() throws Exception {
        AddonParserList underTest = new AddonParserList();
        underTest.withAddon(Sample.class);

        assertThat(underTest.list(), hasItem(SpacelessAddon.class));
        assertThat(underTest.list(), hasItem(FilterAddon.class));
        assertThat(underTest.list(), hasItem(ConcurrentAddon.class));
        assertThat(underTest.list(), hasItem(Sample.class));
    }

    public static class Sample extends Addon {
        public Sample(JtwigResource resource, ParserConfiguration configuration) {
            super(resource, configuration);
        }

        @Override
        public AddonModel instance() {
            return null;
        }

        @Override
        public String beginKeyword() {
            return null;
        }

        @Override
        public String endKeyword() {
            return null;
        }
    }
}