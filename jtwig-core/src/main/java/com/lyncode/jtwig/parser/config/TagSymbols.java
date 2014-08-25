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

package com.lyncode.jtwig.parser.config;

public enum TagSymbols {
    DEFAULT("{%", "%}", "{{", "}}", "{#", "#}"),
    JAVASCRIPT_COLLISION_FREE("<#","#>","@>","<@", "<$","$>");

    private final String beginOutput;
    private final String endOutput;

    private final String beginTag;
    private final String endTag;

    private final String beginComment;
    private final String endComment;

    TagSymbols(String beginTag, String endTag, String beginOutput, String endOutput, String beginComment, String endComment) {
        this.beginOutput = beginOutput;
        this.endOutput = endOutput;
        this.beginTag = beginTag;
        this.endTag = endTag;
        this.beginComment = beginComment;
        this.endComment = endComment;
    }

    public String beginOutput() {
        return beginOutput;
    }

    public String endOutput() {
        return endOutput;
    }

    public String beginTag() {
        return beginTag;
    }

    public String endTag() {
        return endTag;
    }

    public String beginComment() {
        return beginComment;
    }

    public String endComment() {
        return endComment;
    }
}
