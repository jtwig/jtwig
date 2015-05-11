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

package org.jtwig.unit.content.model.compilable;

import org.jtwig.content.model.compilable.Block;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.RenderException;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.unit.AbstractJtwigTest;
import org.junit.Test;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class BlockTest extends AbstractJtwigTest {
    @Test(expected = RenderException.class)
    public void renderCapturesExceptions() throws Exception {
        when(env.compile(anyString())).thenThrow(ParseException.class);
        
        JtwigPosition pos = new JtwigPosition(stringResource(""), 1, 1);
        Block block = new Block(pos, "test");
        block.withContent(new Sequence());
        
        block.compile(compileContext).render(renderContext);
    }
}