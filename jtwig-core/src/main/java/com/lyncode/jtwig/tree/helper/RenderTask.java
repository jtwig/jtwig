package com.lyncode.jtwig.tree.helper;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.tree.api.Content;

import java.io.IOException;

/**
 * Created by rsilva on 3/20/14.
 */
class RenderTask implements Runnable {

    private RenderStream renderStream;
    private Content content;
    private JtwigContext context;

    RenderTask(RenderStream renderStream, Content content, JtwigContext context) {
        this.renderStream = renderStream;
        this.content = content;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            content.render(renderStream, context);
            renderStream.close();
            renderStream.merge();
            renderStream.notifyTaskFinished();
        } catch (RenderException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
