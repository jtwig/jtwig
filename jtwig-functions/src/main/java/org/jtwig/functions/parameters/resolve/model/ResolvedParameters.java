package org.jtwig.functions.parameters.resolve.model;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.jtwig.functions.parameters.input.InputParameters;
import org.jtwig.functions.parameters.resolve.api.ParameterResolver;
import org.jtwig.functions.reflection.JavaMethodParameter;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Collections2.filter;

public class ResolvedParameters {
    private final List<SpecificationAndValue> parameters = new ArrayList<>();

    public ResolvedParameters(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0;i< parameterTypes.length;i++) {
            this.parameters.add(i, new SpecificationAndValue(new JavaMethodParameter(i, method)));
        }
    }

    public ResolvedParameters resolve(JavaMethodParameter parameter, Object value) {
        parameters.get(parameter.position()).value = Optional.of(new ParameterResolver.Value(value));
        return this;
    }

    public boolean isFullyResolved() {
        return filter(parameters, valueIsAbsent()).isEmpty();
    }

    public Object[] values() {
        return Collections2.transform(parameters, extractValues())
                .toArray();
    }

    public Collection<JavaMethodParameter> unresolvedParameters() {
        return Collections2.transform(filter(parameters, valueIsAbsent()), extractSpecification());
    }

    private Function<SpecificationAndValue, JavaMethodParameter> extractSpecification() {
        return new Function<SpecificationAndValue, JavaMethodParameter>() {
            @Nullable
            @Override
            public JavaMethodParameter apply(@Nullable SpecificationAndValue input) {
                return input.specification;
            }
        };
    }

    private Predicate<SpecificationAndValue> valueIsAbsent() {
        return new Predicate<SpecificationAndValue>() {
            @Override
            public boolean apply(@Nullable SpecificationAndValue input) {
                return !input.value.isPresent();
            }
        };
    }

    private Function<? super SpecificationAndValue, Object> extractValues() {
        return new Function<SpecificationAndValue, Object>() {
            @Nullable
            @Override
            public Object apply(@Nullable SpecificationAndValue input) {
                return input.value.get().value();
            }
        };
    }

    public boolean resolvableBy(InputParameters parameters) {
        return true;
    }

    private class SpecificationAndValue {
        private final JavaMethodParameter specification;
        private Optional<ParameterResolver.Value> value;

        private SpecificationAndValue(JavaMethodParameter specification) {
            this.specification = specification;
            this.value = Optional.absent();
        }
    }
}
