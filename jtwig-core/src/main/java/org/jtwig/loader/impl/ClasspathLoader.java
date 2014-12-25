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
import org.apache.commons.lang3.StringUtils;
import org.jtwig.exception.ResourceException;
import org.jtwig.loader.Loader;
import org.jtwig.util.LoaderUtil;

public class ClasspathLoader extends Loader {

    @Override
    public boolean exists(final String name) {
        return this.getClass().getClassLoader().getResource(normalizeName(name)) != null;
    }

    @Override
    public ClasspathResource get(final String name) throws ResourceException {
        if (!exists(name)) {
            return null;
        }
        
        return new ClasspathResource(normalizeName(name));
    }
    
    protected String normalizeName(String name) {
        if (StringUtils.startsWithIgnoreCase(name, "classpath:")) {
            name = StringUtils.removeStartIgnoreCase(name, "classpath:");
        }
        name = "/"+StringUtils.strip(name, "/\\");
        String path = new File(name).toURI().normalize().getRawPath();
        return StringUtils.stripStart(path, "/\\");
    }
    
    public static class ClasspathResource extends Resource {
        private final String name;
        
        public ClasspathResource(final String name) {
            this.name = name;
        }

        @Override
        public String getCacheKey() {
            return LoaderUtil.getCacheKey(name);
        }

        @Override
        public String canonicalPath() {
            return this.getClass().getClassLoader().getResource(name).toExternalForm();
        }

        @Override
        public String relativePath() {
            return StringUtils.strip(name, "/\\");
        }

        @Override
        public InputStream source() throws ResourceException {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(name);
            if (resourceAsStream == null) {
                throw new ResourceException("Resource '"+ name +"' not found");
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