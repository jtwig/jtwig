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

import java.io.File;
import java.io.InputStream;

import static com.lyncode.jtwig.util.FilePath.parentOf;

public class ClasspathJtwigResource implements JtwigResource {
    private static final String PREFIX = "classpath:";
    private String resource;

    public ClasspathJtwigResource(String resource) {
        if (resource.startsWith(PREFIX))
            resource = resource.substring(PREFIX.length());
        this.resource = resource.startsWith(File.separator) ? resource.substring(1) : resource;
    }

    @Override
    public boolean exists() {
        return this.getClass().getClassLoader().getResource(this.resource) != null;
    }

    @Override
    public InputStream retrieve() throws ResourceException {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(this.resource);
        if (resourceAsStream == null)
            throw new ResourceException("Resource '"+ this.resource +"' not found");
        return resourceAsStream;
    }

    @Override
    public JtwigResource resolve(String relativePath) throws ResourceException {
        return new ClasspathJtwigResource(parentOf(resource).append(relativePath).normalize());
    }

    @Override
    public String path() {
        return resource;
    }
    
    @Override
    public String toString() {
        return resource;
    }
}
