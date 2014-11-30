package org.jtwig.resource.loader;

import org.jtwig.resource.ClasspathJtwigResource;
import org.jtwig.resource.FileJtwigResource;
import org.jtwig.resource.JtwigResource;
import org.jtwig.resource.WebJtwigResource;

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
