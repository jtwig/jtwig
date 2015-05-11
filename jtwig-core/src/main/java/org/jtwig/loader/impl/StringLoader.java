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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.jtwig.exception.ResourceException;
import org.jtwig.loader.Loader;

public class StringLoader extends Loader {
    private final String source;
    
    public StringLoader(String source) {
        this.source = source;
    }
    
    @Override
    public boolean exists(String name) {
        return true;
    }

    @Override
    public StringResource get(String name) {
        return new StringResource(source);
    }
    
    public static class StringResource extends Resource {
        private final String source;
        
        public StringResource(String source) {
            this.source = source;
        }

        @Override
        public String getCacheKey() {
            return null;
        }

        @Override
        public String canonicalPath() {
            return "";
        }

        @Override
        public String relativePath() {
            return "";
        }

        @Override
        public InputStream source() throws ResourceException {
            return new ByteArrayInputStream(source.getBytes());
        }

        @Override
        public String resolve(String relative) throws ResourceException {
            throw new ResourceException("StringResource does not support relative path resolution.");
        }
        
    }
    
}