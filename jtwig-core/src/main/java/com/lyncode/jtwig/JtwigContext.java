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

import com.lyncode.jtwig.functions.JtwigFunction;
import com.lyncode.jtwig.functions.exceptions.FunctionNotFoundException;
import com.lyncode.jtwig.functions.repository.AbstractFunctionRepository;
import com.lyncode.jtwig.functions.repository.DefaultFunctionRepository;

public class JtwigContext {

    private static final String MODEL = "model";

    public static JtwigContext context () {
        return new JtwigContext();
    }

    private AbstractFunctionRepository functionRepository;
    private JtwigModelMap modelMap;

    public JtwigContext(JtwigModelMap modelMap, AbstractFunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
        this.modelMap = modelMap;
    }

    public JtwigContext(JtwigModelMap modelMap) {
        this.functionRepository = new DefaultFunctionRepository();
        this.modelMap = modelMap;
    }

    public JtwigContext() {
        this.functionRepository = new DefaultFunctionRepository();
        this.modelMap = new JtwigModelMap();
    }

    public JtwigContext withFunction(String name, JtwigFunction function) {
        this.functionRepository.add(function, name);
        return this;
    }

    public JtwigContext withModelAttribute(String key, Object value) {
        this.modelMap.add(key, value);
        return this;
    }

    public JtwigFunction function(String name) throws FunctionNotFoundException {
        return functionRepository.retrieve(name);
    }

    public Object map(String key) {
        if (MODEL.equals(key))
            return modelMap;
        else
            return modelMap.get(key);
    }

    public void set(String key, Object value) {
        modelMap.add(key, value);
    }
}
