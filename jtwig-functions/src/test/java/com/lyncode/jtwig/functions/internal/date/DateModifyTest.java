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

package com.lyncode.jtwig.functions.internal.date;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNotEquals;

public class DateModifyTest {
    private DateModify underTest = new DateModify();

    @Test
    public void testExecute() throws Exception {
        Date date = new Date();
        assertNotEquals(date, underTest.execute(date, "+1 day"));
    }
}
