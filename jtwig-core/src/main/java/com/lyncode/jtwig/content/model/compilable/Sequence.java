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
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.render.RenderContext;

import java.util.ArrayList;
import java.util.List;

public class Sequence extends AbstractElement {
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

    public Sequence add (Compilable compilable) {
        this.contents.add(compilable);
        return this;
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
