package com.lyncode.jtwig.parser.config;

import com.lyncode.jtwig.addons.Addon;
import com.lyncode.jtwig.addons.AddonParser;
import com.lyncode.jtwig.addons.concurrent.ConcurrentParser;
import com.lyncode.jtwig.addons.filter.FilterParser;
import com.lyncode.jtwig.addons.spaceless.SpacelessParser;
import com.lyncode.jtwig.resource.JtwigResource;
import org.junit.Test;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

public class AddonParserListTest {
    @Test
    public void addonParsersContainsByDefault() throws Exception {
        AddonParserList underTest = new AddonParserList();

        assertThat(underTest.list(), hasItem(SpacelessParser.class));
        assertThat(underTest.list(), hasItem(FilterParser.class));
        assertThat(underTest.list(), hasItem(ConcurrentParser.class));
    }

    @Test
    public void addonParserAdded() throws Exception {
        AddonParserList underTest = new AddonParserList();
        underTest.withAddon(Sample.class);

        assertThat(underTest.list(), hasItem(SpacelessParser.class));
        assertThat(underTest.list(), hasItem(FilterParser.class));
        assertThat(underTest.list(), hasItem(ConcurrentParser.class));
        assertThat(underTest.list(), hasItem(Sample.class));
    }

    public static class Sample extends AddonParser {
        public Sample(JtwigResource resource, ParserConfiguration configuration) {
            super(resource, configuration);
        }

        @Override
        public Addon instance() {
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