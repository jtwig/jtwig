/**
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

package org.jtwig.acceptance.addons.concurrent;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import static java.util.Arrays.asList;
import java.util.concurrent.ExecutorService;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.jtwig.acceptance.addons.AbstractAddonTest;
import org.jtwig.addons.concurrent.Concurrent;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.exception.RenderException;
import org.jtwig.render.RenderContext;
import org.jtwig.render.stream.RenderStream;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ConcurrentTest extends AbstractAddonTest {
    
    @Test(expected = RenderException.class)
    public void concurrentWithNoOutputStream() throws Exception {
        output = null;
        renderContext = spy(RenderContext.create(env, model, null));
        resource = stringResource("{% concurrent %}a{% endconcurrent %}b");
        render();
    }

    @Test
    public void concurrentWithStaticContent() throws Exception {
        resource = stringResource("{% concurrent %}a{% endconcurrent %}b");
        assertThat(theResult(), is("ab"));
    }

    @Test
    public void concurrentWithConditionalContent() throws Exception {
        resource = stringResource("{% concurrent %}{% if true %}a{% endif %}{% endconcurrent %}b");
        assertThat(theResult(), is("ab"));
    }

    @Test
    public void doubleConcurrentWithStaticContent() throws Exception {
        resource = stringResource("{% concurrent %}a{% endconcurrent %}"
                +"{% concurrent %}b{% endconcurrent %}"
                +"c");
        assertThat(theResult(), is("abc"));
    }

    @Test
    public void concurrentWithDynamicContent() throws Exception {
        model.withModelAttribute("list", asList("a", "b", "c", "d"));
        resource = stringResource("{% concurrent %}{% for item in list %}{{ item }}{% endfor %}{% endconcurrent %}");
        assertThat(theResult(), is("abcd"));
    }

    @Test
    public void test_concurrent_1() throws Exception {
        model.withModelAttribute("list", asList("a", "b", "c", "d", "e"));
        resource = stringResource("{% concurrent %}{% for item in list %}" +
                                                           "{% if loop.first %}{% concurrent %}First {% endconcurrent %}{% elseif loop.last %}{% concurrent %}Last{% endconcurrent %}{% else %}I: {{ loop.index0 }} R: {{ loop.revindex0 }} {% endif %}" +
                                                           "{% endfor %}{% endconcurrent %}");
        assertThat(theResult(), is("First I: 1 R: 3 I: 2 R: 2 I: 3 R: 1 Last"));
    }

    @Test
    public void test_concurrent_2() throws Exception {
        resource = stringResource(
                "{% concurrent %}1{% endconcurrent %}{% concurrent %}{% concurrent %}{% concurrent %}2{% endconcurrent %}{% endconcurrent %}{% endconcurrent %}{% concurrent %}3{% endconcurrent %}{% concurrent %}{% concurrent %}{% concurrent %}{% concurrent %}{% concurrent %}{% concurrent %}4{% endconcurrent %}{% endconcurrent %}{% endconcurrent %}{% endconcurrent %}{% endconcurrent %}{% endconcurrent %}5{% concurrent %}6{% endconcurrent %}{% concurrent %}7{% endconcurrent %}");

        assertThat(theResult(), is("1234567"));
    }
    
    @Test
    public void ensureOutOfMemoryShutdown() throws Exception {
        RenderStream rs = spy(new RenderStream(new ByteArrayOutputStream(), env));
        when(rs.fork()).thenReturn(rs);
        when(renderContext.renderStream()).thenReturn(rs);
        
        // Get the RenderStream's ExecutorService
        Field field = RenderStream.class.getDeclaredField("sExecutor");
        field.setAccessible(true);
        ExecutorService sExecutor = spy((ExecutorService)field.get(null));
        field.set(null, sExecutor);
        doThrow(OutOfMemoryError.class).when(sExecutor).execute(any(Runnable.class));
        
        Concurrent concurrent = new Concurrent();
        concurrent.withContent(new Sequence());
        concurrent.compile(compileContext).render(renderContext);
        verify(sExecutor, atLeastOnce()).shutdownNow();
        
        field.set(null, null);
    }
}
