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
import org.jtwig.content.api.Renderable;
import org.jtwig.exception.CompileException;

public abstract class Content<T extends Content> extends AbstractElement {
    private Sequence content;

    public T withContent (Sequence content) {
        this.content = content.withParent(this);
        return (T) this;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        return content.compile(context);
    }
}
