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

package com.lyncode.jtwig.functions;

import com.lyncode.jtwig.exceptions.AssetResolveException;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.services.api.assets.AssetResolver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class Asset extends AutowiredFunction {
    @Autowired
    private AssetResolver assetResolver;

    @Autowired
    private HttpServletRequest request;

    @Override
    protected Object call(Object... arguments) throws FunctionException {
        if (arguments.length != 1) throw new FunctionException("Invalid number of arguments");
        try {
            return request.getContextPath() + assetResolver.resolve(arguments[0].toString());
        } catch (AssetResolveException e) {
            throw new FunctionException(e);
        }
    }
}
