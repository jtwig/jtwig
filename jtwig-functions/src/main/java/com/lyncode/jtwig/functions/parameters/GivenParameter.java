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

package com.lyncode.jtwig.functions.parameters;

public class GivenParameter {
    private final String name;
    private final Object value;

    public GivenParameter(Object value) {
        this.value = value;
        this.name = null;
    }

    public GivenParameter(Object value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public boolean hasName() {
        return name != null;
    }

    public Class<?> type() {
        if (value == null) return null;
        else return value.getClass();
    }
}
