/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.jtwig.functions.builtin;

import com.lyncode.jtwig.functions.annotations.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.Parameter;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.functions.util.ObjectIterator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.lyncode.jtwig.functions.util.ObjectUtils.compare;

public class ListFunctions {
    @JtwigFunction(name = "batch")
    public List<List<Object>> batch (@Parameter Object input, @Parameter int groupSize) {
        ObjectIterator iterator = new ObjectIterator(input);
        List<List<Object>> result = new ArrayList<>();
        while (iterator.hasNext()) {
            List<Object> batch = new ArrayList<Object>();
            for (int i=0;i<groupSize;i++) {
                if (iterator.hasNext())
                    batch.add(iterator.next());
            }
            result.add(batch);
        }
        return result;
    }

    @JtwigFunction(name = "batch")
    public List<List<Object>> batch (@Parameter Object input, @Parameter int groupSize, @Parameter Object padding) {
        ObjectIterator iterator = new ObjectIterator(input);
        List<List<Object>> result = new ArrayList<>();
        while (iterator.hasNext()) {
            List<Object> batch = new ArrayList<Object>();
            for (int i=0;i<groupSize;i++) {
                if (iterator.hasNext())
                    batch.add(iterator.next());
                else
                    batch.add(padding);
            }
            result.add(batch);
        }
        return result;
    }

    @JtwigFunction(name = "concat", aliases = { "concatenate" })
    public String concatenate (@Parameter String... pieces) {
        StringBuilder builder = new StringBuilder();
        for (String piece : pieces) {
            if (piece != null)
                builder.append(piece);

        }
        return builder.toString();
    }

    @JtwigFunction(name = "join")
    public String join (@Parameter Object input, @Parameter String separator) {
        List<String> pieces = new ArrayList<>();
        ObjectIterator iterator = new ObjectIterator(input);
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (next == null)
                pieces.add("");
            else
                pieces.add(next.toString());
        }
        return StringUtils.join(pieces, separator);
    }

    @JtwigFunction(name = "join")
    public String join (@Parameter Object input) {
        return join(input, "");
    }

    @JtwigFunction(name = "merge")
    public Object merge (@Parameter Object first, @Parameter Object... rest) {
        if (first instanceof Iterable)
            return mergeList(first, rest);
        else if (first instanceof Map)
            return mergeMap(first, rest);
        else // is array (precondition)
            return mergeArray(first, rest);
    }

    @JtwigFunction(name = "length")
    public int length (@Parameter Iterable input) {
        Iterator iterator = input.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }

    @JtwigFunction(name = "first")
    public Object first (@Parameter List input) {
        if (input.isEmpty()) return null;
        return input.get(0);
    }

    @JtwigFunction(name = "last")
    public Object last (@Parameter List input) {
        if (input.isEmpty()) return null;
        return input.get(input.size() - 1);
    }

    @JtwigFunction(name = "reverse")
    public List reverse (@Parameter Object input) {
        ObjectIterator objectIterator = new ObjectIterator(input);
        List<Object> result = new ArrayList<Object>();
        while (objectIterator.hasNext())
            result.add(objectIterator.next());

        Collections.reverse(result);
        return result;
    }

    @JtwigFunction(name = "slice")
    public Object slice(@Parameter Object input, @Parameter int begin, @Parameter int length) throws FunctionException {

        if (input instanceof String) {
            String value = (String) input;
            if (value.length() < begin) return "";
            return value.substring(begin, Math.min(value.length(), begin + length));
        }

        ObjectIterator iterator = new ObjectIterator(input);
        List list = new ArrayList();
        int i = 0;
        while (iterator.hasNext()) {
            if (i >= begin && i < begin + length)
                list.add(iterator.next());
            else
                iterator.next();
            i++;
        }

        if (input instanceof Iterable)
            return list;
        else
            return list.toArray();
    }

    @JtwigFunction(name = "sort")
    public List sort (@Parameter List input) {
        Collections.sort(input);
        return input;
    }
    
    @JtwigFunction(name = "max")
    public Object max (@Parameter Object ... values) {
        Object result = values[0];
        values = ArrayUtils.remove(values, 0);
        for(Object value : values) {
            int cmp = compare(result, value);
            if(cmp < 0) {
                result = value;
            }
        }
        return result;
    }
    
    @JtwigFunction(name = "min")
    public Object min (@Parameter Object ... values) {
        Object result = values[0];
        values = ArrayUtils.remove(values, 0);
        for(Object value : values) {
            int cmp = compare(result, value);
            if(cmp > 0) {
                result = value;
            }
        }
        return result;
    }

    private Object mergeArray(Object first, Object... arguments) {
        List<Object> result = new ArrayList<>();
        for (Object obj : (Object[]) first)
            result.add(obj);
        for (Object obj : arguments) {
            if (obj == null) continue;
            Object[] list = (Object[]) obj;
            for (Object value : list) {
                result.add(value);
            }
        }
        return result.toArray();
    }

    private Object mergeMap(Object first, Object... arguments) {
        Map<Object, Object> result;
        if (first instanceof TreeMap)
            result = new TreeMap<Object, Object>();
        else
            result = new HashMap<Object, Object>();
        result.putAll((Map) first);
        for (Object obj : arguments) {
            if (obj == null) continue;
            result.putAll((Map) obj);
        }
        return result;
    }

    private Object mergeList(Object first, Object... arguments) {
        List<Object> result = new ArrayList<Object>();
        result.addAll((List) first);
        for (Object obj : arguments) {
            if (obj == null) continue;
            result.addAll((List) obj);
        }
        return result;
    }
}
