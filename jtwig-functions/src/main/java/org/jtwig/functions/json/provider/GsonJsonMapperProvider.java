package org.jtwig.functions.json.provider;

import com.google.common.base.Function;
import org.jtwig.functions.json.JsonMapperProvider;
import org.jtwig.functions.json.mapper.GsonJsonMapper;

public class GsonJsonMapperProvider implements JsonMapperProvider {
    @Override
    public String className() {
        return "com.google.gson.Gson";
    }

    @Override
    public Function<Object, String> jsonMapper() {
        return new GsonJsonMapper();
    }
}
