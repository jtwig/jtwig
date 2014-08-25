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

package com.lyncode.jtwig.addons.spaceless;

import com.google.common.base.Function;
import com.lyncode.jtwig.addons.tag.TagAddon;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;

public class SpacelessAddon extends TagAddon {
    public SpacelessAddon(JtwigResource resource, ParserConfiguration configuration) {
        super(resource, configuration);
    }

    @Override
    protected Function<String, String> transformation() {
        return new Function<String, String>() {
            @Override
            public String apply(String input) {
                return input
                        .replaceAll("\\s+<", "<")
                        .replaceAll(">\\s+", ">");
            }
        };
    }

    @Override
    protected String keyword() {
        return "spaceless";
    }

}
