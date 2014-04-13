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

package com.lyncode.jtwig.tree.api;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.tree.helper.RenderStream;
import com.lyncode.jtwig.tree.structural.Block;
import com.lyncode.jtwig.unit.resource.JtwigResource;

public interface Content {
    /**
     * Render the content to the output stream
     *
     * @param outputStream
     * @param context
     * @throws RenderException If something goes wrong
     */
    void render (RenderStream outputStream, JtwigContext context) throws RenderException;

    /**
     * This method must return a compiled version of the current content.
     * It should be used for optimization purposes.
     *
     * @param parser
     * @param resource
     * @return
     * @throws CompileException
     */
    Content compile(JtwigParser parser, JtwigResource resource) throws CompileException;

    /**
     * Should replace the given block.
     * Returns true if it was replaced, false otherwise.
     *
     * @param expression
     * @return
     * @throws CompileException
     */
    boolean replace (Block expression) throws CompileException;
}
