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

package org.jtwig.extension.core.operators;

import org.jtwig.extension.api.operator.BinaryOperator;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.jtwig.types.Undefined;
import static org.jtwig.util.TypeUtil.isLong;
import static org.jtwig.util.TypeUtil.toLong;

public abstract class AbstractBitwiseOperator extends BinaryOperator {
    protected boolean pad = false;
    protected boolean trim = false;

    public AbstractBitwiseOperator(String name, int precedence) {
        super(name, precedence);
    }

    @Override
    public Object render(RenderContext ctx, JtwigPosition pos, Object left, Object right) {
        if (left == null || left instanceof Undefined) {
            left = 0;
        }
        if (right == null || right instanceof Undefined) {
            right = 0;
        }
        
        int width;
        if (trim) {
            width = Math.min(left.toString().length(), right.toString().length());
        } else {
            width = Math.max(left.toString().length(), right.toString().length());
        }
        return calculate(toBitwiseableLong(left, width), toBitwiseableLong(right, width));
    }
    
    public abstract Long calculate(Long left, Long right);
    
    protected Long toBitwiseableLong(Object obj, int width) {
        if (obj instanceof Boolean) {
            return (boolean)obj ? 1L : 0L;
        }
        if ((obj instanceof CharSequence && !isLong(obj))
                || !(obj instanceof Number)) {
            obj = 0L;
        }
        return toLong(obj);
    }
}