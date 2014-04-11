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

package com.lyncode.jtwig.tree.content;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.positioning.Position;
import com.lyncode.jtwig.tree.api.AbstractContent;

import java.io.OutputStream;

public class Comment extends AbstractContent {
    public Comment(Position position) {
        super(position);
    }
    public String toString() {
        return "Comment";
    }

    @Override
    public void render(OutputStream outputStream, JtwigContext context) throws RenderException {}
}
