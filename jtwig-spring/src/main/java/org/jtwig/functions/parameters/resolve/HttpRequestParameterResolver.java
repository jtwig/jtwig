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

package org.jtwig.functions.parameters.resolve;

import com.google.common.base.Optional;
import org.jtwig.functions.parameters.resolve.api.ParameterResolver;
import org.jtwig.functions.reflection.JavaMethodParameter;
import org.jtwig.util.LocalThreadHolder;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestParameterResolver implements ParameterResolver {
    @Override
    public Optional<Value> resolve(JavaMethodParameter parameter) {
        if (parameter.type().isAssignableFrom(HttpServletRequest.class))
            return Optional.of(new Value(LocalThreadHolder.getServletRequest()));
        return Optional.absent();
    }
}
