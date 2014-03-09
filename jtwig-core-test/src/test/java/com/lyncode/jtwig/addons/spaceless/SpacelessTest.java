/**
 * Copyright 2012 Lyncode
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

package com.lyncode.jtwig.addons.spaceless;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.resource.StringJtwigResource;
import com.lyncode.jtwig.tree.documents.JtwigDocument;
import com.lyncode.jtwig.tree.helper.RenderStream;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

public class SpacelessTest extends AbstractAddonTest {
    @Test
    public void testName() throws Exception {
        JtwigParser jtwigParser = new JtwigParser.Builder().build();

        StringJtwigResource resource = new StringJtwigResource("{% spaceless %}  <div>   <two>   </two>   </div>{% endspaceless %}");
        JtwigDocument document = JtwigParser.parse(jtwigParser, resource);
        RenderStream renderStream = new RenderStream(new ByteArrayOutputStream());
        document.compile(jtwigParser, resource).render(renderStream, new JtwigContext());

        System.out.println(renderStream.toString());
    }
}
