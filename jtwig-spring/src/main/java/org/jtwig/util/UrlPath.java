package org.jtwig.util;

public class UrlPath {
    private static final String PATH_SEPARATOR = "/";
    private String url;

    public UrlPath append (String path) {
        if (url == null) url = path;
        else {
            if (url.endsWith(PATH_SEPARATOR)) {
                if (path.startsWith(PATH_SEPARATOR))
                    path = path.substring(1);
            } else {
                if (!path.startsWith(PATH_SEPARATOR))
                    path = PATH_SEPARATOR + path;
            }
            url += path;
        }
        return this;
    }

    @Override
    public String toString() {
        return url == null ? "" : url;
    }
}
