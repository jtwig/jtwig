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
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.RenderException;
import org.jtwig.exception.ResourceException;
import org.jtwig.extension.api.functions.Function;
import org.jtwig.extension.api.functions.FunctionException;
import org.jtwig.render.RenderContext;

public class IncludeFunction implements Function {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object... args) throws FunctionException {
        if (args.length == 0 || args[0] == null || args[0].toString().trim().isEmpty()) {
            return null;
        }
        Template template = getTemplate(args[0], env);
        if (template == null) {
            return null;
        }
        
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            RenderContext isolated = ctx.newRenderContext(baos);
            env.compile(template, template.getPosition().getResource()).render(isolated);
            return baos.toString();
        } catch (CompileException | RenderException ex) {
            throw new FunctionException(ex);
        }
    }
    
    protected Template getTemplate(final Object templateObj, final Environment env) throws FunctionException {
        if (templateObj instanceof Template) {
            return (Template)templateObj;
        }
        if (templateObj instanceof CharSequence) {
            try {
                return env.parse(templateObj.toString());
            } catch (ResourceException | ParseException ex) {
                return null;
            }
        }
        if (templateObj instanceof Iterable) {
            for (Object obj : ((Iterable)templateObj)) {
                Template tpl = getTemplate(obj, env);
                if (tpl != null) {
                    return tpl;
                }
            }
            return null;
        }
        throw new FunctionException("The include() function expects a template instance or template name as the first argument.");
    }
    
}