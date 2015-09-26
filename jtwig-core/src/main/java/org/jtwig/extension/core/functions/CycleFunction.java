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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.jtwig.Environment;
import org.jtwig.extension.api.functions.Function;
import org.jtwig.extension.api.functions.FunctionException;
import org.jtwig.render.RenderContext;
import org.jtwig.util.ObjectIterator;

public class CycleFunction implements Function {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object... args) throws FunctionException {
        if (args.length < 2) {
            throw new FunctionException("Cycle function requires two arguments (collection, position)");
        }
        
        if (!(args[1] instanceof Number)) {
            throw new FunctionException("Cycle function requires a numeric position");
        }
        Integer pos = ((Number)args[1]).intValue();
        
        if (args[0] instanceof List) {
            if (((List)args[0]).isEmpty()) {
                return null;
            }
            return ((List)args[0]).get(pos % ((List)args[0]).size());
        }
        if (args[0] instanceof Collection || args[0] instanceof Map) {
            ObjectIterator it = new ObjectIterator(args[0]);
            if (it.size() > 0) {
                int idx = pos % it.size();
                for (int i = 0; it.hasNext(); i++) {
                    Object obj = it.next();
                    if (i == idx) {
                        return obj;
                    }
                }
            }
            return null;
        }
        return args[0];
    }
    
}