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

package com.lyncode.jtwig.functions;

import com.lyncode.jtwig.exceptions.AssetResolveException;
import com.lyncode.jtwig.functions.parameters.resolve.HttpRequestParameterResolver;
import com.lyncode.jtwig.functions.repository.FunctionResolver;
import com.lyncode.jtwig.services.api.assets.AssetResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletRequest;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringFunctionsTest.class)
public class SpringFunctionsTest {
    @Bean
    public AssetResolver assetResolver () {
        return new AssetResolver() {
            @Override
            public String resolve(String asset) throws AssetResolveException {
                return asset;
            }
        };
    }


    @Autowired
    private ApplicationContext applicationContext;

    private SpringFunctions functions = new SpringFunctions();
    private FunctionResolver builder = new FunctionResolver()
            .add(new HttpRequestParameterResolver())
            .store(functions);

    @Before
    public void setup () {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(functions);
    }


    @Test
    public void assetTest() throws Exception {
        HttpServletRequest mock = mock(HttpServletRequest.class);
        assertEquals("One", functions.asset(mock, "One"));
    }
}
