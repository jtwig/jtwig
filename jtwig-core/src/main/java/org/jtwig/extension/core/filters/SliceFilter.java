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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jtwig.Environment;
import org.jtwig.extension.api.filters.Filter;
import org.jtwig.extension.api.filters.FilterException;
import org.jtwig.render.RenderContext;
import org.jtwig.types.Undefined;
import org.jtwig.util.TypeUtil;

public class SliceFilter implements Filter {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object left, Object... args) throws FilterException {
        if (left == null || left == Undefined.UNDEFINED) {
            return null;
        }
        
        int start = 0;
        if (args.length > 0) {
            start = TypeUtil.toLong(args[0]).intValue();
        }
        int length = Integer.MAX_VALUE;
        if (args.length > 1) {
            length = TypeUtil.toLong(args[1]).intValue();
        }
        
        if (left instanceof CharSequence || left instanceof Character
                || left instanceof Number) {
            String value = left.toString();
            if (value.length() < start) {
                return "";
            }
            start = resolveStart(start, value.length());
            int end = resolveEnd(start, length, value.length());
            return value.substring(start, end);
        }
        if (left instanceof Collection) {
            List list = new ArrayList((Collection)left);
            start = resolveStart(start, list.size());
            int end = resolveEnd(start, length, list.size());
            return list.subList(start, end);
        }
        if (left instanceof Map) {
            Map source = (Map)left;
            Map result = new LinkedHashMap();
            List keys = new ArrayList(source.keySet());
            start = resolveStart(start, keys.size());
            int end = resolveEnd(start, length, keys.size());
            keys = new ArrayList(keys.subList(start, end));
            for (Object key : keys) {
                result.put(key, source.get(key));
            }
            return result;
        }
        throw new FilterException("Unable to convert "+left.getClass().getName()+" to string");
    }
    
    protected int resolveStart(int requestedStart, int sourceLength) {
        if (requestedStart < 0) {
            return sourceLength + requestedStart;
        }
        return Math.min(requestedStart, sourceLength);
    }
    protected int resolveEnd(int start, int requestedLength, int sourceLength) {
        if (requestedLength < 0) {
            return sourceLength + requestedLength; // Adjust for end-index exclusivity
        }
        if (requestedLength == Integer.MAX_VALUE) {
            return sourceLength;
        }
        return Math.min(start + requestedLength, sourceLength);
    }
    
}