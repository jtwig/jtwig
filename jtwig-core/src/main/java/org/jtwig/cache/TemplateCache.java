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

package org.jtwig.cache;

import org.jtwig.content.model.Template;

/**
 * A TemplateCache implementation is used at compile/render time to store and
 * retrieve previously-compiled templates in order to avoid re-compiling them
 * repeatedly.
 */
public interface TemplateCache {
    /**
     * Caches the given parsed template so that it can be retrieved again later.
     * @param name The name or identifier of the template
     * @param template The template to store
     * @return The cache instance
     */
    TemplateCache addParsed(String name, Template template);
    /**
     * Retrieves the parsed template indicated by the provided name.
     * @param name The name or identifier of the template
     * @return The template requested, or null if not found
     */
    Template getParsed(String name);
    /**
     * Removes the parsed template indicated by the provided name.
     * @param name The name or identifier of the template
     * @return The removed template, or null if not found
     */
    Template removeParsed(String name);
    /**
     * Caches the given compiled template so that it can be retrieved again
     * later.
     * @param name The name or identifier of the template
     * @param template The compiled template to store
     * @return The cache instance
     */
    TemplateCache addCompiled(String name, Template.Compiled template);
    /**
     * Retrieves the compiled template indicated by the provided name.
     * @param name The name or identifier of the template
     * @return The compiled template requested, or null if not found
     */
    Template.Compiled getCompiled(String name);
    /**
     * Removes the compiled template indicated by the provided name.
     * @param name The name or identifier of the compiled template
     * @return The removed compiled template, or null if not found
     */
    Template.Compiled removeCompiled(String name);
}