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

package org.jtwig.util;

import static org.jtwig.types.Undefined.UNDEFINED;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.jtwig.content.api.ability.ExecutionAware;
import org.jtwig.exception.RenderException;
import org.jtwig.render.RenderContext;

public class ObjectExtractor {
    private RenderContext renderContext;
    private Object context;

    public ObjectExtractor(RenderContext renderContext, Object context) {
        this.renderContext = renderContext;
        this.context = context;
    }

    public Object extract (final String name, Object... parameters) throws ExtractException {
        if (context instanceof ExecutionAware) {
            try {
                return ((ExecutionAware)context).execute(renderContext, name, parameters);
            } catch (RenderException ex) {
                throw new ExtractException(ex);
            }
        }

        List<Callable> callables = new ArrayList<>();

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
	    return context instanceof Map;
    }

    private Callable tryField() {
        return new Callable() {
            @Override
            public Result<Object> execute(String name, Object... args) {
	            Field field = FieldUtils.getField(getClassForContext(context), name, true);

                if (field != null) {
	                try {
		                return new Result<>(field.get(context));
	                }
	                catch(IllegalAccessException e) {
		                return new Result<>();
	                }
                } else return new Result<>();
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

	            Class clazz = getClassForContext(context);

	            Method method = MethodUtils.getMatchingAccessibleMethod(clazz, name, 
			            getParametersClasses(args));

	            if(method == null) {
		            for(String prefix : prefixes) {
			            method = MethodUtils.getMatchingAccessibleMethod(clazz, prefix + 
					            WordUtils.capitalize(name), getParametersClasses(args));
			            
			            if(method != null) 
				            break;
		            }
	            }

	            if(method != null) {
		            try {
			            return new Result<>(method.invoke(context, args));
		            }
		            catch(InvocationTargetException | IllegalAccessException e) {
			            throw new ExtractException(e);
		            }
	            }
				else
	                return new Result<>();
            }
        };
    }
	
	private Class getClassForContext(Object context) {
		return context instanceof Class ? (Class) context  : context.getClass();
	}
	
	private Class[] getParametersClasses(Object... args) {
		if(ArrayUtils.isEmpty(args)) {
			return ArrayUtils.EMPTY_CLASS_ARRAY;
		}
		
		Class[] result = new Class[args.length];
		
		for(int i=0; i<args.length; i++) {
			result[i] = args[i] == null ? null : args[i].getClass();
		}
		
		return result;
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
