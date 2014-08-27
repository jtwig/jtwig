package com.lyncode.jtwig.services.impl.url;

import com.lyncode.jtwig.services.api.url.ResourceUrlResolver;

public class ThemedResourceUrlResolver implements ResourceUrlResolver {
    private final String theme;

    public ThemedResourceUrlResolver(String theme) {
        this.theme = theme;
    }

    @Override
    public String resolve(String prefix, String url, String suffix) {
        return prefix + resolve(url) + suffix;
    }

    private String resolve(String url) {
        if (url.startsWith("/"))
            return "/" + theme + url;
        else
            return theme + "/" + url;
    }
}
