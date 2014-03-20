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

package com.lyncode.jtwig.beans;

import org.springframework.context.ApplicationContext;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Vitali Carbivnicii
 * Date: 2014-03-20
 * Time: 19:05
 */
public class BeanResolver implements Map {

    private ApplicationContext context;

    public BeanResolver(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public int size() {
        return context.getBeanDefinitionCount();
    }

    @Override
    public boolean isEmpty() {
        return context.getBeanDefinitionCount() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return context.containsBean((String) key);
    }

    @Override
    public Object get(Object key) {
        return context.getBean((String) key);
    }

    @Override
    public boolean containsValue(Object value) {
        throw new NotImplementedException();
    }

    @Override
    public Object put(Object key, Object value) {
        throw new NotImplementedException();
    }

    @Override
    public Object remove(Object key) {
        throw new NotImplementedException();
    }

    @Override
    public void putAll(Map m) {
        throw new NotImplementedException();
    }

    @Override
    public void clear() {
        throw new NotImplementedException();
    }

    @Override
    public Set keySet() {
        throw new NotImplementedException();
    }

    @Override
    public Collection values() {
        throw new NotImplementedException();
    }

    @Override
    public Set<Entry> entrySet() {
        throw new NotImplementedException();
    }
}
