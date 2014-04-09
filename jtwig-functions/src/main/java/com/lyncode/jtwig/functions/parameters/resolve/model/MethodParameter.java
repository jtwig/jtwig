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

package com.lyncode.jtwig.functions.parameters.resolve.model;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;

public class MethodParameter {
    private static boolean hasAnnotation(Annotation[] annotations, Class<?> parameterClass) {
        for (Annotation annotation : annotations)
            if (annotation.annotationType().equals(parameterClass))
                return true;

        return false;
    }

    private final Object instance;
    private final Method method;
    private final int position;

    public MethodParameter(Object instance, Method method, int position) {
        this.instance = instance;
        this.method = method;
        this.position = position;
    }

    public boolean hasAnnotation() {
        Annotation[] annotations = method.getParameterAnnotations()[position];
        return annotations != null && annotations.length > 0;
    }

    public boolean hasAnnotation(Class<?> parameterClass) {
        Annotation[] annotations = method.getParameterAnnotations()[position];
        return hasAnnotation(annotations, parameterClass);
    }

    public Collection<Class<? extends Annotation>> annotations() {
        return Collections2.transform(asList(method.getParameterAnnotations()[position]), new Function<Annotation, Class<? extends Annotation>>() {
            @Override
            public Class<? extends Annotation> apply(Annotation input) {
                return input.annotationType();
            }
        });
    }

    public Class<?> type () {
        return method.getParameterTypes()[position];
    }

    public boolean hasType(Class<?> type) {
        return type.equals(type());
    }

    @Override
    public String toString() {
        return position + " of method "+method.getName()+" of class "+instance.getClass().getName();
    }

    public <T extends Annotation> T annotation(Class<T> annotationType) {
        for (Annotation annotation : method.getParameterAnnotations()[position]) {
            if (annotation.annotationType().equals(annotationType))
                return annotationType.cast(annotation);
        }
        return null;
    }

    public int positionOf(Class<? extends Annotation> annotationClass) {
        int counter = 0;
        for (int i = 0;i<position;i++)
            if (hasAnnotation(method.getParameterAnnotations()[i], annotationClass))
                counter++;

        return counter;
    }

    public boolean isVarArg () {
        int length = method.getParameterTypes().length;
        return (position == length - 1) &&
                method.getParameterTypes()[position].isArray();
    }

    public boolean isNullable() {
        List<? extends Class> nativeTypes = asList(
                Integer.TYPE,
                Long.TYPE,
                Float.TYPE,
                Boolean.TYPE,
                Double.TYPE,
                Byte.TYPE
        );
        return !nativeTypes.contains(type());
    }
}
