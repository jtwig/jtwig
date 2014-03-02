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

package com.lyncode.jtwig.tree.api;

import java.util.ArrayList;
import java.util.List;

public class TagInformation {
    private List<TagProperty> leftProperties = new ArrayList<>();
    private List<TagProperty> rightProperties = new ArrayList<>();

    public TagInformation addToLeft (TagProperty property) {
        leftProperties.add(property);
        return this;
    }

    public TagInformation addToRight (TagProperty property) {
        rightProperties.add(property);
        return this;
    }

    public boolean hasLeft (TagProperty property) {
        return leftProperties.contains(property);
    }

    public boolean hasRight (TagProperty property) {
        return rightProperties.contains(property);
    }
}
