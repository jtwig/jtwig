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
    public static GivenParameters parameters(Object... parameters) {
        List<GivenParameter> givenParameters = new ArrayList<>();
        for (Object parameter : parameters) {
            givenParameters.add(new GivenParameter(parameter));
        }
        return new GivenParameters(givenParameters);
    }

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

    public Optional<Object> byName(String name) {
        for (GivenParameter givenParameter : givenParameters) {
            if (givenParameter.hasName() && name.equals(givenParameter.getName()))
                return new Optional<>(givenParameter.getValue());
        }
        return new Optional<>();
    }

    public Optional<Object> byPosition(int position) {
        if (position >= givenParameters.size()) return new Optional<>();
        return new Optional<>(givenParameters.get(position).getValue());
    }

    public boolean namesGiven () {
        return (!givenParameters.isEmpty()) && givenParameters.get(0).hasName();
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
