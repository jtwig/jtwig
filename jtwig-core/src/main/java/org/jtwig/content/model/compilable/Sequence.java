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

import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Compilable;
import org.jtwig.content.api.Renderable;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.render.RenderContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jtwig.content.api.ability.ElementList;

public class Sequence extends AbstractElement implements ElementList<Compilable> {
    private List<Compilable> contents = new ArrayList<>();
    private Compilable parent;
    private Compilable after;

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        List<Renderable> result = new ArrayList<>();
        context = context.clone().withParent(this);
        for (Compilable content : contents) {
            Renderable renderable = content.compile(context);
            if (renderable != Renderable.NOOP) // Optimization for comments
                result.add(renderable);
        }
        return new Compiled(result);
    }

    @Override
    public Sequence add (Compilable compilable) {
        this.contents.add(compilable);
        return this;
    }

    @Override
    public Collection<Compilable> elements() {
        return contents;
    }

    public Compilable previous(Compilable current) {
        int indexOf = contents.indexOf(current);
        if (indexOf > 0) return contents.get(indexOf - 1);
        else return null;
    }

    public Compilable next(Compilable current) {
        int indexOf = contents.indexOf(current);
        if (indexOf < contents.size() - 1) return contents.get(indexOf + 1);
        else return null;
    }

    public Sequence withParent(Compilable parent) {
        this.parent = parent;
        return this;
    }

    public Compilable parent() {
        return this.parent;
    }

    private static class Compiled implements Renderable {
        private List<Renderable> contents = new ArrayList<>();

        public Compiled(List<Renderable> result) {
            contents.addAll(result);
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            for (Renderable content : contents) {
                content.render(context);
            }
        }
    }
}
