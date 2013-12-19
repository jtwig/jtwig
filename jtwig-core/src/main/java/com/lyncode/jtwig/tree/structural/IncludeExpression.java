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

package com.lyncode.jtwig.tree.structural;

import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.tree.api.Compilable;
import com.lyncode.jtwig.tree.content.Content;
import com.lyncode.jtwig.tree.documents.JtwigDocument;

public class IncludeExpression implements Compilable<Content> {
    private String path;

    public IncludeExpression(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public Content compile(JtwigResource resource) throws CompileException {
        try {
            JtwigResource jtwigResource = resource.resolve(path);
            JtwigDocument jtwigDocument = JtwigParser.parse(jtwigResource);
            return jtwigDocument.compile(jtwigResource);
        } catch (ParseException e) {
            throw new CompileException(e);
        } catch (ResourceException e) {
            throw new CompileException(e);
        }
    }

    @Override
    public boolean replace(BlockExpression expression) throws CompileException {
        return false;
    }
}
