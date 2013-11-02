package com.lyncode.jtwig.mvc;

import com.lyncode.jtwig.api.AssetResolver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class DefaultAssetResolver implements AssetResolver {
    @Autowired
    JtwigViewResolver viewResolver;
    @Autowired
    HttpServletRequest req;

    private String basePath = "";


    public String getBasePath() {
        return basePath;
    }


    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }


    @Override
    public String getPath(String resource) {
        String path = this.getBasePath();
        if (path == null) path = "";
        if (viewResolver.hasTheme()) {
            if (!path.endsWith("/")) {
                path += "/";
                path += viewResolver.getTheme();
            }
        }
        if (!path.endsWith("/"))
            path += "/";

        if (resource.startsWith("/"))
            resource = resource.substring(1);

        path = path + resource;
        if (path.startsWith("/"))
            path = path.substring(1);
        return req.getContextPath() + "/" + path;
    }

}
