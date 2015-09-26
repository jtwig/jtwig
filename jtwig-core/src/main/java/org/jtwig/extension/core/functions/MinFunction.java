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

import org.jtwig.Environment;
import org.jtwig.extension.api.functions.Function;
import static org.jtwig.functions.util.ObjectUtils.compare;
import org.jtwig.render.RenderContext;
import org.jtwig.util.ArrayUtil;

public class MinFunction implements Function {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object... args) {
        if (args.length == 1) {
            args = ArrayUtil.toArray(args[0]);
        }
        Object result = args[0];
        args = ArrayUtil.toArray(args);
        for(Object value : args) {
            int cmp = compare(result, value);
            if(cmp > 0) {
                result = value;
            }
        }
        return result;
    }
    
}