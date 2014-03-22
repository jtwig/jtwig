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

package com.lyncode.jtwig.spring;

import com.lyncode.jtwig.controller.DynamicController;
import com.lyncode.jtwig.functions.test.SimpleJtwigFunction;
import com.lyncode.jtwig.mvc.JtwigViewResolver;
import com.lyncode.jtwig.services.api.ModelMapFiller;
import com.lyncode.jtwig.services.api.ViewShownResolver;
import com.lyncode.jtwig.services.api.assets.AssetResolver;
import com.lyncode.jtwig.services.impl.InMemoryMessageSource;
import com.lyncode.jtwig.services.impl.TestModelMapFiller;
import com.lyncode.jtwig.services.impl.TestViewShownResolver;
import com.lyncode.jtwig.services.impl.assets.BaseAssetResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import static java.util.Locale.ENGLISH;

@Configuration
@ComponentScan(basePackageClasses = { DynamicController.class })
@EnableWebMvc
public class WebappConfig extends WebMvcConfigurerAdapter {
    @Bean
    public ViewShownResolver viewShownResolver () {
        return new TestViewShownResolver();
    }


    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public LocaleResolver localeResolver () {
        return new FixedLocaleResolver(ENGLISH);
    }

    @Bean
    public MessageSource messageSource () {
        return new InMemoryMessageSource();
    }

    @Bean
    public ModelMapFiller modelMapFiller () {
        return new TestModelMapFiller();
    }

    @Bean
    public AssetResolver assetResolver () {
        BaseAssetResolver baseAssetResolver = new BaseAssetResolver();
        baseAssetResolver.setPrefix("public");
        return baseAssetResolver;
    }

    @Bean
    public ViewResolver viewResolver() {
        JtwigViewResolver jtwigViewResolver = new JtwigViewResolver();
        jtwigViewResolver.setPrefix("/WEB-INF/views/");
        jtwigViewResolver.setSuffix(".twig.html");
        jtwigViewResolver.setTheme("default");
        jtwigViewResolver.addFunctionPackages(
                SimpleJtwigFunction.class.getPackage().getName()
        );
        return jtwigViewResolver;
    }
}
