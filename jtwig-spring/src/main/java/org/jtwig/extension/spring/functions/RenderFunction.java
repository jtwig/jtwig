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

package org.jtwig.extension.spring.functions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.jtwig.Environment;
import org.jtwig.extension.api.functions.FunctionException;
import org.jtwig.render.RenderContext;
import org.jtwig.util.render.RenderHttpServletRequest;
import org.jtwig.util.render.RenderHttpServletResponse;
import org.springframework.http.HttpMethod;

public class RenderFunction extends AbstractSpringFunction {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, HttpServletRequest request, Object... args) throws FunctionException {
        if (args.length == 0 || args[0] == null || args[0].toString().isEmpty()) {
            throw new FunctionException("Render function requires a url.");
        }
        final String url = args[0].toString();
        final Map<String, String> parameters = new HashMap<>();
        
        if (args.length > 1 && args[1] != null && args[1] instanceof Map) {
            parameters.putAll((Map<String, String>)args[1]);
        }
        
        RenderHttpServletResponse responseWrapper = new RenderHttpServletResponse();
        RenderHttpServletRequest builder = new RenderHttpServletRequest(request)
                .to(url)
                .withMethod(HttpMethod.GET);

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            builder.withGetParameter(entry.getKey(), entry.getValue());
        }

        try {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(request.getServletPath());
            requestDispatcher.include(builder, responseWrapper);

            return responseWrapper.toString();
        } catch (ServletException | IOException e) {
            throw new FunctionException(e);
        }
    }

//    @JtwigFunction(name = "render")
//    public String render(HttpServletRequest request, @Parameter String url) throws FunctionException {
//        return render(request, url, new HashMap<String, String>());
//    }
//
//    @JtwigFunction(name = "render")
//    public String render(HttpServletRequest request, @Parameter String url, @Parameter Map<String, String> parameters) throws FunctionException {
//        RenderHttpServletResponse responseWrapper = new RenderHttpServletResponse();
//        RenderHttpServletRequest builder = new RenderHttpServletRequest(request)
//                .to(url)
//                .withMethod(GET);
//
//        for (Map.Entry<String, String> entry : parameters.entrySet()) {
//            builder.withGetParameter(entry.getKey(), entry.getValue());
//        }
//
//        try {
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher(request.getServletPath());
//            requestDispatcher.include(builder, responseWrapper);
//
//            return responseWrapper.toString();
//        } catch (ServletException | IOException e) {
//            throw new FunctionException(e);
//        }
//    }
    
}