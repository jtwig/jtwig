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

package com.lyncode.jtwig.mvc;

import com.lyncode.jtwig.functions.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.JtwigFunctionDeclaration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;

public class BeanFunctionDeclarationProcessor implements BeanPostProcessor {
    private List<JtwigFunction> processed = new ArrayList<JtwigFunction>();
    private JtwigViewResolver viewResolver;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (JtwigFunction.class.isAssignableFrom(bean.getClass())) {
            addToRepository((JtwigFunction) bean);
        }
        if (JtwigViewResolver.class.isAssignableFrom(bean.getClass())) {
            this.viewResolver = (JtwigViewResolver) bean;
            this.viewResolver.getFunctionRepository()
                    .add(processed.toArray(new JtwigFunction[processed.size()]));
        }
        return bean;
    }

    private void addToRepository(JtwigFunction function) {
        if (viewResolver != null)
            viewResolver.getFunctionRepository().add(function);
        else
            processed.add(function);
    }

    private String getName(JtwigFunctionDeclaration declaration, String functionName) {
        if (declaration.name().equals(""))
            return functionName;
        else
            return declaration.name();
    }
}
