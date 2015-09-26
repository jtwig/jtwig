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

import org.apache.commons.lang3.math.NumberUtils;
import org.jtwig.extension.api.test.AbstractTest;
import org.jtwig.extension.api.test.TestException;

/**
 * http://twig.sensiolabs.org/doc/tests/sameas.html
 * 
 * According to the Twig documentation, the sameas test is equivalent to strict
 * equality (===) evaluation in PHP. This does not check for identity, but
 * rather ensures that both the type as well as the value of both operands are
 * identical.
 * 
 * Arrays, lists, and maps are the same in PHP, but not in Java. As a result, an
 * equivalence comparison between {...} and [...] in Twig may yield true
 * provided the contents are the same.
 * 
 * NOTE: We currently do not implement array/list/map comparison.
 */
public class SameAsTest extends AbstractTest {

    @Override
    public boolean evaluate(Object left, Object... args) throws TestException {
        Object right = args.length > 0 ? args[0] : null;
        if (left == null ^ right == null) {
            return false;
        }
        
        // Numbers, regardless of type, are considered equal based on value
        // alone. Here we widen and then check equivalence.
        if (left instanceof Number && right instanceof Number) {
            return Double.valueOf(((Number)left).doubleValue()).equals(((Number)right).doubleValue());
        }
        
        if (left instanceof Boolean && right instanceof Boolean) {
            return ((Boolean)left).equals((Boolean)right);
        }
        
        if (left instanceof CharSequence && right instanceof CharSequence) {
            return left.equals(right);
        }
        return false;
    }
    
}