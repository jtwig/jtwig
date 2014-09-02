package com.lyncode.jtwig.functions.repository.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.lyncode.jtwig.functions.annotations.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.Parameter;
import com.lyncode.jtwig.functions.builtin.*;
import com.lyncode.jtwig.functions.parameters.input.InputParameters;
import com.lyncode.jtwig.functions.repository.api.FunctionRepository;
import com.lyncode.jtwig.functions.repository.model.Function;
import com.lyncode.jtwig.functions.util.SortedList;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Predicates.*;
import static com.google.common.collect.Range.lessThan;
import static com.lyncode.jtwig.functions.repository.model.Function.functionFrom;

public class MapFunctionRepository implements FunctionRepository {
    private final ConcurrentHashMap<String, Collection<Function>> repository = new ConcurrentHashMap<>();

    public MapFunctionRepository() {
        include(new BooleanFunctions());
        include(new DateFunctions());
        include(new ListFunctions());
        include(new MapFunctions());
        include(new MathFunctions());
        include(new NumberFunctions());
        include(new ObjectFunctions());
        include(new StringFunctions());
    }

    @Override
    public FunctionRepository include(Object instance) {
        for (Method method : instance.getClass().getDeclaredMethods()) {
            JtwigFunction annotation = method.getAnnotation(JtwigFunction.class);
            if (annotation != null) {
                this.add(annotation.name(), functionFrom(method).on(instance));
                this.aliases(annotation.name(), annotation.aliases());
            }
        }
        return this;
    }

    @Override
    public FunctionRepository add(String name, Function function) {
        synchronized (repository) {
            if (!repository.containsKey(name))
                repository.put(name, new SortedList<Function>());
        }
        repository.get(name).add(function);
        return this;
    }

    @Override
    public FunctionRepository aliases(String name, String[] aliases) {
        for (String alias : aliases) {
            repository.putIfAbsent(alias, repository.get(name));
        }
        return this;
    }

    @Override
    public boolean containsFunctionName(String name) {
        return repository.containsKey(name);
    }

    @Override
    public Collection<Function> retrieve(String name, InputParameters parameters) {
        Collection<Function> functions = repository.get(name);
        if (functions == null) return new ArrayList<>();
        return Collections2.filter(functions, or(
                and(isVarArg(), numberOfArgumentsAnnotatedWithParameter(lessThan(parameters.length()))),
                numberOfArgumentsAnnotatedWithParameter(equalTo(parameters.length()))
        ));
    }

    private Predicate<? super Function> isVarArg() {
        return new Predicate<Function>() {
            @Override
            public boolean apply(@Nullable Function input) {
                return input.method().isVarArgs();
            }
        };
    }

    private Predicate<? super Function> numberOfArgumentsAnnotatedWithParameter(final Predicate<Integer> integerPredicate) {
        return new Predicate<Function>() {
            @Override
            public boolean apply(@Nullable Function input) {
                int size = input.getParameterTypesWithAnnotation(Parameter.class).size();
                if (input.method().isVarArgs())
                    size--;
                return integerPredicate.apply(size);
            }
        };
    }


}
