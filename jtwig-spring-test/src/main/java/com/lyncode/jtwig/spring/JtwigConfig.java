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

package com.lyncode.jtwig.spring;

import com.lyncode.jtwig.functions.SimpleJtwigFunction;
import com.lyncode.jtwig.mvc.AbstractJtwigConfigurerAdapter;
import com.lyncode.jtwig.mvc.JtwigViewResolver;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackageClasses = SimpleJtwigFunction.class)
@Configuration
public class JtwigConfig extends AbstractJtwigConfigurerAdapter {
    @Override
    protected void configure(JtwigViewResolver jtwigViewResolver) {
        jtwigViewResolver.setPrefix("/WEB-INF/views/");
        jtwigViewResolver.setSuffix(".twig.html");
        jtwigViewResolver.setTheme("default");
    }
}
