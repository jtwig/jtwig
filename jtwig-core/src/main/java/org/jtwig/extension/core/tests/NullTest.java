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

package org.jtwig.extension.core.tests;

import org.jtwig.extension.api.test.AbstractTest;
import org.jtwig.types.Undefined;

/**
 * http://twig.sensiolabs.org/doc/tests/null.html
 * 
 * Returns true if the variable is null.
 * 
 * In Twig, 'none' is an alias for the null test.
 */
public class NullTest extends AbstractTest {

    @Override
    public boolean evaluate(Object left, Object... args) {
        return left == null || left instanceof Undefined;
    }
    
}