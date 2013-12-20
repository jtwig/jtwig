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

package com.lyncode.jtwig.services.impl.assets;

import com.lyncode.jtwig.exceptions.AssetResolveException;
import com.lyncode.jtwig.mvc.JtwigViewResolver;
import com.lyncode.jtwig.services.api.assets.AssetResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ViewResolver;

import java.io.File;

public class BaseAssetResolver implements AssetResolver {
    private String prefix;

    @Autowired
    private ViewResolver viewResolver;

    @Override
    public String resolve(String asset) throws AssetResolveException {
        if (prefix == null) prefix = "/";
        if (!(viewResolver instanceof JtwigViewResolver))
            throw new AssetResolveException("The view resolver must be a JtwigViewResolver");
        else {
            if (((JtwigViewResolver) viewResolver).hasTheme()) {
                return new File(new File(prefix, ((JtwigViewResolver) viewResolver).getTheme()), asset).getPath();
            } else {
                return new File(prefix, asset).getPath();
            }
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
