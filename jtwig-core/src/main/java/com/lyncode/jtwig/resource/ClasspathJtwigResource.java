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

public class ClasspathJtwigResource implements JtwigResource {
    private String resource;

    public ClasspathJtwigResource(String resource) {
        this.resource = resource.startsWith(File.separator) ? resource.substring(1) : resource;
    }

    @Override
    public InputStream retrieve() throws ResourceException {
        return this.getClass().getClassLoader().getResourceAsStream(this.resource);
    }

    @Override
    public JtwigResource resolve(String relativePath) throws ResourceException {
        File relativeFile = new File(new File(resource).getParent(), relativePath);
        return new ClasspathJtwigResource(relativeFile.getPath());
    }
}
