/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.extension.core.functions;

import java.io.ByteArrayOutputStream;
import org.jtwig.Environment;
import org.jtwig.content.model.Template;
import org.jtwig.exception.RenderException;
import org.jtwig.extension.api.functions.Function;
import org.jtwig.extension.api.functions.FunctionException;
import org.jtwig.extension.core.tokenparsers.model.Block;
import org.jtwig.render.RenderContext;

public class ParentFunction implements Function {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object... args) throws FunctionException {
        // Start by determining which block we're currently in
        Template.Compiled currentTemplate = ctx.getTemplateStack().peek();
        int idx = ctx.getTemplateStack().indexOf(currentTemplate);
        if (idx <= 0) {
            throw new FunctionException("Cannot call parent() function from a template that does not extend another.");
        }
        
        Template.Compiled parentTemplate = ctx.getTemplateStack().elementAt(idx);
        Block.CompiledBlock block = parentTemplate.block(ctx.getBlockStack().peek().name());
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            block.render(ctx.newRenderContext(baos), true);
        } catch (RenderException ex) {
            throw new FunctionException(ex);
        }
        return baos.toString();
    }
    
}
