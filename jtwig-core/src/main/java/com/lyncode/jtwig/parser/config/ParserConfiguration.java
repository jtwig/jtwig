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

public class ParserConfiguration {
    public static ParserConfiguration configuration () {
        return new ParserConfiguration();
    }

    private String beginCode = "{%";
    private String endCode = "%}";
    private String beginOutput = "{{";
    private String endOutput = "}}";
    private String beginComment = "{#";
    private String endComment = "#}";
    private boolean useStrictEvaluation = true;

    public ParserConfiguration() {}

    public String getBeginCode() {
        return beginCode;
    }

    public ParserConfiguration withBeginCode(String startCode) {
        this.beginCode = startCode;
        return this;
    }

    public String getEndCode() {
        return endCode;
    }

    public ParserConfiguration withEndCode(String endCode) {
        this.endCode = endCode;
        return this;
    }

    public String getBeginOutput() {
        return beginOutput;
    }

    public ParserConfiguration withBeginOutput(String startOutput) {
        this.beginOutput = startOutput;
        return this;
    }

    public String getEndOutput() {
        return endOutput;
    }

    public ParserConfiguration withEndOutput(String endOutput) {
        this.endOutput = endOutput;
        return this;
    }

    public String getBeginComment() {
        return beginComment;
    }

    public ParserConfiguration withBeginComment(String startComment) {
        this.beginComment = startComment;
        return this;
    }

    public String getEndComment() {
        return endComment;
    }

    public ParserConfiguration withEndComment(String endComment) {
        this.endComment = endComment;
        return this;
    }
    
    public boolean isUsingStrictEvaluation() {
        return useStrictEvaluation;
    }
    
    public ParserConfiguration useStrictEvaluation(boolean useStrictEvaluation) {
        this.useStrictEvaluation = useStrictEvaluation;
        return this;
    }
}
