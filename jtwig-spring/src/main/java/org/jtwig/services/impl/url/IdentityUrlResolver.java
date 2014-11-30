package org.jtwig.services.impl.url;

import org.jtwig.services.api.url.ResourceUrlResolver;

public class IdentityUrlResolver implements ResourceUrlResolver {
    public static IdentityUrlResolver INSTANCE = new IdentityUrlResolver();

    private IdentityUrlResolver() {}

    @Override
    public String resolve(String prefix, String url, String suffix) {
        return prefix + url + suffix;
    }
}
