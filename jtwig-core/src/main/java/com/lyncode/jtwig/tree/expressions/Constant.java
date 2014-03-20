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

package com.lyncode.jtwig.tree.expressions;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.tree.api.Expression;

public class Constant<T> implements Expression {
    private T value;

    public Constant(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public boolean isInstanceOf (Class<?> clazzToTest) {
        return clazzToTest.isInstance(value);
    }

    public <S> S as (Class<S> type) {
        return type.cast(this.value);
    }

    public boolean isNull () {
        return value == null;
    }

    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        return value;
    }
}
