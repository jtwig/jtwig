package org.jtwig.functions.json.provider;

import com.google.common.base.Function;
import org.jtwig.functions.json.JsonMapperProvider;
import org.jtwig.functions.json.mapper.JacksonFasterXmlJsonMapper;

public class JacksonFasterXmlJsonMapperProvider implements JsonMapperProvider {
    @Override
    public String className() {
        return "com.fasterxml.jackson.databind.ObjectMapper";
    }

    @Override
    public Function<Object, String> jsonMapper() {
        return new JacksonFasterXmlJsonMapper();
    }
}
