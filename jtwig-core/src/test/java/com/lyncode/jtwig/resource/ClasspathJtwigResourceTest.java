/**
 * Copyright 2012 Lyncode
 *
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

package com.lyncode.jtwig.resource;

<<<<<<< HEAD:jtwig-core/src/test/java/com/lyncode/jtwig/resource/ClasspathJtwigResourceTest.java
=======
import com.lyncode.jtwig.resource.ClasspathJtwigResource;
import junit.framework.Assert;
>>>>>>> 23e3054... Supporting different types of template location::jtwig-core/src/test/java/com/lyncode/jtwig/unit/resource/ClasspathJtwigResourceTest.java
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

public class ClasspathJtwigResourceTest {
    private ClasspathJtwigResource underTest = new ClasspathJtwigResource("/sample.twig");

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
        ClasspathJtwigResource resource = new ClasspathJtwigResource("classpath:/templates/unit/sample.twig");
        Assert.assertNotNull(resource.retrieve());
    }
}
