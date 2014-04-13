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

package com.lyncode.jtwig.parser;

import com.lyncode.jtwig.addons.concurrent.ConcurrentParser;
import com.lyncode.jtwig.addons.spaceless.SpacelessParser;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.parser.addons.JtwigContentAddonParser;
import com.lyncode.jtwig.parser.addons.JtwigEmptyContentAddonParser;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;

import java.util.ArrayList;
import java.util.List;

public class JtwigParserBuilder {
    private ParserConfiguration configuration = new ParserConfiguration();
    private List<Class<? extends JtwigEmptyContentAddonParser>> emptyAddons = new ArrayList<>();
    private List<Class<? extends JtwigContentAddonParser>> contentAddons = new ArrayList<>();

    public JtwigParserBuilder() {
        contentAddons
                .add(SpacelessParser.class);
        contentAddons
                .add(ConcurrentParser.class);
    }

    public JtwigParserBuilder withEmptyAddon (Class<? extends JtwigEmptyContentAddonParser> parserType) {
        emptyAddons.add(parserType);
        return this;
    }
    public JtwigParserBuilder withContentAddon (Class<? extends JtwigContentAddonParser> parserType) {
        contentAddons.add(parserType);
        return this;
    }

    public JtwigParserBuilder withConfiguration (ParserConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public JtwigParser build (JtwigResource resource) throws ParseException {
        return JtwigParser.newParser(resource, configuration, emptyAddons, contentAddons);
    }
}
