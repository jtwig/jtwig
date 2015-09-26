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

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jtwig.Environment;
import org.jtwig.extension.api.filters.Filter;
import org.jtwig.extension.api.filters.FilterException;
import org.jtwig.render.RenderContext;
import org.jtwig.util.TypeUtil;

public class ReverseFilter implements Filter {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object left, Object... args) throws FilterException {
        if (left instanceof Collection) {
            List result = new ArrayList((Collection)left);
            Collections.reverse(result);
            return result;
        }
        if (left instanceof Map) {
            Map source = (Map)left;
            LinkedHashMap result = new LinkedHashMap();
            List keys = new ArrayList(source.keySet());
            Collections.reverse(keys);
            for (Object key : keys) {
                result.put(key, source.get(key));
            }
            return result;
        }
        if (left instanceof Number) {
            left = left.toString();
        }
        if (left instanceof CharSequence) {
            return new StringBuilder(left.toString()).reverse().toString();
        }
        throw new FilterException("Unable to convert "+left.getClass().getName()+" to string");
    }
    
}