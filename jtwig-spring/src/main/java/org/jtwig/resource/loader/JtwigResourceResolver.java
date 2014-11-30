package org.jtwig.resource.loader;

import org.jtwig.resource.JtwigResource;

public interface JtwigResourceResolver {
    JtwigResource resolve(String viewUrl);
}
