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

package org.jtwig.extension.core.functions;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jtwig.Environment;
import org.jtwig.extension.api.functions.Function;
import org.jtwig.render.RenderContext;

public class DumpFunction implements Function {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object... args) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : args) {
            sb.append(ToStringBuilder.reflectionToString(obj, ToStringStyle.MULTI_LINE_STYLE));
        }
        return sb.toString();
    }
    
}
