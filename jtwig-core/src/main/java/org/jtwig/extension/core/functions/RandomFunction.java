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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.jtwig.Environment;
import org.jtwig.extension.api.functions.Function;
import org.jtwig.extension.api.functions.FunctionException;
import org.jtwig.render.RenderContext;
import org.slf4j.LoggerFactory;

/**
 * http://twig.sensiolabs.org/doc/functions/random.html
 * 
 * According to the Twig documentation, random() returns a random value from the
 * given list, or between 0 and the given integer value.
 */
public class RandomFunction implements Function {
    private static final Random RANDOM = new Random();

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object... args) throws FunctionException {
        if (args.length == 0) {
            return RANDOM.nextInt(Integer.MAX_VALUE);
        }
        if (args[0] instanceof Number) {
            return RANDOM.nextInt(((Number)args[0]).intValue());
        }
        if (args[0] instanceof Map) {
            args[0] = ((Map)args[0]).values();
        }
        if (args[0] instanceof Collection) {
            Collection coll = (Collection)args[0];
            if (coll.isEmpty()) {
                throw new FunctionException("The random function cannot pick from an empty array.");
            }
            // We don't need to iterate with lists, so avoid if possible
            if (args[0] instanceof List) {
                return ((List)args[0]).get(RANDOM.nextInt(((List)args[0]).size()));
            }
            
            // Iterate over the collection
            int target = RANDOM.nextInt(coll.size());
            Iterator it = coll.iterator();
            for (int i = 0; i < target; i++) {
                it.next();
            }
            return it.next();
        }
        if (args[0] instanceof CharSequence) {
            CharSequence seq = ((CharSequence)args[0]);
            return seq.charAt(RANDOM.nextInt(seq.length()));
        }
        return null;
    }
    
}