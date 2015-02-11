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

import java.io.File;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringEndsWith.endsWith;
import org.jtwig.exception.ResourceException;
import org.jtwig.loader.impl.FileLoader;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.parboiled.common.FileUtils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FileLoaderTest {
    @Test
    public void fileResource() throws Exception {
        FileLoader loader = new FileLoader(new File[]{new File(getClass().getResource("/").toURI())});
        FileLoader.FileResource file = loader.get("/templates/unit/sample.twig");
        
        assertThat(FileUtils.readAllText(file.source()), notNullValue());
        assertThat(file.canonicalPath(), endsWith("/templates/unit/sample.twig"));
        assertThat(file.resolve("other.jtwig"), endsWith("/templates/unit/other.jtwig"));
    }
    
    @Test
    public void ensureExistsMethodWorksProperly() throws Exception {
        FileLoader loader = new FileLoader(new String[]{getClass().getResource("/").toExternalForm()});
        assertThat(loader.exists("/templates/unit/sample.twig"), equalTo(true));
        assertThat(loader.exists("invalid.twig"), equalTo(false));
    }
    
    @Test
    public void retrievingNonExistingResourceReturnsNull() throws Exception {
        FileLoader loader = new FileLoader(new String[]{getClass().getResource("/").toExternalForm()});
        assertNull(loader.get("/nonexistent.twig"));
    }
    
    @Test
    public void ensureThatScrewedUpExistsCallReturnsNull() throws Exception {
        FileLoader loader = spy(new FileLoader(new String[]{getClass().getResource("/").toExternalForm()}));
        when(loader.exists("/nonexistent.twig")).thenReturn(Boolean.TRUE);
        assertNull(loader.get("/nonexistent.twig"));
    }
    
    @Test
    public void ensurePathNormalizationWorks() throws Exception {
        FileLoader loader = new FileLoader(new String[]{getClass().getResource("/").toExternalForm()});
        assertNotNull(loader.get("./templates/unit/../unit/sample.twig"));
    }
    
    @Test(expected = ResourceException.class)
    public void ensurePathNormalizationCatchesOutOfHierarchyLoading() throws Exception {
        FileLoader loader = new FileLoader(new String[]{getClass().getResource("/").toExternalForm()});
        loader.get("../sample.twig");
    }
}
