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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StringJtwigResource implements JtwigResource {
    private String content;

    public StringJtwigResource (String content) {
        this.content = content;
    }

    @Override
    public InputStream retrieve() throws ResourceException {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Override
    public JtwigResource resolve(String relativePath) throws ResourceException {
        throw new ResourceException("Cannot resolve relative resources in a String resource");
    }
}
