package com.lyncode.jtwig.functions.json;

import com.google.common.base.Function;
import com.lyncode.jtwig.functions.json.provider.GsonJsonMapperProvider;
import com.lyncode.jtwig.functions.json.provider.JacksonCodehausJsonMapperProvider;
import com.lyncode.jtwig.functions.json.provider.JacksonFasterXmlJsonMapperProvider;
import com.lyncode.jtwig.functions.util.ClassUtils;

import javax.annotation.Nullable;
import java.util.List;

import static java.util.Arrays.asList;

public class DefaultJsonMapper implements Function<Object, String> {
    private static List<JsonMapperProvider> providers = asList(
        new JacksonCodehausJsonMapperProvider(), new JacksonFasterXmlJsonMapperProvider(),
            new GsonJsonMapperProvider()
    );

    private Function<Object, String> implementation;

    public DefaultJsonMapper() {
        for (JsonMapperProvider provider : providers) {
            if (ClassUtils.classExists(DefaultJsonMapper.class.getClassLoader(), provider.className())) {
                implementation = provider.jsonMapper();
                return;
            }
        }
        implementation = new Function<Object, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Object input) {
                throw new JsonMappingException("No json mapper found in the project. Try to add jackson, jackson2 or gson to your classpath.");
            }
        };
    }

    @Nullable
    @Override
    public String apply(@Nullable Object input) {
        return implementation.apply(input);
    }
}
