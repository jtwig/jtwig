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

package org.jtwig.render.stream;

import org.jtwig.content.api.Renderable;
import org.jtwig.render.RenderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RenderTask implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(RenderTask.class);

    private Renderable content;
    private RenderContext context;

    RenderTask(Renderable content, RenderContext context) {
        this.content = content;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            content.render(context);
            context.renderStream().close().merge().notifyTaskFinished();
        } catch (Exception e) {
            logger.error("[Concurrent Beta] Rendering failed", e);
        }
    }
}
