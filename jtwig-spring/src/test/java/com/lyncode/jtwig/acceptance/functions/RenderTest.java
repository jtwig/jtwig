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
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.lyncode.jtwig.util.SyntacticSugar.then;
import static com.lyncode.jtwig.util.SyntacticSugar.when;
import static com.lyncode.jtwig.util.matchers.GetMethodMatchers.body;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@Controller
public class RenderTest extends AbstractJtwigAcceptanceTest {
    @RequestMapping("/")
    public String action () {
        return "render/test";
    }

    @RequestMapping("/doPost")
    public String doPost () {
        return "render/post";
    }

    @RequestMapping("/test")
    public ResponseEntity<String> test () {
        return new ResponseEntity<>("k", HttpStatus.OK);
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public ResponseEntity<String> post (@RequestBody String value) {
        return new ResponseEntity<>(value, HttpStatus.OK);
    }

    @Test
    public void renderTest() throws Exception {
        when(serverReceivesGetRequest("/"));
        then(theGetResult(), body(is(equalTo("ok"))));
    }

    @Ignore("Render not working for posts")
    @Test
    public void renderPostTest() throws Exception {
        when(serverReceivesGetRequest("/doPost"));
        then(theGetResult(), body(is(equalTo("ok"))));
    }
}
