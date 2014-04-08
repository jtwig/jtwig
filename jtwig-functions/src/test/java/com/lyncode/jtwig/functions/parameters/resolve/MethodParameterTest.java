/**
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

package com.lyncode.jtwig.functions.parameters.resolve;

import com.lyncode.jtwig.functions.annotations.Parameter;
import com.lyncode.jtwig.functions.parameters.resolve.model.MethodParameter;
import org.junit.Test;

import java.lang.reflect.Method;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertEquals;

public class MethodParameterTest {

    @Test
    public void parameterWithoutAnnotation() throws Exception {
        Method testMethod = this.getClass().getDeclaredMethod("testMethod", String.class);
        MethodParameter underTest = new MethodParameter(this, testMethod, 0);

        assertThat(underTest.hasAnnotation(), equalTo(false));
        assertEquals(underTest.type(), String.class);
        assertTrue(underTest.hasType(String.class));
    }

    @Test
    public void parameterWithAnnotation() throws Exception {
        Method testMethod = this.getClass().getDeclaredMethod("testMethodWithAnnotation", String.class);
        MethodParameter underTest = new MethodParameter(this, testMethod, 0);

        assertThat(underTest.hasAnnotation(), equalTo(true));
        assertThat(underTest.hasAnnotation(Parameter.class), equalTo(true));
        assertThat(underTest.annotations(), hasItem(Parameter.class));
    }

    String testMethod (String one) {
        return "";
    }

    String testMethodWithAnnotation (@Parameter String one) {
        return "";
    }
}
