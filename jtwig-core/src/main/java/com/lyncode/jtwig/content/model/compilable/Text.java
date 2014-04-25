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

package com.lyncode.jtwig.content.model.compilable;

import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Compilable;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.content.api.Tag;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.render.RenderContext;

import java.io.IOException;

public class Text implements Compilable {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        String result = text;
        if (context.hasParent()) {
            Compilable before = context.parent().previous(this);
            Compilable after = context.parent().next(this);

            if (mustTrimAtRightOf(before, context)) {
                result = result.replaceAll("^\\s+", "");
            }
            if (mustTrimAtLeftOf(after, context)) {
                result = result.replaceAll("\\s+$", "");
            }
        }
        return new Compiled(result);
    }

    private boolean mustTrimAtLeftOf(Compilable element, CompileContext context) {
        if (element != null)
            return element instanceof Tag && ((Tag) element).tag().whiteSpaceControl().trimBeforeBegin();
        else {
            Compilable container = context.parent().parent();
            if (container != null && container instanceof Tag)
                return ((Tag) container).tag().whiteSpaceControl().trimBeforeEnd();
            return false;
        }
    }

    private boolean mustTrimAtRightOf(Compilable element, CompileContext context) {
        if (element != null)
            return element instanceof Tag && ((Tag) element).tag().whiteSpaceControl().trimAfterEnd();
        else {
            Compilable container = context.parent().parent();
            if (container != null && container instanceof Tag)
                return ((Tag) container).tag().whiteSpaceControl().trimAfterBegin();
            return false;
        }
    }

    private static class Compiled implements Renderable {
        private final String value;

        public Compiled(String value) {
            this.value = value;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            try {
                context.write(value.getBytes());
            } catch (IOException e) {
                throw new RenderException(e);
            }
        }
    }

    public static class Builder implements Compilable {
        private final StringBuilder builder = new StringBuilder();

        public Builder append (String piece) {
            builder.append(piece);
            return this;
        }


        public Text build () {
            return new Text(builder.toString());
        }

        @Override
        public Renderable compile(CompileContext context) throws CompileException {
            return null;
        }
    }
}
