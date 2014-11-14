/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.jtwig.resource;

import com.lyncode.jtwig.exception.ResourceException;

import javax.servlet.ServletContext;
import java.io.InputStream;

import static com.lyncode.jtwig.util.FilePath.parentOf;
import java.net.MalformedURLException;

public class WebJtwigResource implements JtwigResource {
    private final ServletContext servletContext;
    private final String url;

    public WebJtwigResource(ServletContext servletContext, String url) {
        this.servletContext = servletContext;
        this.url = url;
    }

    @Override
    public boolean exists() {
        try {
            return servletContext.getResource(url) != null;
        } catch (MalformedURLException ex) {}
        return false;
    }

    @Override
    public InputStream retrieve() throws ResourceException {
        InputStream resourceAsStream = servletContext.getResourceAsStream(url);
        if (resourceAsStream == null) throw new ResourceException("Resource "+url+" not found");
        return resourceAsStream;
    }

    @Override
    public JtwigResource resolve(String relativePath) throws ResourceException {
        return new WebJtwigResource(servletContext, parentOf(url).append(relativePath).toString());
    }
}
