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

package com.lyncode.jtwig.unit.resource;

import com.lyncode.jtwig.resource.ClasspathJtwigResource;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ClasspathJtwigResourceTest {
    private ClasspathJtwigResource underTest = new ClasspathJtwigResource("/templates/unit/sample.twig");

    @Test
    public void testRetrieve() throws Exception {
        assertNotNull(underTest.retrieve());
    }

    @Test
    public void testResolve() throws Exception {
        assertNotNull(underTest.resolve("other.twig").retrieve());
    }

    @Test
    public void classpathPrefixRemoved() throws Exception {
        ClasspathJtwigResource resource = new ClasspathJtwigResource("classpath:/templates/../templates/unit/sample.twig");
        assertNotNull(resource.retrieve());
    }
    
    @Test
    public void ensureExistsMethodWorksProperly() throws Exception {
        assertTrue(underTest.exists());
        assertFalse(underTest.resolve("invalid.twig").exists());
    }
}
