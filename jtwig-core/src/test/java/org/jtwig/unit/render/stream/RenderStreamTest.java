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

package org.jtwig.unit.render.stream;

import static org.hamcrest.Matchers.isEmptyString;
import org.jtwig.content.api.Renderable;
import org.jtwig.render.stream.RenderStream;
import org.jtwig.unit.AbstractJtwigTest;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class RenderStreamTest extends AbstractJtwigTest {
    @Test
    public void testRenderConcurrentOutOfMemory() throws Exception {
        Renderable renderable = mock(Renderable.class);
        doThrow(OutOfMemoryError.class).when(renderable).render(renderContext);
        
        RenderStream stream = new RenderStream(output, env);
        stream.renderConcurrent(renderable, renderContext);
        assertThat(output.toString(),
                isEmptyString());
    }
}