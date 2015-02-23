package org.jtwig.acceptance.config;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.jtwig.parser.config.Symbols;
import org.jtwig.parser.config.TagSymbols;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.jtwig.configuration.JtwigConfigurationBuilder.newConfiguration;

public class SymbolConfigurationTest {
    @Test
    public void symbolsShouldAdjustToTheDefinedOnes() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("[[ 'two' ]]", newConfiguration()
                .withSymbols(new Symbols() {
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
                })
                .build())
            .render(model);

        assertThat(result, equalTo("two"));
    }
}
