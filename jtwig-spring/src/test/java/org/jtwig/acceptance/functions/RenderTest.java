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
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.jtwig.util.SyntacticSugar.then;
import static org.jtwig.util.SyntacticSugar.when;
import static org.jtwig.util.matchers.GetMethodMatchers.body;

@Controller
public class RenderTest extends AbstractJtwigAcceptanceTest {
    @RequestMapping("/")
    public String action () {
        return "render/test";
    }

    @RequestMapping("/doParameters")
    public String doPost () {
        return "render/parameters";
    }

    @RequestMapping("/test")
    public ResponseEntity<String> test () {
        return new ResponseEntity<>("k", HttpStatus.OK);
    }

    @RequestMapping(value = "/parameters")
    public ResponseEntity<String> post (@RequestParam("title") String title) {
        return new ResponseEntity<>(title, HttpStatus.OK);
    }

    @Test
    public void renderTest() throws Exception {
        when(serverReceivesGetRequest("/"));
        then(theGetResult(), body(is(equalTo("ok"))));
    }

    @Test
    public void renderWithParametersTest() throws Exception {
        when(serverReceivesGetRequest("/doParameters"));
        then(theGetResult(), body(endsWith("Hi")));
    }
}
