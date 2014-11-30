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

package org.jtwig.acceptance.functions;

import org.jtwig.acceptance.AbstractJtwigAcceptanceTest;
import org.jtwig.services.api.assets.AssetResolver;
import org.jtwig.services.impl.assets.BaseAssetResolver;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.jtwig.util.SyntacticSugar.then;
import static org.jtwig.util.SyntacticSugar.when;
import static org.jtwig.util.matchers.GetMethodMatchers.body;

@Controller
public class AssetTest extends AbstractJtwigAcceptanceTest {
    @RequestMapping("/")
    public String assetAction () {
        return "asset/resolve";
    }

    @Bean
    public AssetResolver assetResolver () {
        return new BaseAssetResolver();
    }

    @Test
    public void assetTest() throws Exception {
        when(serverReceivesGetRequest("/"));
        then(theGetResult(), body(is(equalTo("/default/test"))));
    }
}
