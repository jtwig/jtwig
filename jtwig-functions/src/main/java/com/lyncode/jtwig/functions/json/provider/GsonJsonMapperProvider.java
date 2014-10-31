package com.lyncode.jtwig.functions.json.provider;

import com.google.common.base.Function;
import com.lyncode.jtwig.functions.json.JsonMapperProvider;

public class GsonJsonMapperProvider implements JsonMapperProvider {
    @Override
    public String className() {
        return "com.google.gson.Gson";
    }

    @Override
    public Function<Object, String> jsonMapper() {
        return null;
    }
}
