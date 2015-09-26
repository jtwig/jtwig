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

package org.jtwig.extension.core.filters;

import java.util.Collection;
import java.util.Map;
import org.jtwig.Environment;
import org.jtwig.extension.api.filters.Filter;
import org.jtwig.render.RenderContext;

public class KeysFilter implements Filter {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object left, Object... args) {
        if (left instanceof Map) {
            return ((Map)left).keySet();
        }
        if (!(left instanceof Collection) || ((Collection)left).isEmpty()) {
            return new Integer[0];
        }
        
        int size = ((Collection)left).size() - 1;
        Integer[] result = new Integer[size+1];
        for (int i = 0; i <= size; i++) {
            result[i] = i;
        }
        return result;
    }
    
}