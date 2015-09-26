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

package org.jtwig.extension.spring.functions;

import org.jtwig.acceptance.AbstractViewAcceptanceTest;
import org.jtwig.exceptions.AssetResolveException;
import org.jtwig.extension.spring.SpringExtension;
import org.jtwig.services.api.assets.AssetResolver;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = AssetFunctionTest.class)
public class AssetFunctionTest extends AbstractViewAcceptanceTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        registerBean("assetResolver", assetResolver());
        SpringExtension springExtension = new SpringExtension();
        applicationContext().getAutowireCapableBeanFactory().autowireBean(springExtension);
        env.getConfiguration().getExtensions().addExtension(springExtension);
    }

    @Test
    public void assetTest() throws Exception {
        assertEquals("/One", env.getConfiguration().getExtensions().getFunction("asset").evaluate(env, null, "One"));
    }
    
    @Bean
    public AssetResolver assetResolver () {
        return new AssetResolver() {
            @Override
            public String resolve(String asset) throws AssetResolveException {
                return asset;
            }
        };
    }
}