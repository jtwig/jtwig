package com.lyncode.jtwig.resource.loader;

import com.lyncode.jtwig.resource.ClasspathJtwigResource;
import com.lyncode.jtwig.resource.FileJtwigResource;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.resource.WebJtwigResource;

import javax.servlet.ServletContext;

public class DefaultResourceResolver implements JtwigResourceResolver {
    private final ServletContext servletContext;

    public DefaultResourceResolver(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public JtwigResource resolve(String viewUrl) {
        if (viewUrl.startsWith("classpath:"))
            return new ClasspathJtwigResource(viewUrl);
        else if (viewUrl.startsWith("file://"))
            return new FileJtwigResource(viewUrl);
        else
            return new WebJtwigResource(servletContext, viewUrl);
    }
}
