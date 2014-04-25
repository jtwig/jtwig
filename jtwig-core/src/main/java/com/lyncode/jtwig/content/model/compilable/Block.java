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
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.content.model.renderable.Replacement;
import com.lyncode.jtwig.exception.CompileException;

public class Block extends Content<Block> {
    private final String name;

    public Block(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        Renderable render = super.compile(context);
        if (context.hasReplacement(name()))
            return new Replacement(context.replacement(name()), render);
        return render;
    }
}
