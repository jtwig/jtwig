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

package com.lyncode.jtwig.functions.builtin;

import com.lyncode.jtwig.functions.annotations.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.Parameter;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.functions.util.HtmlUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.Charset.forName;
import static java.util.Arrays.asList;

public class StringFunctions {
    @JtwigFunction(name = "capitalize")
    public String capitalize (@Parameter String input) {
        if (input.length() > 0)
            return input.substring(0, 1).toUpperCase() + input.substring(1);
        else
            return input;
    }

    @JtwigFunction(name = "convert_encoding")
    public String convertEncoding (@Parameter String input, @Parameter String from, @Parameter String to) {
        return new String(input.getBytes(forName(from)), forName(to));
    }

    @JtwigFunction(name = "escape", aliases = {"e"})
    public String escape (@Parameter String input) throws FunctionException {
        return escape(input, EscapeStrategy.HTML.name());
    }

    @JtwigFunction(name = "escape", aliases = {"e"})
    public String escape (@Parameter String input, @Parameter String strategy) throws FunctionException {
        switch (EscapeStrategy.strategyByName(strategy.toLowerCase())) {
            case JAVASCRIPT:
                return StringEscapeUtils.escapeEcmaScript(input);
            case XML:
                return StringEscapeUtils.escapeXml(input);
            case HTML: // Default html
            default:
                return StringEscapeUtils.escapeHtml4(input);
        }
    }


    @JtwigFunction(name = "format")
    public String format (@Parameter String input, @Parameter Object... arguments) {
        return String.format(input, arguments);
    }

    @JtwigFunction(name = "lower")
    public String lower (@Parameter String input) {
        return input.toLowerCase();
    }

    @JtwigFunction(name = "nl2br")
    public String nl2br (@Parameter String input) {
        return input.replace("\n", "<br />");
    }

    @JtwigFunction(name = "replace")
    public String replace (@Parameter String input, @Parameter Map<String, Object> replacements) {
        for (String key : replacements.keySet()) {
            if (replacements.containsKey(key)) {
                input = input.replace(key, replacements.get(key).toString());
            }
        }
        return input;
    }

    private boolean test(Map<String, Object> replacements, String key) {
        return replacements.containsKey(key);
    }

    @JtwigFunction(name = "split")
    public List<String> split (@Parameter String input, @Parameter String separator) {
        return asList(input.split(separator));
    }

    @JtwigFunction(name = "striptags")
    public String stripTags (@Parameter String input) {
        return stripTags(input, "");
    }

    @JtwigFunction(name = "striptags")
    public String stripTags (@Parameter String input, @Parameter String allowedTags) {
        return HtmlUtils.stripTags(input, allowedTags);
    }


    @JtwigFunction(name = "title")
    public String title (@Parameter String input) {
        return WordUtils.capitalize(input);
    }

    @JtwigFunction(name = "trim")
    public String trim (@Parameter String input) {
        return (input == null) ? null : input.trim();
    }

    @JtwigFunction(name = "upper")
    public String upper (@Parameter String input) {
        return input.toUpperCase();
    }

    @JtwigFunction(name = "url_encode")
    public String urlEncode (@Parameter String input) throws UnsupportedEncodingException {
        return URLEncoder.encode(input, Charset.defaultCharset().displayName());
    }
    @JtwigFunction(name = "url_encode")
    public String urlEncode (@Parameter Map input) throws UnsupportedEncodingException {
        List<String> pieces = new ArrayList<String>();
        for (Object key : input.keySet()) {
            pieces.add(urlEncode(key.toString()) + "=" + urlEncode(input.get(key).toString()));
        }
        return StringUtils.join(pieces, "&");
    }

    @JtwigFunction(name = "first")
    public Character first (@Parameter String input) {
        if (input.isEmpty()) return null;
        return input.charAt(0);
    }
    @JtwigFunction(name = "last")
    public Character last (@Parameter String input) {
        if (input.isEmpty()) return null;
        return input.charAt(input.length() - 1);
    }
    @JtwigFunction(name = "reverse")
    public String reverse (@Parameter String input) {
        return new StringBuilder(input).reverse().toString();
    }


    enum EscapeStrategy {
        HTML("html"),
        JAVASCRIPT("js", "javascript"),
        XML("xml");

        private List<String> representations;

        EscapeStrategy(String... representations) {
            this.representations = asList(representations);
        }

        public static EscapeStrategy strategyByName(String name) {
            for (EscapeStrategy escape : EscapeStrategy.values()) {
                if (escape.representations.contains(name))
                    return escape;
            }
            throw new IllegalStateException(String.format("Unknown strategy '%s'", name));
        }
    }

}
