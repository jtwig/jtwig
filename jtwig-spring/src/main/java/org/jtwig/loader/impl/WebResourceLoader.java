/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.loader.impl;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import javax.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.jtwig.exception.ResourceException;
import org.jtwig.loader.Loader;
import org.jtwig.util.LoaderUtil;

public class WebResourceLoader extends Loader {
    private final ServletContext servletContext;
    
    public WebResourceLoader(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public boolean exists(final String name) throws ResourceException {
        try {
            return servletContext.getResource(name) != null;
        } catch (MalformedURLException ex) {}
        return false;
    }

    @Override
    public Resource get(final String name) throws ResourceException {
        if (!exists(name)) {
            return null;
        }
        
        return new WebResource(servletContext, name);
    }
    
    public static class WebResource extends Resource {
        private final ServletContext context;
        private final String name;
        
        public WebResource(final ServletContext context, final String name) {
            this.context = context;
            this.name = name;
        }

        @Override
        public String getCacheKey() {
            return LoaderUtil.getCacheKey(name);
        }

        @Override
        public String canonicalPath() {
            try {
                return context.getResource(name).getPath();
            } catch (MalformedURLException ex) {}
            return null;
        }

        @Override
        public String relativePath() {
            return name;
        }

        @Override
        public InputStream source() throws ResourceException {
            InputStream resourceAsStream = context.getResourceAsStream(name);
            if (resourceAsStream == null) {
                throw new ResourceException("Resource "+name+" not found");
            }
            return resourceAsStream;
        }

        @Override
        public String resolve(final String relative) throws ResourceException {
            String path = new File(new File("/"+relativePath()).getParentFile(), relative).toURI().normalize().getPath();
            return StringUtils.strip(path, "/");
        }
        
    }
    
}