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

import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.resource.FileJtwigResource;
import com.lyncode.jtwig.resource.JtwigResource;
import org.junit.Test;
import org.parboiled.common.FileUtils;

import java.io.File;
import java.net.URI;
import java.net.URL;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.Assert.assertThat;

public class FileJtwigResourceTest {
    @Test
    public void fileResource() throws Exception {
        URL classLoader = getClass().getResource("/templates/unit/sample.twig");
        URI uri = classLoader.toURI();
        FileJtwigResource resource = new FileJtwigResource(new File(uri));

        assertThat(FileUtils.readAllText(resource.retrieve()), notNullValue());
        assertThat(resource.toString(), endsWith("/templates/unit/sample.twig"));
        assertThat(resource.resolve("other.jtwig"), notNullValue());
    }
    
    @Test
    public void ensureExistsMethodWorksProperly() throws Exception {
        JtwigResource resource = new FileJtwigResource(new File(getClass().getResource("/templates/unit/sample.twig").toURI()));
        assertThat(resource.exists(), equalTo(true));
        
        resource = resource.resolve("invalid.twig");
        assertThat(resource.exists(), equalTo(false));
    }

    @Test
    public void whatHappensWhenFileProtocolGiven () throws ResourceException, NoSuchFieldException, IllegalAccessException {
        FileJtwigResource resource = new FileJtwigResource("file:///tmp/test");
        assertThat(resource.toString(), equalTo("/tmp/test"));
    }


}
