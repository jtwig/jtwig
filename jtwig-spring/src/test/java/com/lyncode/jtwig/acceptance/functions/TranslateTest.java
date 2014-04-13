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

package com.lyncode.jtwig.acceptance.functions;

import com.lyncode.jtwig.acceptance.AbstractJtwigAcceptanceTest;
import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.lyncode.jtwig.util.SyntacticSugar.then;
import static com.lyncode.jtwig.util.SyntacticSugar.when;
import static com.lyncode.jtwig.util.matchers.GetMethodMatchers.body;
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

    public static class InMemoryMessageSource extends AbstractMessageSource {
        private Map<String, String> map = new HashMap<String, String>();

        @Override
        protected MessageFormat resolveCode(String code, Locale locale) {
            return new MessageFormat(map.get(code), locale);
        }

        public InMemoryMessageSource add (String code, String message) {
            this.map.put(code, message);
            return this;
        }
    }
}
