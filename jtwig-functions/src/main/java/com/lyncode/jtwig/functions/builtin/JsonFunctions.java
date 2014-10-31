package com.lyncode.jtwig.functions.builtin;

import com.lyncode.jtwig.functions.annotations.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.Parameter;
import com.lyncode.jtwig.functions.config.JsonConfiguration;

import java.io.IOException;

public class JsonFunctions {
    private final JsonConfiguration jsonConfiguration;

    public JsonFunctions(JsonConfiguration jsonConfiguration) {
        this.jsonConfiguration = jsonConfiguration;
    }

    @JtwigFunction(name = "json_encode")
    public String jsonEncode (@Parameter Object input) throws IOException {
        return jsonConfiguration.jsonMapper().apply(input);
    }
}
