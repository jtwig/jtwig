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

package com.lyncode.jtwig.parser.addons;

import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.tree.api.Content;
import com.lyncode.jtwig.tree.api.Tag;
import com.lyncode.jtwig.tree.api.TagInformation;

public abstract class JtwigEmptyContentAddon implements Content, Tag {
    private TagInformation tag = new TagInformation();

    @Override
    public TagInformation begin() {
        return tag;
    }

    @Override
    public TagInformation end() {
        return tag;
    }

    @Override
    public boolean replace(Content expression) throws CompileException {
        return false;
    }
}
