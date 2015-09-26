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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import org.jtwig.Environment;
import org.jtwig.exception.ResourceException;
import org.jtwig.extension.api.functions.Function;
import org.jtwig.extension.api.functions.FunctionException;
import org.jtwig.loader.Loader;
import org.jtwig.render.RenderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://twig.sensiolabs.org/doc/functions/source.html
 * 
 * According to the Twig docs, the source function returns the content of a
 * template without rendering it
 */
public class SourceFunction implements Function {
    private static final Logger LOGGER = LoggerFactory.getLogger(SourceFunction.class);

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object... args) throws FunctionException {
        if (args.length == 0 || args[0].toString().trim().length() == 0) {
            LOGGER.warn("Missing argument for source()");
            return null;
        }
        try {
            Loader.Resource resource = env.load(args[0].toString());
            return inputStreamToString(resource.source());
        } catch (IOException | ResourceException ex) {
            throw new FunctionException(ex);
        }
    }
    
    protected static String inputStreamToString(final InputStream is) throws UnsupportedEncodingException, IOException {
        final char[] buffer = new char[1024];
        final StringBuilder result = new StringBuilder();
        try (Reader in = new InputStreamReader(is, "UTF-8")) {
            while (true) {
                int read = in.read(buffer, 0, buffer.length);
                if (read < 0) {
                    break;
                }
                result.append(buffer, 0, read);
            }
        }
        return result.toString();
    }
    
}