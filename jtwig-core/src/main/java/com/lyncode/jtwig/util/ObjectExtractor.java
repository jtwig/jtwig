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

package com.lyncode.jtwig.util;

import com.google.common.base.Predicate;
import com.lyncode.jtwig.content.model.compilable.Import;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.render.RenderContext;
import org.hamcrest.Matcher;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static com.lyncode.jtwig.types.Undefined.UNDEFINED;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.getAllMethods;

public class ObjectExtractor {
    private RenderContext renderContext;
    private Object context;

    public ObjectExtractor(RenderContext renderContext, Object context) {
        this.renderContext = renderContext;
        this.context = context;
    }

    public Object extract (final String name, Object... parameters) throws ExtractException {
        if (context instanceof Import.MacroRepository) {
            try {
                return ((Import.MacroRepository)context).execute(renderContext, name, parameters);
            } catch (RenderException ex) {
                throw new ExtractException(ex);
            }
        }
        
        List<Callable> callables = new ArrayList<Callable>();

        if (parameters.length == 0) {
            callables.add(tryField());
        }

        callables.add(tryMethod());

        if (knownType(context))
            callables.add(tryKnownType());

        for (Callable callable : callables) {
            Result<Object> result = callable.execute(name, parameters);
            if (result.hasResult()) return result.getResult();
        }

        return UNDEFINED;
    }

    private Callable tryKnownType() {
        return new Callable() {
            @Override
            public Result<Object> execute(String name, Object... args) throws ExtractException {
                return new Result<>(extractKnownType(name, args));
            }
        };
    }

    private Object extractKnownType(String name, Object... parameters) {
        if ((context instanceof Map) && parameters.length == 0) {
            return ((Map) context).get(name);
        }
        return null;
    }

    private boolean knownType(Object context) {
        if (context instanceof Map)
            return true;
        else return false;
    }

    private Callable tryField() {
        return new Callable() {
            @Override
            public Result<Object> execute(String name, Object... args) {
                Set<Field> fields = getAllFields(context.getClass(), fieldPredicate(name));
                if (!fields.isEmpty()) {
                    Iterator<Field> iterator = fields.iterator();
                    while (iterator.hasNext()) {
                        try {
                            return new Result<Object>(iterator.next().get(context));
                        } catch (IllegalAccessException e) {
                            // do nothing
                        }
                    }
                    return new Result<Object>();
                } else return new Result<Object>();
            }
        };
    }

    private Predicate<Field> fieldPredicate(final String name) {
        return new Predicate<Field>() {
            @Override
            public boolean apply(@Nullable Field field) {


                if (field == null) return false;
                return equalToIgnoringCase(name).matches(field.getName());
            }
        };
    }

    private Callable tryMethod() {
        return new Callable() {
            @Override
            public Result<Object> execute(final String name, Object... args) throws ExtractException {
                String[] prefixes = new String[]{
                        "get",
                        "is",
                        "has"
                };

                Set<Method> methods = getAllMethods(context.getClass(), methodMatcher(equalToIgnoringCase(name), args.length));
                int i = 0;
                while (methods.isEmpty() && i < prefixes.length) {
                    methods = getAllMethods(context.getClass(), methodMatcher(equalToIgnoringCase(prefixes[i++] + name), args.length));
                }

                if (methods.isEmpty()) return new Result<>();
                else {
                    Iterator<Method> iterator = methods.iterator();
                    Exception thrown = null;
                    while (iterator.hasNext()) {
                        try {
                            return new Result<>(iterator.next().invoke(context, args));
                        } catch (Exception e) {
                            thrown = e;
                        }
                    }

                    if (thrown != null)
                        throw new ExtractException(thrown);
                }

                return new Result<>();
            }
        };
    }

    private Predicate<Method> methodMatcher(final Matcher<? super String> nameMatcher, final int numberOfArguments) {
        return new Predicate<Method>() {
            @Override
            public boolean apply(@Nullable Method method) {
                if (method == null) return false;
                else {
                    return nameMatcher.matches(method.getName()) &&
                            method.getParameterTypes().length == numberOfArguments;
                }
            }
        };
    }

    private static interface Callable {
        Result<Object> execute (String name, Object... args) throws ExtractException;
    }

    private static class Result<T> {
        private T result;
        private boolean has;

        public Result () {
            has = false;
        }

        public Result (T result) {
            has = true;
            this.result = result;
        }

        private T getResult() {
            return result;
        }

        private boolean hasResult() {
            return has;
        }
    }

    public static class ExtractException extends Exception {
        public ExtractException(String message) {
            super(message);
        }
        public ExtractException(Throwable cause) {
            super(cause);
        }
    }
}
