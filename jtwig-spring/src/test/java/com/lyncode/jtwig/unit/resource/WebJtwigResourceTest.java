package com.lyncode.jtwig.unit.resource;

import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.resource.WebJtwigResource;
import org.junit.Test;

import javax.servlet.ServletContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WebJtwigResourceTest {
    private static final String URL = "file.test";
    private ServletContext servletContext = mock(ServletContext.class);
    private WebJtwigResource resource = new WebJtwigResource(servletContext, URL);

    @Test(expected = ResourceException.class)
    public void retrieveTest() throws Exception {
        try {
            resource.retrieve();
        } finally {
            verify(servletContext).getResourceAsStream(URL);
        }
    }

    @Test(expected = ResourceException.class)
    public void resolveRetrieveTest() throws Exception {
        try {
            resource.resolve("hello.test").retrieve();
        } finally {
            verify(servletContext).getResourceAsStream("hello.test");
        }
    }
}