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

package com.lyncode.jtwig.render;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.render.config.RenderConfiguration;
import com.lyncode.jtwig.render.stream.RenderStream;

import java.io.IOException;
import java.io.OutputStream;

public class RenderContext {
    /**
     * NOTE: This method should only be used once (in JtwigTemplate)
     */
    public static RenderContext create (RenderConfiguration configuration, JtwigContext context, OutputStream output) {
        return new RenderContext(configuration, context, new RenderStream(output));
    }

    private final RenderConfiguration configuration;
    private final JtwigContext context;
    private final RenderStream renderStream;

    private RenderContext(RenderConfiguration configuration, JtwigContext context, RenderStream renderStream) {
        this.configuration = configuration;
        this.context = context;
        this.renderStream = renderStream;
    }

    public void write(byte[] bytes) throws IOException {
        renderStream.write(bytes);
    }

    public JtwigContext model() {
        return context;
    }

    public RenderStream renderStream () {
        return this.renderStream;
    }

    public RenderContext newRenderContext(OutputStream outputStream) {
        return new RenderContext(configuration, context, new RenderStream(outputStream));
    }

    public RenderConfiguration configuration() {
        return configuration;
    }

    public void renderConcurrent(Renderable content) throws IOException, RenderException {
        renderStream.renderConcurrent(content, fork());
    }

    private RenderContext fork() throws IOException {
        return new RenderContext(configuration, context, renderStream.fork());
    }
}
