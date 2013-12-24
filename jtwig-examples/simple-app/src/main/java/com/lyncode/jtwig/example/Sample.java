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

package com.lyncode.jtwig.example;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.resource.JtwigResource;

import java.io.InputStream;

public class Sample {
    public static void main (String... args) throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate(getClasspathResource("template.twig"));
        JtwigContext context = new JtwigContext();

        context.withModelAttribute("name", "JTwig User");

        System.out.println(template.output(context));
    }

    private static JtwigResource getClasspathResource(final String resourcePath) {
        return new JtwigResource() {
            @Override
            public InputStream retrieve() throws ResourceException {
                return Sample.class.getClassLoader().getResourceAsStream(resourcePath);
            }

            @Override
            public JtwigResource resolve(String relativePath) throws ResourceException {
                return null;
            }
        };
    }
}
