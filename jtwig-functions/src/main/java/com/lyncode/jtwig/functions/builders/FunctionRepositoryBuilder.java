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

package com.lyncode.jtwig.functions.builders;

import com.lyncode.jtwig.functions.JtwigFunction;
import com.lyncode.jtwig.functions.repository.AbstractFunctionRepository;
import com.lyncode.jtwig.functions.repository.DefaultFunctionRepository;

public class FunctionRepositoryBuilder {
    public static FunctionRepositoryBuilder aFunctionRepository () {
        return new FunctionRepositoryBuilder();
    }
    public static FunctionRepositoryBuilder aFunctionRepositoryExtending(AbstractFunctionRepository inherits) {
        return new FunctionRepositoryBuilder(inherits);
    }

    private AbstractFunctionRepository inherits;

    public FunctionRepositoryBuilder () {
        this(new DefaultFunctionRepository());
    }

    public FunctionRepositoryBuilder (AbstractFunctionRepository inherits) {
        this.inherits = inherits;
    }

    public FunctionRepositoryBuilder withFunction (JtwigFunction function) {
        this.inherits.add(function);
        return this;
    }

    public FunctionRepositoryBuilder withFunction (JtwigFunction function, String name, String... aliases) {
        this.inherits.add(function, name, aliases);
        return this;
    }

    public AbstractFunctionRepository build () {
        return this.inherits;
    }
}
