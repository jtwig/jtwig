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

package org.jtwig.extension.spring;

import org.jtwig.Environment;
import org.jtwig.acceptance.AbstractViewAcceptanceTest;
import org.junit.Before;

public abstract class AbstractSpringExtensionTest extends AbstractViewAcceptanceTest {
    protected Environment env;
    protected SpringExtension extension;

    @Before
    @Override
    public void setUp () throws Exception {
        super.setUp();
        env = new Environment();
        extension = new SpringExtension();
        applicationContext().getAutowireCapableBeanFactory().autowireBean(extension);
        env.getConfiguration().getExtensions().addExtension(extension);
    }
}