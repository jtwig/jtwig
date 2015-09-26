/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.extension;

import org.jtwig.extension.api.tokenparser.TokenParser;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.jtwig.Environment;
import org.jtwig.extension.api.filters.Filter;
import org.jtwig.extension.api.functions.Function;
import org.jtwig.extension.api.operator.Operator;
import org.jtwig.extension.api.test.Test;

public abstract class SimpleExtension implements Extension {

    @Override
    public abstract String getName();

    @Override
    public void init(final Environment env) {}

    @Override
    public Map<String, Object> getGlobals() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public Map<String, Operator> getUnaryOperators() {
        return Collections.EMPTY_MAP;
    }
    @Override
    public Map<String, Operator> getBinaryOperators() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public Map<String, Function> getFunctions() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public Map<String, Filter> getFilters() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public Map<String, Test> getTests() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public Collection<Class<? extends TokenParser>> getTokenParsers() {
        return Collections.EMPTY_LIST;
    }
    
    
    
}