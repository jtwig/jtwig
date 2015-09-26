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

package org.jtwig.acceptance;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jtwig.parser.config.TagSymbols.JAVASCRIPT_COLLISION_FREE;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.jtwig.extension.core.CoreJtwigExtension;
import org.junit.Test;

public class CustomTagsTest {
    @Test
    public void javascriptOutputTag() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("@> 1 <@", JtwigConfigurationBuilder.newConfiguration()
                .withSymbols(JAVASCRIPT_COLLISION_FREE)
                .build())
            .render(model);

        assertThat(result, is("1"));
    }

    @Test
    public void javascriptCodeTag() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        JtwigConfiguration config = JtwigConfigurationBuilder.newConfiguration()
                .withSymbols(JAVASCRIPT_COLLISION_FREE)
                .build();
        config.getExtensions().addExtension(new CoreJtwigExtension(config));
        
        String result = JtwigTemplate
            .inlineTemplate("<# if (true) #>Hello<# endif #>", config)
            .render(model);

        assertThat(result, is("Hello"));
    }

    @Test
    public void javascriptComment() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        JtwigConfiguration config = JtwigConfigurationBuilder.newConfiguration()
                .withSymbols(JAVASCRIPT_COLLISION_FREE)
                .build();
        config.getExtensions().addExtension(new CoreJtwigExtension(config));
        
        String result = JtwigTemplate
            .inlineTemplate("<$ if (true) #>Hello<# endif $>", config)
            .render(model);

        assertThat(result, is(""));
    }
}
