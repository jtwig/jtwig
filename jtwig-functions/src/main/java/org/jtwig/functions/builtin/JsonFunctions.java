package org.jtwig.functions.builtin;

import org.jtwig.functions.annotations.JtwigFunction;
import org.jtwig.functions.annotations.Parameter;
import org.jtwig.functions.config.JsonConfiguration;

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
