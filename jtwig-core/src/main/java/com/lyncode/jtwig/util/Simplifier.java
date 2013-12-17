/**
 * Copyright 2012 Lyncode
 *
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

import com.lyncode.jtwig.tree.value.OperationBinary;

public class Simplifier {
    public static Object simplify(Object object) {
        if (object instanceof OperationBinary) {
            if (isSingleArgumentedOperation((OperationBinary) object))
                return simplify(getOperationArgument((OperationBinary) object));
        }
        return object;
    }

    public static boolean isSingleArgumentedOperation (OperationBinary binary) {
        return binary.getOperators().isEmpty();
    }

    public static Object getOperationArgument (OperationBinary binary) {
        return binary.getOperands().getList().get(0);
    }
}
