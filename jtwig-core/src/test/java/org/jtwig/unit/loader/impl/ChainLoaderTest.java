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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jtwig.exception.ResourceException;
import org.jtwig.loader.Loader;
import org.jtwig.loader.impl.ChainLoader;
import org.jtwig.loader.impl.FileLoader;
import org.junit.Before;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class ChainLoaderTest {
    private ChainLoader loader;
    
    @Before
    public void before() throws Exception {
        List<Loader> loaders = new ArrayList<>();
        
        Loader loader1 = mock(Loader.class);
        when(loader1.exists(anyString())).thenReturn(Boolean.FALSE);
        when(loader1.exists("test1.twig")).thenReturn(Boolean.TRUE);
        when(loader1.get("test1.twig")).thenReturn(mock(Loader.Resource.class));
        loaders.add(loader1);
        
        Loader loader2 = mock(Loader.class);
        when(loader2.exists(anyString())).thenReturn(Boolean.FALSE);
        when(loader2.exists("test3.twig")).thenReturn(Boolean.TRUE);
        when(loader2.get("test3.twig")).thenReturn(mock(Loader.Resource.class));
        loaders.add(loader2);
        
        loader = new ChainLoader(loaders);
    }
    
    @Test
    public void callsExistsOnLoaders() throws Exception {
        assertTrue(loader.exists("test1.twig"));
        assertFalse(loader.exists("test2.twig"));
        assertTrue(loader.exists("test3.twig"));
    }
    
    @Test
    public void callsGetOnLoaders() throws Exception {
        assertNotNull(loader.get("test1.twig"));
        assertNull(loader.get("test2.twig"));
        assertNotNull(loader.get("test3.twig"));
    }
}