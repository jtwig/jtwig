package com.lyncode.jtwig.functions.json.mapper;

import com.google.common.base.Function;
import com.lyncode.jtwig.functions.json.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.annotation.Nullable;
import java.io.IOException;

public class JacksonCodehausJsonMapper implements Function<Object, String> {
    private static ObjectMapper mapper = new ObjectMapper();

    @Nullable
    @Override
    public String apply(@Nullable Object input) {
        try {
            return mapper.writeValueAsString(input);
        } catch (IOException e) {
            throw new JsonMappingException(e);
        }
    }
}
