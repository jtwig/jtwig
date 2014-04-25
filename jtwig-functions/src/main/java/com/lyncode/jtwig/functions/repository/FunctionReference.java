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

package com.lyncode.jtwig.functions.repository;

import com.lyncode.jtwig.functions.parameters.resolve.model.MethodInformation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FunctionReference implements Comparable<FunctionReference> {
    private final Method method;
    private final MethodInformation mi;
    private final Object instance;

    public FunctionReference(Method method, Object instance) {
        this.method = method;
        this.mi = new MethodInformation(method);
        this.instance = instance;
    }

    public Object execute (Object[] arguments) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(this.instance, arguments);
    }

    @Override
    public int compareTo(FunctionReference other) {
        if (mi.numberOfArgs() > 0 && other.mi.numberOfArgs() == this.mi.numberOfArgs()) {
            int compareResult = 0;
            for (int i = 0;i<mi.numberOfArgs();i++) {
                Class<?> type = this.mi.getType(i);
                Class<?> otherType = other.mi.getType(i);

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
        } else return Integer.compare(this.mi.numberOfArgs(), other.mi.numberOfArgs());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionReference that = (FunctionReference) o;

        return method.equals(that.method);
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }

    public Method getMethod() {
        return method;
    }

    public Object getInstance() {
        return instance;
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
}
