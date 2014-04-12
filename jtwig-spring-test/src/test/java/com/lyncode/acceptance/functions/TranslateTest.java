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
import com.lyncode.jtwig.services.impl.InMemoryMessageSource;
import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import static com.lyncode.jtwig.GetMethodMatchers.body;
import static com.lyncode.jtwig.SyntacticSugar.then;
import static com.lyncode.jtwig.SyntacticSugar.when;
import static java.util.Locale.ENGLISH;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@Controller
public class TranslateTest extends AbstractJtwigAcceptanceTest {
    @RequestMapping("/")
    public String action () {
        return "translate/test";
    }

    @Bean
    public MessageSource messageSource () {
        InMemoryMessageSource inMemoryMessageSource = new InMemoryMessageSource();
        inMemoryMessageSource.add("test", "Hello {0}");
        return inMemoryMessageSource;
    }

    @Bean
    public LocaleResolver localeResolver () {
        return new FixedLocaleResolver(ENGLISH);
    }



    @Test
    public void translateTest() throws Exception {
        when(serverReceivesGetRequest("/"));
        then(theGetResult(), body(is(equalTo("Hello JTwig"))));
    }
}
