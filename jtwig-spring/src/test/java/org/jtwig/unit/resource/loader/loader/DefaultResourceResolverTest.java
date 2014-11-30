package org.jtwig.unit.resource.loader.loader;

import org.jtwig.resource.ClasspathJtwigResource;
import org.jtwig.resource.FileJtwigResource;
import org.jtwig.resource.JtwigResource;
import org.jtwig.resource.WebJtwigResource;
import org.jtwig.resource.loader.DefaultResourceResolver;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class DefaultResourceResolverTest {
    private final MockServletContext servletContext = new MockServletContext();
    private DefaultResourceResolver underTest = new DefaultResourceResolver(servletContext);

    @Test
    public void ifStartsWithClasspathMustReturnClasspathJtwigResource() throws Exception {
        JtwigResource resolve = underTest.resolve("classpath:/");
        assertThat(resolve, instanceOf(ClasspathJtwigResource.class));
    }
    @Test
    public void ifStartsWithFileMustReturnFileJtwigResource() throws Exception {
        JtwigResource resolve = underTest.resolve("file://");
        assertThat(resolve, instanceOf(FileJtwigResource.class));
    }
    @Test
    public void byDefaultUsesWebJtwigResource() throws Exception {
        JtwigResource resolve = underTest.resolve("/");
        assertThat(resolve, instanceOf(WebJtwigResource.class));
    }
}
