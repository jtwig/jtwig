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

package com.lyncode.jtwig.expressions.operations.binary;

import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.functions.util.ObjectIterator;

public class InOperation extends SimpleBinaryOperation {
    @Override
    protected Object apply(Object left, Object right) throws CalculateException {
        if (right == null) return false;
        if ((right instanceof Iterable) || right.getClass().isArray())
            return new ObjectIterator(right).contains(left);
        else if (right instanceof String)
            return ((String) right).contains(left.toString());
        else return false;
    }

//            case STARTS_WITH:
//                if (leftResolved == null) return false;
//                return leftResolved.toString().startsWith(rightResolved.toString());
//            case ENDS_WITH:
//                if (leftResolved == null) return false;
//                return leftResolved.toString().endsWith(rightResolved.toString());
//            case MATCHES:
//                if (leftResolved == null) return false;
//                return leftResolved.toString().matches(rightResolved.toString());
//            case IN:
//                if (rightResolved == null) return false;
//                if ((rightResolved instanceof Iterable) || rightResolved.getClass().isArray())
//                    return new ObjectIterator(rightResolved).contains(leftResolved);
//                else if (rightResolved instanceof String)
//                    return ((String) rightResolved).contains(leftResolved.toString());
//                else
//                    return false;
}
