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

import javax.servlet.ServletContext;
import static org.hamcrest.Matchers.endsWith;
import org.jtwig.loader.Loader;
import org.jtwig.loader.impl.WebResourceLoader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

public class WebResourceLoaderTest {
    
    private static final String URL = "file.test";
    private ServletContext ctx;
    private WebResourceLoader loader;
    
    @Before
    public void before() throws Exception {
        ctx = new MockServletContext();
        loader = new WebResourceLoader(ctx);
    }
    
    @Test
    public void ensureExistsWorks() throws Exception {
        assertTrue(loader.exists("/WEB-INF/views/default/test.twig.html"));
        assertFalse(loader.exists("invalid.twig"));
    }
    
    @Test
    public void ensureRetrieveWorks() throws Exception {
        assertNotNull(loader.get("WEB-INF/views/default/test.twig.html"));
        assertNull(loader.get("invalid.twig"));
    }
    
    @Test
    public void ensureCanonicalPathsAreCorrect() throws Exception {
        Loader.Resource resource = loader.get("WEB-INF/views/default/test.twig.html");
        assertThat(resource.canonicalPath(), endsWith("WEB-INF/views/default/test.twig.html"));
    }
    
    @Test
    public void ensureRelativePathsAreCorrect() throws Exception {
        Loader.Resource resource = loader.get("WEB-INF/views/default/test.twig.html");
        assertEquals("WEB-INF/views/default/test.twig.html", resource.relativePath());
    }
    
    @Test
    public void testGettingSource() throws Exception {
        Loader.Resource resource = loader.get("WEB-INF/views/default/test.twig.html");
        assertNotNull(resource.source());
    }
    
    @Test
    public void testPathResolution() throws Exception {
        Loader.Resource resource = loader.get("WEB-INF/views/default/test.twig.html");
        assertEquals("WEB-INF/views/mobile/test.twig.html", resource.resolve("../mobile/test.twig.html"));
    }
}