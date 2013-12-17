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

package com.lyncode.jtwig.tree.value;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.tree.api.Calculable;
import com.lyncode.jtwig.tree.helper.ElementList;

import java.util.ArrayList;
import java.util.List;

public class ExpressionList extends ElementList implements Calculable {
    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        List<Object> result = new ArrayList<Object>();
        for (Object obj : getList()) {
            if (obj instanceof Calculable)
                result.add(((Calculable) obj).calculate(context));
            else
                result.add(obj);
        }
        return result;
    }
}
