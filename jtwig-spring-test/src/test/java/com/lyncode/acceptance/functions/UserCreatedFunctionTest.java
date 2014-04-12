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

package com.lyncode.acceptance.functions;

import com.lyncode.acceptance.AbstractJtwigAcceptanceTest;
import com.lyncode.jtwig.functions.annotations.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.Parameter;
import com.lyncode.jtwig.mvc.JtwigViewResolver;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ViewResolver;

import static com.lyncode.jtwig.GetMethodMatchers.body;
import static com.lyncode.jtwig.SyntacticSugar.then;
import static com.lyncode.jtwig.SyntacticSugar.when;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@Controller
public class UserCreatedFunctionTest extends AbstractJtwigAcceptanceTest {
    @RequestMapping("/")
    public String action () {
        return "other/test";
    }

    @Configuration
    public static class Config extends JtwigViewResolverConfig {
        @Autowired
        private UserCreatedFunctionTest controller;


        @Bean
        public ViewResolver viewResolver () {
            JtwigViewResolver viewResolver = (JtwigViewResolver) super.viewResolver();
            viewResolver.includeFunctions(controller);
            return viewResolver;
        }
    }

    @Override
    protected Class<?>[] configurationClasses() {
        return new Class[]{
                Config.class
        };
    }

    @JtwigFunction(name = "other")
    public String func (@Parameter String input) {
        return input;
    }

    @Test
    public void userDefinedFunction() throws Exception {
        when(serverReceivesGetRequest("/"));
        then(theGetResult(), body(is(equalTo("a"))));
    }
}
