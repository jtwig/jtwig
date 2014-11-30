package org.jtwig.functions.json.mapper;

import com.google.common.base.Function;
import com.google.gson.Gson;

import javax.annotation.Nullable;

public class GsonJsonMapper implements Function<Object, String> {
    private static Gson gson = new Gson();

    @Nullable
    @Override
    public String apply(@Nullable Object input) {
        return gson.toJson(input);
    }
}
