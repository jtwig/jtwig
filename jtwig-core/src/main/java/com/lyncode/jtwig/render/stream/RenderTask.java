package com.lyncode.jtwig.render.stream;

import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.render.RenderContext;
import org.apache.log4j.Logger;

/**
 * Created by rsilva on 3/20/14.
 */
class RenderTask implements Runnable {
    private static Logger logger = Logger.getLogger(RenderTask.class);

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
            context.renderStream().close();
            context.renderStream().merge();
            context.renderStream().notifyTaskFinished();
        } catch (Exception e) {
            logger.error("[Concurrent Beta] Rendering failed", e);
        }
    }
}
