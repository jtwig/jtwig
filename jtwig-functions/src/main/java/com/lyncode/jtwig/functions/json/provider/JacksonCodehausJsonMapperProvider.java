package com.lyncode.jtwig.functions.json.provider;

import com.google.common.base.Function;
import com.lyncode.jtwig.functions.json.JsonMapperProvider;
import com.lyncode.jtwig.functions.json.mapper.JacksonCodehausJsonMapper;


public class JacksonCodehausJsonMapperProvider implements JsonMapperProvider {
    @Override
    public String className() {
        return "org.codehaus.jackson.map.ObjectMapper";
    }

    @Override
    public Function<Object, String> jsonMapper() {
        return new JacksonCodehausJsonMapper();
    }
}
