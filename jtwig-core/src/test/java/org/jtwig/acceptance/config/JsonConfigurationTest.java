package org.jtwig.acceptance.config;

import com.google.common.base.Function;
import org.jtwig.AbstractJtwigTest;
import org.junit.Test;

import javax.annotation.Nullable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class JsonConfigurationTest extends AbstractJtwigTest {
    @Test
    public void changeJsonMapperConfigurationShouldAffectTheJsonEncodeFunction() throws Exception {
        theEnvironment().setJsonMapper(new Function<Object, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Object input) {
                return "ohoh";
            }
        });

        withResource("{{ json_encode 'three' }}");

        assertThat(theResult(), equalTo("ohoh"));
    }
}
