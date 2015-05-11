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

package org.jtwig.unit.loader.impl;

import static org.hamcrest.core.StringEndsWith.endsWith;
import org.jtwig.loader.impl.ClasspathLoader;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ClasspathLoaderTest {
    private ClasspathLoader loader = new ClasspathLoader();
//    private ClasspathJtwigResource underTest = new ClasspathJtwigResource("/templates/unit/sample.twig");

    @Test
    public void ensureExistsMethodWorksProperly() throws Exception {
        assertTrue(loader.exists("/templates/unit/sample.twig"));
        assertTrue(loader.exists("templates/unit/sample.twig"));
        assertFalse(loader.exists("/invalid.twig"));
        assertFalse(loader.exists("invalid.twig"));
    }
    
    @Test
    public void getExistingResource() throws Exception {
        ClasspathLoader.ClasspathResource resource = loader.get("/templates/unit/sample.twig");
        assertNotNull(resource);
        assertNotNull(resource.source());
    }

    @Test
    public void testResolve() throws Exception {
        ClasspathLoader.ClasspathResource resource = loader.get("templates/unit/sample.twig");
        assertEquals("templates/other.jtwig", resource.resolve("../other.jtwig"));
    }

    @Test
    public void classpathPrefixRemoved() throws Exception {
        ClasspathLoader.ClasspathResource resource = loader.get("classpath:/templates/../templates/unit/sample.twig");
        assertNotNull(resource.source());
    }
}