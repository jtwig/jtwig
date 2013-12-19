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

package com.lyncode.jtwig.tree.content;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.tree.api.Compilable;
import com.lyncode.jtwig.tree.api.Renderable;
import com.lyncode.jtwig.tree.helper.ElementList;
import com.lyncode.jtwig.tree.structural.BlockExpression;

import java.io.OutputStream;

public class Content extends ElementList implements Renderable, Compilable<Content> {
    @Override
    public boolean render(OutputStream outputStream, JtwigContext context) throws RenderException {
        for (Object obj : getList()) {
            if (!(obj instanceof Renderable)) throw new RenderException("Expecting only renderable objects. Object "+obj.getClass().getName()+" isn't");
            Renderable renderable = (Renderable) obj;
            renderable.render(outputStream, context);
        }
        return true;
    }

    @Override
    public Content compile(JtwigResource resource) throws CompileException {
        for (int i=0;i<getList().size();i++) {
            if (getList().get(i) instanceof Compilable)
                getList().set(i, ((Compilable) getList().get(i)).compile(resource));
        }
        return this;
    }

    @Override
    public boolean replace(BlockExpression expression) throws CompileException {
        boolean replaced = false;
        for (int i=0;i<getList().size();i++) {
            if (getList().get(i) instanceof BlockExpression) {
                BlockExpression tmp = (BlockExpression) getList().get(i);
                if (expression.getName().equals(tmp.getName())) {
                    getList().set(i, expression.getContent());
                    replaced = true;
                }
            }
            else if (getList().get(i) instanceof Compilable)
                replaced = replaced || ((Compilable) getList().get(i)).replace(expression);
        }
        return replaced;
    }
}
