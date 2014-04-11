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

package com.lyncode.jtwig.tree.expressions;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.parser.positioning.Position;
import com.lyncode.jtwig.tree.api.AbstractExpression;
import com.lyncode.jtwig.tree.api.Expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.join;

public class ValueMap extends AbstractExpression {
    private Map<String, Object> map = new LinkedHashMap<>();

    public ValueMap(Position position) {
        super(position);
    }

    public boolean add (String key, Object element) {
        map.put(key, element);
        return true;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public String toString () {
        List<String> parts = new ArrayList<String>();
        for (String k : map.keySet())
            parts.add(k+"->"+map.get(k));

        return join(parts, ", ");
    }

    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        Map<String, Object> result = new LinkedHashMap<>();
        for (String key : map.keySet()) {
            if (map.get(key) instanceof Expression)
                result.put(key, ((Expression) map.get(key)).calculate(context));
            else
                result.put(key, map.get(key));
        }
        return result;
    }
}
