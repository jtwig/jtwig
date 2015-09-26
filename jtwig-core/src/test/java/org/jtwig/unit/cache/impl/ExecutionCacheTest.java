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

package org.jtwig.unit.cache.impl;

import org.jtwig.cache.impl.ExecutionCache;
import org.jtwig.content.model.Template;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class ExecutionCacheTest {
    @Test
    public void testParsedTemplateLifecycle() throws Exception {
        Template template = new Template(null);
        Template.Compiled compiledTemplate = new Template.Compiled(null, null, null, null, null, null);
        
        ExecutionCache cache = new ExecutionCache();
        cache.addParsed("test1", template);
        assertEquals(template, cache.getParsed("test1"));
        assertEquals(template, cache.removeParsed("test1"));
        assertNull(cache.getParsed("test1"));
        
        cache.addCompiled("test2", compiledTemplate);
        assertEquals(compiledTemplate, cache.getCompiled("test2"));
        assertEquals(compiledTemplate, cache.removeCompiled("test2"));
        assertNull(cache.getCompiled("test2"));
    }
    
    @Test
    public void passingNullNamesReturnsNull() throws Exception {
        ExecutionCache cache = new ExecutionCache();
        assertNull(cache.getParsed(null));
        assertNull(cache.removeParsed(null));
        assertNull(cache.getCompiled(null));
        assertNull(cache.removeCompiled(null));
    }
}