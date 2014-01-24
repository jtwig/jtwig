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

package com.lyncode.jtwig;

import com.lyncode.jtwig.tree.value.FunctionElement;
import com.lyncode.jtwig.tree.value.Selection;
import com.lyncode.jtwig.tree.value.Variable;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JtwigContextTest {
    private JtwigModelMap modelMap = mock(JtwigModelMap.class);
    private JtwigContext resolver = new JtwigContext(modelMap);

    @Test
    public void shouldResolveVariable() throws Exception {
        when(modelMap.get("name")).thenReturn("joao");
        assertThat(resolver.resolve(new Variable("name")), is((Object) "joao"));
    }

    @Test
    public void shouldResolveComposition() throws Exception {
        when(modelMap.get("name")).thenReturn(new ArrayList<Object>());
        Selection selection = new Selection(
                new Variable("name"),
                new Variable("size")
        );
        assertThat(resolver.resolve(selection), is((Object) 0));
    }
    @Test
    public void shouldResolveCompositionWithFunction() throws Exception {
        ArrayList<Object> objects = new ArrayList<Object>();
        objects.add("a");
        when(modelMap.get("name")).thenReturn(objects);
        FunctionElement function = new FunctionElement("get");
        function.add(0);
        Selection selection = new Selection(
                new Variable("name"),
                function
        );
        assertThat(resolver.resolve(selection), is((Object) "a"));
    }
}
