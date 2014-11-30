package org.jtwig.addons.tag;

import com.google.common.base.Function;
import org.jtwig.addons.AddonModel;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.render.RenderContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Tag extends AddonModel<Tag> {
    private final Function<String,String> transformation;

    public Tag(Function<String, String> transformation) {
        this.transformation = transformation;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        return new Compiled(super.compile(context), transformation);
    }


    private static class Compiled implements Renderable {
        private final Renderable content;
        private final Function<String, String> transformation;

        private Compiled(Renderable content, Function<String, String> transformation) {
            this.content = content;
            this.transformation = transformation;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            content.render(context.newRenderContext(outputStream));
            String result = this.transformation.apply(outputStream.toString());
            try {
                context.write(result.getBytes());
            } catch (IOException e) {
                throw new RenderException(e);
            }
        }
    }
}
