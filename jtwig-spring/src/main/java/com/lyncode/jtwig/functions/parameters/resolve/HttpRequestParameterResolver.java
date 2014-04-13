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

package com.lyncode.jtwig.functions.parameters.resolve;

import com.lyncode.jtwig.functions.parameters.GivenParameters;
import com.lyncode.jtwig.functions.parameters.convert.api.ParameterConverter;
import com.lyncode.jtwig.functions.parameters.resolve.api.TypeMethodParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.exceptions.ResolveException;
import com.lyncode.jtwig.functions.parameters.resolve.model.MethodParameter;
import com.lyncode.jtwig.unit.util.LocalThreadHolder;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestParameterResolver implements TypeMethodParameterResolver {
    @Override
    public Class<?> resolveType() {
        return HttpServletRequest.class;
    }

    @Override
    public boolean canResolveParameter(MethodParameter javaParameter, GivenParameters templateGivenParameters, ParameterConverter converter) {
        return javaParameter.type().equals(resolveType());
    }

    @Override
    public Object resolveParameter(MethodParameter javaParameter, GivenParameters templateGivenParameters, ParameterConverter converter) throws ResolveException {
        return LocalThreadHolder.getServletRequest();
    }
}
