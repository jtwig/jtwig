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

package com.lyncode.jtwig.functions;

import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.util.LocalThreadHolder;
import org.springframework.beans.BeansException;

public abstract class AutowiredJtwigFunction implements JtwigFunction {
    public AutowiredJtwigFunction() {
        super();
    }

    public Object execute (Object... arguments) throws FunctionException {
        try {
            LocalThreadHolder.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
            return call(arguments);
        } catch (BeansException e) {
            throw new FunctionException(e);
        }
    }

    protected abstract Object call (Object... arguments) throws FunctionException;
}
