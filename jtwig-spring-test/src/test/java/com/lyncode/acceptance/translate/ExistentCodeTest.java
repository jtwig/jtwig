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

package com.lyncode.acceptance.translate;

import com.lyncode.acceptance.AbstractViewTest;
import com.lyncode.jtwig.services.impl.InMemoryMessageSource;
import org.hamcrest.Matcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import static org.hamcrest.CoreMatchers.is;

public class ExistentCodeTest extends AbstractViewTest {
    @Autowired
    private MessageSource messageSource;

    private InMemoryMessageSource getMessageSource () {
        return (InMemoryMessageSource) messageSource;
    }

    @Override
    protected void prepare() {
        getMessageSource().add("test", "Hi {0}");
    }

    @Override
    protected String view() {
        return "translate/existent-code";
    }

    @Override
    protected Matcher<? super String> matches() {
        return is("Hi JTwig");
    }
}
