package com.lyncode.jtwig.functions.json;

public class JsonMappingException extends RuntimeException {
    public JsonMappingException(Throwable cause) {
        super(cause);
    }

    public JsonMappingException(String message) {
        super(message);
    }
}
