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

package org.jtwig.loader;

import java.io.InputStream;
import org.jtwig.exception.ResourceException;

/**
 * A loader implementation provides a means by which to retrieve a template from
 * a given datastore, such as a file.
 */
public abstract class Loader {
    /**
     * Determines whether or not the loader can find the request template.
     * @param name The name of the template to find
     * @return True if exists, false otherwise
     * @throws org.jtwig.exception.ResourceException  Resource Exception
     */
    public abstract boolean exists(String name) throws ResourceException;
    /**
     * Retrieves a representation of the requested template.
     * @param name The name of the template to retrieve
     * @return The template representation if the template exists, or null
     * @throws org.jtwig.exception.ResourceException  Resource Exception
     */
    public abstract Resource get(String name) throws ResourceException;
    
    /**
     * A loader resource implementation provides a means by which to retrieve
     * the source and other information of a template.
     */
    public abstract static class Resource {
        /**
         * Retrieves or builds the key used to retrieve the given template from
         * the cache.
         * @return The cache key
         */
        public abstract String getCacheKey();
        /**
         *
         * @return the absolute path of the resource.
         */
        public abstract String canonicalPath();
        /**
         *
         * @return the path of the resource relative to the loader root.
         */
        public abstract String relativePath();
        /**
         * Retrieve the source code of the template as an input stream.
         * @return The source of the template
         * @throws ResourceException  Resource Exception
         */
        public abstract InputStream source() throws ResourceException;
        /**
         * Creates a path, relative to the loader root, from the given relative
         * path. The returned path should be usable by other loaders.
         * @param relative Relative path
         * @return Usable path
         * @throws ResourceException Resource Exception
         */
        public abstract String resolve(String relative) throws ResourceException;

        @Override
        public String toString() {
            return relativePath();
        }
    }
}