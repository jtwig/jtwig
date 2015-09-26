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

package org.jtwig.extension.core.filters;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.jtwig.Environment;
import org.jtwig.extension.api.filters.Filter;
import org.jtwig.extension.api.filters.FilterException;
import org.jtwig.render.RenderContext;
import org.jtwig.types.Undefined;
import org.jtwig.util.ObjectIterator;

public class UrlEncodeFilter implements Filter {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object left, Object... args) throws FilterException {
        try {
            return doEvaluate(left);
        } catch (UnsupportedEncodingException ex) {
            throw new FilterException(ex);
        }
    }
    
    protected Object doEvaluate(Object left) throws UnsupportedEncodingException {
        if (left == null || left == Undefined.UNDEFINED) {
            return null;
        }
        
        if (left instanceof CharSequence || left instanceof Number) {
            return URLEncoder.encode(left.toString(), Charset.defaultCharset().displayName())
                    .replaceAll("\\+", "%20"); // PHP doesn't handle spaces to spec
        }
        if (left instanceof Collection) {
            List<String> pieces = new ArrayList<>();
            int idx = 0;
            for (Object obj : new ObjectIterator(left)) {
                pieces.add(idx+"="+doEvaluate(obj.toString()));
                ++idx;
            }
            return StringUtils.join(pieces, "&");
        }
        if (left instanceof Map) {
            Map source = (Map)left;
            List<String> pieces = new ArrayList<>();
            for (Object key : source.keySet()) {
                pieces.add(doEvaluate(key.toString()) + "=" + doEvaluate(source.get(key).toString()));
            }
            return StringUtils.join(pieces, "&");
        }
        return null;
    }
    
}