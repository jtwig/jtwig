package org.jtwig.functions.repository.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Function implements Comparable<Function> {
    public static FunctionBuilder functionFrom (Method method) {
        return new FunctionBuilder(method);
    }

    private final Object holder;
    private final Method method;

    private Function(Object holder, Method method) {
        this.holder = holder;
        this.method = method;
    }

    public Method method() {
        return method;
    }

    public Object holder() {
        return holder;
    }

    @Override
    public int compareTo(Function other) {
        if (numberOfArguments() > 0 && other.numberOfArguments() == numberOfArguments()) {
            int compareResult = 0;
            for (int i = 0;i< numberOfArguments();i++) {
                Class<?> type = getType(i);
                Class<?> otherType = other.getType(i);

                if (type.equals(String.class) && otherType.equals(Object.class)) {
                    compareResult--;
                    continue;
                } else if (type.equals(Object.class) && otherType.equals(String.class)) {
                    compareResult++;
                    continue;
                }


                if (type.equals(String.class))
                    type = Object.class;
                if (otherType.equals(String.class))
                    otherType = Object.class;

                if (type.equals(otherType))
                    continue;
                if (type.isAssignableFrom(otherType))
                    compareResult++;
                else if (otherType.isAssignableFrom(type))
                    compareResult--;
            }
            return compareResult;
        } else return Integer.compare(numberOfArguments(), other.numberOfArguments()) * -1;
    }

    private Class<?> getType(int i) {
        return method.getParameterTypes()[i];
    }

    private int numberOfArguments() {
        return method.getParameterTypes().length;
    }


    public List<Class<?>> getParameterTypesWithAnnotation(Class<? extends Annotation> parameterClass) {
        List<Class<?>> result = new ArrayList<>();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0;i<parameterAnnotations.length;i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            for (Annotation annotation : parameterAnnotation) {
                if (annotation.annotationType().equals(parameterClass)) {
                    result.add(method.getParameterTypes()[i]);
                    break;
                }
            }
        }
        return result;
    }

    public static class FunctionBuilder {
        private Method method;

        public FunctionBuilder(Method method) {
            this.method = method;
        }

        public Function on (Object instance) {
            return new Function(instance, method);
        }
    }
}
