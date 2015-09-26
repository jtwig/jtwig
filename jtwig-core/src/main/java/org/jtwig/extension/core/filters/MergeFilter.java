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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jtwig.Environment;
import org.jtwig.extension.api.filters.Filter;
import org.jtwig.extension.api.filters.FilterException;
import org.jtwig.render.RenderContext;
import org.jtwig.util.ObjectIterator;

public class MergeFilter implements Filter {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object left, Object... args) throws FilterException {
        assert args.length > 0;
        
        if (!(args[0] instanceof Iterable) && !(args[0] instanceof Map) && !args[0].getClass().isArray()) {
            throw new FilterException("The merge filter only works with arrays, collections, and maps");
        }
        
        if (left instanceof Iterable) {
            return mergeList((Iterable)left, args);
        }
        if (left instanceof Map) {
            return mergeMap((Map)left, args);
        }
        return mergeArray(left, args);
    }

    private Object mergeArray(Object first, Object... args) {
        List<Object> result = new ArrayList<>();
        result.addAll(Arrays.asList((Object[]) first));
        ObjectIterator it = new ObjectIterator(args[0]);
        for (Object val : it) {
            result.add(val);
        }
        return result.toArray();
    }

    private Object mergeMap(Object first, Object... args) {
        Map<Object, Object> result;
        if (first instanceof TreeMap) {
            result = new TreeMap((Map)first);
        } else {
            result = new LinkedHashMap((Map)first);
        }
        
        if (args[0] instanceof Map) {
            result.putAll((Map) args[0]);
        } else {
            int i = 0;
            for (Object val : new ObjectIterator(args[0])) {
                result.put(String.valueOf(i), val);
                i++;
            }
        }
        return result;
    }

    private Object mergeList(Object first, Object... args) {
        List<Object> result = new ArrayList<>();
        result.addAll((List) first);
        if (args[0] instanceof Map) {
            args[0] = ((Map)args[0]).values();
        } else if (args[0].getClass().isArray()) {
            args[0] = Arrays.asList((Object[])args[0]);
        }
        result.addAll((Collection)args[0]);
        return result;
    }
    
}