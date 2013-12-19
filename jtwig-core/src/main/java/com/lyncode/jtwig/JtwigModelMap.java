/**
 * Copyright 2012 Lyncode
 *
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

package com.lyncode.jtwig;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JtwigModelMap {
    private Map<String, Object> model = new HashMap<String, Object>();

    public JtwigModelMap add (String key, Object object) {
        model.put(key, object);
        return this;
    }

    public boolean has (String key) {
        return model.containsKey(key);
    }

    public Object get (String key) {
        return model.get(key);
    }

    public JtwigModelMap add(Map<String, Object> model) {
        model.putAll(model);
        return this;
    }

    public String toString () {
        StringBuilder builder = new StringBuilder().append("{");
        Iterator<String> keys = model.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            builder.append(key).append(" = ").append(model.get(key));
            if (keys.hasNext()) builder.append(", ");
        }
        builder.append("}");
        return builder.toString();
    }
}
