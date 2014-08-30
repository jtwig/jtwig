package com.lyncode.jtwig.unit.parser.config;

import com.lyncode.jtwig.parser.config.TagSymbols;
import org.hamcrest.core.AllOf;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;

public class TagSymbolsTest {
    @Test
    public void values() throws Exception {
        assertThat(asList(TagSymbols.values()), AllOf.allOf(
                hasItem(equalTo(TagSymbols.JAVASCRIPT_COLLISION_FREE))
        ));
    }

    @Test
    public void valueOf() throws Exception {
        assertEquals(TagSymbols.DEFAULT, TagSymbols.valueOf("DEFAULT"));
    }
}