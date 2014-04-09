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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MethodInformation {
    private final Method method;
    private Object firstParameter;

    public MethodInformation(Method method) {
        this.method = method;
    }


    public int countParametersWithAnnotation(Class<? extends Annotation> annotationClass) {
        int counter = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(annotationClass))
                    counter++;
            }
        }
        return counter;
    }

    public boolean hasVarArgParameter(Class<? extends Annotation> annotationClass) {
        int length = method.getParameterTypes().length;
        if (length == 0) return false;
        Class<?> type = method.getParameterTypes()[length - 1];
        if (type.isArray()) {
            for (Annotation annotation : method.getParameterAnnotations()[length - 1]) {
                if (annotation.annotationType().equals(annotationClass))
                    return true;
            }
        }
        return false;
    }

    public boolean hasVarArgParameter() {
        int length = method.getParameterTypes().length;
        if (length == 0) return false;
        Class<?> type = method.getParameterTypes()[length - 1];
        if (type.isArray())
            return true;
        return false;
    }

    public int numberOfArgs() {
        return method.getParameterTypes().length;
    }

    public Class<?> getFirstParameterType() {
        return method.getParameterTypes()[0];
    }

    public Class<?> getType(int position) {
        return method.getParameterTypes()[position];
    }
}
