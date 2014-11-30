package org.jtwig.functions.json;

import com.google.common.base.Function;

public interface JsonMapperProvider {
    String className ();
    Function<Object, String> jsonMapper ();
}
