/**
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

package com.lyncode.jtwig.addons.concurrent;

import com.lyncode.jtwig.addons.Addon;
import com.lyncode.jtwig.addons.AddonModel;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;

public class ConcurrentAddon extends Addon {

    public ConcurrentAddon(JtwigResource resource, ParserConfiguration configuration) {
        super(resource, configuration);
    }

    @Override
    public AddonModel instance() {
        return new Concurrent();
    }

    @Override
    public String beginKeyword() {
        return "concurrent";
    }

    @Override
    public String endKeyword() {
        return "endconcurrent";
    }
}
