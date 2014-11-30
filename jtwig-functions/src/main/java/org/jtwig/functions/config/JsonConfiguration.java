package org.jtwig.functions.config;

import com.google.common.base.Function;
import org.jtwig.functions.json.DefaultJsonMapper;

public class JsonConfiguration {
    private Function<Object, String> jsonMapper = new DefaultJsonMapper();

    public JsonConfiguration jsonMapper (Function<Object, String> mapper) {
        this.jsonMapper = mapper;
        return this;
    }

    public Function<Object, String> jsonMapper() {
        return jsonMapper;
    }
}
