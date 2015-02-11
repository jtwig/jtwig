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

package org.jtwig.unit.compile;

import org.jtwig.Environment;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.model.compilable.Sequence;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompileContextTest {
    @Test
    public void ensureParentalMethodsWorkProperly() {
        CompileContext ctx = new CompileContext(null, new Environment());
        
        Sequence seq = new Sequence();
        assertFalse(ctx.hasParent());
        assertNull(ctx.parent());
        ctx.withParent(seq);
        assertTrue(ctx.hasParent());
        assertEquals(seq, ctx.parent());
        
        // Quick check
        assertNotNull(ctx.cache());
    }
}