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

package com.lyncode.jtwig;

import com.lyncode.jtwig.exception.ComposeException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.resource.StringJtwigResource;
import com.lyncode.jtwig.tree.content.Content;

import java.io.OutputStream;

public class JtwigTemplate {
    private JtwigResource resource;
    private JtwigContext context;

    public JtwigTemplate(JtwigResource resource, JtwigContext context) {
        this.resource = resource;
        this.context = context;
    }

    public JtwigTemplate(JtwigResource resource) {
        this.resource = resource;
        this.context = new JtwigContext();
    }

    public JtwigTemplate (String content) {
        this.resource = new StringJtwigResource(content);
        this.context = new JtwigContext();
    }

    public JtwigTemplate withModelAttribute(String key, Object value) {
        this.context.withModelAttribute(key, value);
        return this;
    }

    public void output (OutputStream outputStream) throws ParseException, ComposeException, RenderException {
        JtwigParser.parse(resource)
                .compose(resource)
                .render(outputStream, context);
    }


    public Content compile () throws ParseException, ComposeException {
        return JtwigParser.parse(resource).compose(resource);
    }
}
