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

package org.jtwig.content.model.compilable;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Compilable;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.api.Tag;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.render.RenderContext;

public class Text implements Compilable {
    private final StringBuilder builder = new StringBuilder();

    public Text() {}
    public Text(final String text) {
        builder.append(text);
    }

    public Text append (String piece) {
        builder.append(piece);
        return this;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        String result = builder.toString();
        if (context.hasParent()) {
            Compilable before = context.parent().previous(this);
            Compilable after = context.parent().next(this);

            if (mustTrimStart(before, context)) {
                result = StringUtils.stripStart(result, null);
            }
            if (mustTrimEnd(after, context)) {
                result = StringUtils.stripEnd(result, null);
            }
        }
        return new Compiled(result);
    }

    private boolean mustTrimStart(Compilable element, CompileContext context) {
        if (element != null) {
            return element instanceof Tag && ((Tag) element).tag().whiteSpaceControl().trimAfterClose();
        }
        Compilable container = context.parent().parent();
        if (container != null && container instanceof Tag) {
            return ((Tag) container).tag().whiteSpaceControl().trimAfterOpen();
        }
        return false;
    }

    private boolean mustTrimEnd(Compilable element, CompileContext context) {
        if (element != null) {
            return element instanceof Tag && ((Tag) element).tag().whiteSpaceControl().trimBeforeOpen();
        }
        Compilable container = context.parent().parent();
        if (container != null && container instanceof Tag) {
            return ((Tag) container).tag().whiteSpaceControl().trimBeforeClose();
        }
        return false;
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
}
