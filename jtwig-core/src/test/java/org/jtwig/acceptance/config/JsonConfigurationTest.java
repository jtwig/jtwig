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
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.jtwig.extension.core.CoreJtwigExtension;

public class JsonConfigurationTest {

    @Test
    public void changeJsonMapperConfigurationShouldAffectTheJsonEncodeFunction() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        JtwigConfiguration config = JtwigConfigurationBuilder.newConfiguration()
                .withJsonConfiguration(new JsonConfiguration().jsonMapper(new Function<Object, String>() {
                    @Nullable
                    @Override
                    public String apply(@Nullable Object input) {
                        return "ohoh";
                    }
                }))
                .build();
        config.getExtensions().addExtension(new CoreJtwigExtension(config));
        
        String result = JtwigTemplate
            .inlineTemplate("{{ 'three'|json_encode }}", config)
            .render(model);

        assertThat(result, is(equalTo("ohoh")));
    }
}
