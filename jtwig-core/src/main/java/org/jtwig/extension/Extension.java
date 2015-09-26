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
import java.util.Map;
import org.jtwig.Environment;
import org.jtwig.extension.api.filters.Filter;
import org.jtwig.extension.api.functions.Function;
import org.jtwig.extension.api.operator.Operator;
import org.jtwig.extension.api.test.Test;

public interface Extension {
    /**
     * Returns the name of the extension.
     * @return 
     */
    String getName();
    /**
     * A place for the extension to perform any initialization procedures it
     * requires.
     * @param env The environment under which the extension is registered.
     */
    void init(Environment env);
    /**
     * Returns a list of global variables registered by the extension.
     * @return A map of global variables, indexed by name.
     */
    Map<String, Object> getGlobals();
    /**
     * Returns a map of unary operators registered by the extension, indexed by
     * operator identifier.
     * @return A map of unary operators.
     */
    Map<String, Operator> getUnaryOperators();
    /**
     * Returns a map of binary operators registered by the extension, indexed by
     * operator identifier.
     * @return A map of binary operators.
     */
    Map<String, Operator> getBinaryOperators();
    /**
     * Returns a map of functions registered by the extension, indexed by
     * function name.
     * @return A map of functions.
     */
    Map<String, Function> getFunctions();
    /**
     * Returns a map of filters registered by the extension, indexed by filter
     * name.
     * @return A map of filters.
     */
    Map<String, Filter> getFilters();
    /**
     * Returns a map of tests registered by the extension, indexed by test name.
     * @return A map of tests.
     */
    Map<String, Test> getTests();
    /**
     * Returns a collection of token parser instances registered by the
     * extension.
     * @return A collection of token parser instances.
     */
    Collection<Class<? extends TokenParser>> getTokenParsers();
//    /**
//     * Returns a collection of node visitor instances registered by the
//     * extension.
//     * @return A collection of node visitor instances.
//     */
//    Collection<NodeVisitor> getNodeVisitors();

}