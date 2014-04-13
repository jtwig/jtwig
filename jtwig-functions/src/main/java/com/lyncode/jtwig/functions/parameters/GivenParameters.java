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

import com.lyncode.jtwig.types.Optional;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class GivenParameters {
    private final List<GivenParameter> givenParameters;

    public GivenParameters(List<GivenParameter> givenParameters) {
        this.givenParameters = givenParameters;
    }
    public GivenParameters(GivenParameter... givenParameters) {
        this.givenParameters = asList(givenParameters);
    }
    public GivenParameters () {
        this.givenParameters = new ArrayList<>();
    }

    public Optional<Object> get(int position) {
        if (position >= givenParameters.size()) return new Optional<>();
        return new Optional<>(givenParameters.get(position).getValue());
    }

    public int size () {
        return givenParameters.size();
    }

    public Class<?>[] types () {
        List<Class<?>> types = new ArrayList<>();
        for (GivenParameter givenParameter : givenParameters)
            types.add(givenParameter.type());
        return types.toArray(new Class[types.size()]);
    }

    public GivenParameters add(GivenParameter givenParameter) {
        givenParameters.add(givenParameter);
        return this;
    }

    public GivenParameters addArray(Object[] resolved) {
        for (int i=0;i<resolved.length;i++)
            givenParameters.add(new GivenParameter(resolved[i]));
        return this;
    }

    public GivenParameters addObject(Object resolved) {
        givenParameters.add(new GivenParameter(resolved));
        return this;
    }
}
