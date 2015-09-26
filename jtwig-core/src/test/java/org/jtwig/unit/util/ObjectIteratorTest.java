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

package org.jtwig.unit.util;

import java.util.ArrayList;
import java.util.HashMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import org.jtwig.util.ObjectIterator;
import org.junit.Test;

public class ObjectIteratorTest {
    @Test
    public void arrays() throws Exception {
        ObjectIterator iterator = new ObjectIterator(new Object[]{1,2,3});
        assertThat(iterator.size(), equalTo(3));
    }
    @Test
    public void lists() throws Exception {
        ObjectIterator iterator = new ObjectIterator(new ArrayList<Integer>(){{
            add(1);
            add(2);
            add(3);
        }});
        assertThat(iterator.size(), equalTo(3));
    }
    @Test
    public void maps() throws Exception {
        ObjectIterator iterator = new ObjectIterator(new HashMap<Integer, Object>(){{
            put(1, null);
            put(2, null);
            put(3, null);
        }});
        assertThat(iterator.size(), equalTo(3));
    }
}
