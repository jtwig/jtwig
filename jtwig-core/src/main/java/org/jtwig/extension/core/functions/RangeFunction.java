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
import org.jtwig.render.RenderContext;
import org.jtwig.util.ArrayUtil;
import org.jtwig.util.TypeUtil;

public class RangeFunction implements Function {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object... args) {
        assert args.length >= 2;
        
        int step = args.length > 2 ? TypeUtil.toLong(args[2]).intValue() : 1;
        
        return ArrayUtil.range(args[0], args[1], step);
    }
    
}