package org.jtwig.unit.render.config;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import org.jtwig.Environment;
import org.jtwig.addons.Addon;
import org.jtwig.addons.AddonModel;
import org.jtwig.addons.concurrent.ConcurrentAddon;
import org.jtwig.addons.filter.FilterAddon;
import org.jtwig.addons.spaceless.SpacelessAddon;
import org.jtwig.loader.Loader;
import org.jtwig.parser.config.AddonParserList;
import static org.junit.Assert.assertThat;
import org.junit.Test;

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
        public Sample(Loader.Resource resource, Environment env) {
            super(resource, env);
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
