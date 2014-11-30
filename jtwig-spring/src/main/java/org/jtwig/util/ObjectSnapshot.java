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

import com.google.common.reflect.AbstractInvocationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ObjectSnapshot {
    private static Logger LOG = LoggerFactory.getLogger(ObjectSnapshot.class);

    public static <T> T snapshot (T object) {
        Class<T> objectClass = (Class<T>) object.getClass();
        return snapshot(object, objectClass);
    }

    public static <T> T snapshot(T object, Class<T> objectClass) {
        return objectClass.cast(Proxy.newProxyInstance(objectClass.getClassLoader(), new Class[]{
                objectClass
        }, new SnapshotInvocationHandler(object)));
    }

    private static class SnapshotInvocationHandler extends AbstractInvocationHandler {
        private final Object object;
        private final Map<String, Object> snapshotMap = new HashMap<>();

        private SnapshotInvocationHandler(Object object) {
            this.object = object;
            for (Method method : object.getClass().getMethods()) {
                if (Void.TYPE != method.getReturnType()) {
                    if (method.getParameterTypes().length == 0) {
                        try {
                            snapshotMap.put(method.getName(), method.invoke(object));
                        } catch (Throwable e) {
                            snapshotMap.put(method.getName(), new StoreException(e));
                        }
                    }
                }
            }
        }

        @Override
        protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
            if (Void.TYPE != method.getReturnType()) {
                if (args.length == 0) {
                    Object result = snapshotMap.get(method.getName());
                    if (result instanceof StoreException)
                        throw ((StoreException) result).e;
                    return result;
                }
                else LOG.debug("Unable to create a snapshot of a method with arguments");
            }
            return null;
        }
    }

    private static class StoreException {
        private final Throwable e;


        private StoreException(Throwable e) {
            this.e = e;
        }
    }
}
