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
package org.jtwig.unit.util;

import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.jtwig.util.BigDecimalUtil.*;

public class BigDecimalUtilTest {
    @Test
    public void testIntPower() {
        assertThat(intPower(new BigDecimal("2"), -3, 3),
                is(equalTo(new BigDecimal("0.125"))));
        assertThat(intPower(new BigDecimal("2"), 3, 0),
                is(equalTo(new BigDecimal("8"))));
    }
    
    @Test
    public void testIntRoot() {
        assertThat(intRoot(new BigDecimal("9"), 2, 0),
                is(equalTo(new BigDecimal("3.0"))));
        assertThat(intRoot(new BigDecimal("9"), 3, 5),
                is(equalTo(new BigDecimal("2.080083"))));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRootOfNegative() {
        intRoot(new BigDecimal("-2"), 2, 0);
    }
    
    @Test
    public void testExp() {
        assertThat(exp(new BigDecimal("0"), 0),
                is(equalTo(new BigDecimal("1"))));
        assertThat(exp(new BigDecimal("-2"), 4),
                is(equalTo(new BigDecimal("0.1353"))));
        assertThat(exp(new BigDecimal("2"), 4),
                is(equalTo(new BigDecimal("7.3892"))));
    }
    
    @Test
    public void testLn() {
        assertThat(ln(new BigDecimal("10"), 4),
                is(equalTo(new BigDecimal("2.3026"))));
        assertThat(ln(new BigDecimal("21678"), 4),
                is(equalTo(new BigDecimal("9.9840"))));
    }
    @Test(expected = IllegalArgumentException.class)
    public void testLnWithNegativeValue() {
        ln(new BigDecimal("-2"), 0);
    }
}