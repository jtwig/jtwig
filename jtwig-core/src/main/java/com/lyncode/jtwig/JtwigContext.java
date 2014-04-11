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

package com.lyncode.jtwig;

import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.functions.parameters.GivenParameters;
import com.lyncode.jtwig.functions.parameters.resolve.exceptions.ResolveException;
import com.lyncode.jtwig.functions.repository.FunctionResolver;

import java.lang.reflect.InvocationTargetException;

import static com.lyncode.jtwig.types.Undefined.UNDEFINED;

public class JtwigContext {

    private static final String MODEL = "model";

    public static JtwigContext context () {
        return new JtwigContext();
    }

    private FunctionResolver functionRepository;
    private JtwigModelMap modelMap;

    public JtwigContext(JtwigModelMap modelMap, FunctionResolver functionRepository) {
        this.functionRepository = functionRepository;
        this.modelMap = modelMap;
    }

    public JtwigContext(JtwigModelMap modelMap) {
        this.functionRepository = new FunctionResolver();
        this.modelMap = modelMap;
    }

    public JtwigContext() {
        this.functionRepository = new FunctionResolver();
        this.modelMap = new JtwigModelMap();
    }

    public JtwigContext withModelAttribute(String key, Object value) {
        this.modelMap.add(key, value);
        return this;
    }

    public Object map(String key) {
        if (MODEL.equals(key))
            return modelMap;
        else {
            if (modelMap.containsKey(key))
                return modelMap.get(key);
            else
                return UNDEFINED;
        }
    }

    public void set(String key, Object value) {
        modelMap.add(key, value);
    }

    public Object executeFunction(String name, GivenParameters parameters) throws FunctionException {
        try {
            return functionRepository.get(name, parameters).execute();
        } catch (InvocationTargetException | IllegalAccessException | ResolveException e) {
            throw new FunctionException(e);
        }
    }
}
