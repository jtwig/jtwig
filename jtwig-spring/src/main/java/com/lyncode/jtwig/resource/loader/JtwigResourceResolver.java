package com.lyncode.jtwig.resource.loader;

import com.lyncode.jtwig.resource.JtwigResource;

public interface JtwigResourceResolver {
    JtwigResource resolve(String viewUrl);
}
