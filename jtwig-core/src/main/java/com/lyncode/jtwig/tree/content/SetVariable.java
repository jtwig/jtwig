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
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.tree.api.Content;
import com.lyncode.jtwig.tree.api.Expression;
import com.lyncode.jtwig.tree.expressions.Variable;
import com.lyncode.jtwig.tree.structural.Block;

import java.io.OutputStream;

public class SetVariable implements Content {
    private Variable name;
    private Expression assignment;

    public SetVariable(Variable name) {
        this.name = name;
    }

    public boolean setAssignment(Expression assignment) {
        this.assignment = assignment;
        return true;
    }

    public Variable getName() {
        return name;
    }

    public Object getAssignment() {
        return assignment;
    }

    @Override
    public boolean render(OutputStream outputStream, JtwigContext context) throws RenderException {
        try {
            context.set(name.getIdentifier(), assignment.calculate(context));
            return true;
        } catch (CalculateException e) {
            throw new RenderException(e);
        }
    }

    @Override
    public SetVariable compile(JtwigResource resource) throws CompileException {
        return this;
    }

    @Override
    public boolean replace(Block expression) throws CompileException {
        return false;
    }
}
