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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileJtwigResource implements JtwigResource {
    private static final String PREFIX = "file://";
    private File file;

    public FileJtwigResource (String file) {
        if (file.startsWith(PREFIX))
            file = file.substring(PREFIX.length());
        this.file = new File(file);
    }

    public FileJtwigResource (File file) {
        this.file = file;
    }

    @Override
    public boolean exists() {
        return file.exists();
    }

    @Override
    public InputStream retrieve() throws ResourceException {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new ResourceException(e);
        }
    }

    @Override
    public JtwigResource resolve(String relativePath) {
        return new FileJtwigResource(new File(file.getParentFile(), relativePath));
    }

    @Override
    public String path() {
        return file.getPath();
    }
    
    @Override
    public String toString() {
        return file.getPath();
    }
}
