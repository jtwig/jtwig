package org.jtwig.acceptance.config;

import com.google.common.base.Function;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.jtwig.functions.config.JsonConfiguration;
import org.junit.Test;

import javax.annotation.Nullable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.jtwig.configuration.JtwigConfigurationBuilder.newConfiguration;

public class JsonConfigurationTest {

    @Test
    public void changeJsonMapperConfigurationShouldAffectTheJsonEncodeFunction() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ json_encode 'three' }}", newConfiguration()
                .withJsonConfiguration(new JsonConfiguration().jsonMapper(new Function<Object, String>() {
                    @Nullable
                    @Override
                    public String apply(@Nullable Object input) {
                        return "ohoh";
                    }
                }))
                .build())
            .render(model);

        assertThat(result, is(equalTo("ohoh")));
    }
}
