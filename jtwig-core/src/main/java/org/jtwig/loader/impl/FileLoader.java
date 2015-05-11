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

import org.apache.commons.lang3.StringUtils;
import org.jtwig.exception.ResourceException;
import org.jtwig.loader.Loader;
import org.jtwig.util.LoaderUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileLoader extends Loader {
    private static final String PREFIX = "file:";
    private final File[] paths;
    
    public FileLoader(final String[] paths) {
        if (paths == null || paths.length == 0) {
            throw new IllegalArgumentException("FileLoader requires at least one path.");
        }
        List<File> files = new ArrayList<>();
        for (String path : paths) {
            if (path.startsWith(PREFIX)) {
                path = path.substring(PREFIX.length());
            }
            files.add(new File(path));
        }
        this.paths = files.toArray(new File[]{});
    }
    public FileLoader(final File[] paths) {
        if (paths == null || paths.length == 0) {
            throw new IllegalArgumentException("FileLoader requires at least one path.");
        }
        this.paths = paths;
    }

    @Override
    public boolean exists(String name) throws ResourceException {
        name = normalizeName(name);
        validateName(name);
        
        for (File path : paths) {
            if (new File(path, name).exists()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public FileResource get(final String name) throws ResourceException {
        if (!exists(name)) {
            return null;
        }
        for (File path : paths) {
            File f = new File(path, name);
            if (f.exists()) {
                return new FileResource(path, f);
            }
        }
        return null;
    }
    
    //~ Helpers ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    protected String normalizeName(String name) {
        return name.replaceAll("/+", "/");
    }
    protected void validateName(String name) throws ResourceException {
        name = StringUtils.strip(name, "/");
        String[] parts = StringUtils.split(name, "/");
        int level = 0;
        for (String part : parts) {
            if ("..".equals(part)) {
                --level;
            } else if (".".equals(part)) {
                ++level;
            }
            
            if (level < 0) {
                throw new ResourceException("Cannot load template outside allowed directories.");
            }
        }
    }
    
    public static class FileResource extends Resource {
        private final File base;
        private final File absolute;
        
        public FileResource(final File base, final File absolute) {
            this.base = base;
            this.absolute = absolute;
        }

        @Override
        public String getCacheKey() {
            return LoaderUtil.getCacheKey(relativePath());
        }

        @Override
        public String canonicalPath() {
            try {
                return StringUtils.strip(absolute.getCanonicalPath(), "/");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public String relativePath() {
            try {
                String canonical = canonicalPath();
                String base = StringUtils.strip(this.base.getCanonicalPath(), "/");
                return StringUtils.removeStart(canonical, base);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public InputStream source() throws ResourceException {
            try {
                return new FileInputStream(absolute);
            } catch (FileNotFoundException ex) {
                throw new ResourceException(ex);
            }
        }

        @Override
        public String resolve(String relative) throws ResourceException {
            try {
                File f = new File(absolute.getParentFile(), relative);
                String canonical = StringUtils.strip(f.getCanonicalPath(), "/");
                String base = StringUtils.strip(this.base.getCanonicalPath(), "/");
                return StringUtils.removeStart(canonical, base);
            } catch (IOException ex) {
                throw new ResourceException(ex);
            }
        }
        
    }
    
}