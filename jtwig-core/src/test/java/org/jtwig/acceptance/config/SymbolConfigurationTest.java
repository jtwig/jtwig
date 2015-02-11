package org.jtwig.acceptance.config;

import org.jtwig.AbstractJtwigTest;
import org.jtwig.parser.config.Symbols;
import org.jtwig.parser.config.TagSymbols;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class SymbolConfigurationTest extends AbstractJtwigTest {
    @Test
    public void symbolsShouldAdjustToTheDefinedOnes() throws Exception {
        theEnvironment().setSymbols(new Symbols() {
            @Override
            public String beginOutput() {
                return "[[";
            }

            @Override
            public String endOutput() {
                return "]]";
            }

            @Override
            public String beginTag() {
                return TagSymbols.DEFAULT.beginTag();
            }

            @Override
            public String endTag() {
                return TagSymbols.DEFAULT.endTag();
            }

            @Override
            public String beginComment() {
                return TagSymbols.DEFAULT.beginComment();
            }

            @Override
            public String endComment() {
                return TagSymbols.DEFAULT.endComment();
            }
        });

        withResource("[[ 'two' ]]");

        assertThat(theResult(), equalTo("two"));
    }
}
