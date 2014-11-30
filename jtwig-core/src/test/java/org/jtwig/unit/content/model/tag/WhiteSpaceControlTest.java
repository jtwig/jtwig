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

package org.jtwig.unit.content.model.tag;

import org.jtwig.content.model.tag.WhiteSpaceControl;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class WhiteSpaceControlTest {
    WhiteSpaceControl underTest = new WhiteSpaceControl();

    @Test
    public void trimTest() throws Exception {
        underTest.trimAfterBegin(true);
        underTest.trimAfterEnd(false);
        underTest.trimBeforeBegin(false);
        underTest.trimBeforeEnd(true);
        assertThat(underTest.trimAfterBegin(), is(true));
        assertThat(underTest.trimAfterEnd(), is(false));
        assertThat(underTest.trimBeforeBegin(), is(false));
        assertThat(underTest.trimBeforeEnd(), is(true));
    }

    @Test
    public void defaultTest() throws Exception {
        assertThat(underTest.trimAfterBegin(), is(true));
        assertThat(underTest.trimAfterEnd(), is(true));
        assertThat(underTest.trimBeforeBegin(), is(true));
        assertThat(underTest.trimBeforeEnd(), is(true));
    }
}
