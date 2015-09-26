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

import java.util.HashMap;
import java.util.Map;
import org.jtwig.extension.SimpleExtension;
import org.jtwig.extension.api.filters.Filter;
import org.jtwig.extension.api.functions.Function;
import org.jtwig.extension.spring.filters.TranslateFilter;
import org.jtwig.extension.spring.functions.AssetFunction;
import org.jtwig.extension.spring.functions.PathFunction;
import org.jtwig.extension.spring.functions.PropertyFunction;
import org.jtwig.extension.spring.functions.RenderFunction;
import org.jtwig.services.api.assets.AssetResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;

public class SpringExtension extends SimpleExtension {
    @Autowired(required = false)
    MessageSource messageSource;
    @Autowired(required = false)
    LocaleResolver localeResolver;
    @Autowired(required = false)
    AssetResolver assetResolver;
    @Autowired(required = false)
    Environment env;

    @Override
    public String getName() {
        return "spring";
    }

    @Override
    public Map<String, Filter> getFilters() {
        return new HashMap<String, Filter>(){{
            put("translate", new TranslateFilter(messageSource, localeResolver));
        }};
    }

    @Override
    public Map<String, Function> getFunctions() {
        return new HashMap<String, Function>(){{
            put("asset", new AssetFunction(assetResolver));
            put("path", new PathFunction());
            put("property", new PropertyFunction(env));
            put("render", new RenderFunction());
        }};
    }
    
}