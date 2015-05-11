package org.jtwig.functions.parameters.resolve.api;

import com.google.common.base.Optional;
import org.jtwig.functions.reflection.JavaMethodParameter;

public interface ParameterResolver {
    Optional<Value> resolve (JavaMethodParameter parameter);

    public static class Value {
        private final Object value;

        public Value(Object value) {
            this.value = value;
        }

        public Object value() {
            return value;
        }
    }
}
