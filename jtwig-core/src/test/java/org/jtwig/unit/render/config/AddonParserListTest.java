package org.jtwig.unit.render.config;

import org.jtwig.addons.Addon;
import org.jtwig.addons.AddonModel;
import org.jtwig.addons.concurrent.ConcurrentAddon;
import org.jtwig.addons.filter.FilterAddon;
import org.jtwig.addons.spaceless.SpacelessAddon;
import org.jtwig.parser.config.AddonParserList;
import org.jtwig.parser.config.ParserConfiguration;
import org.jtwig.resource.JtwigResource;
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
