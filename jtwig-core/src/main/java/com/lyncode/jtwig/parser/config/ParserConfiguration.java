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

    private Symbols symbols = TagSymbols.DEFAULT;
    private AddonParserList parserList = new AddonParserList();

    public ParserConfiguration withSymbols (Symbols symbols) {
        this.symbols = symbols;
        return this;
    }

    public Symbols symbols() {
        return symbols;
    }

    public AddonParserList addons() {
        return parserList;
    }
}
