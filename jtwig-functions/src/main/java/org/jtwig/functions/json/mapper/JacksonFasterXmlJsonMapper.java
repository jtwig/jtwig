package org.jtwig.functions.json.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import org.jtwig.functions.json.JsonMappingException;

import javax.annotation.Nullable;
import java.io.IOException;

public class JacksonFasterXmlJsonMapper implements Function<Object, String> {
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
