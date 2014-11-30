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

package org.jtwig.functions.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static java.util.regex.Pattern.*;

public class HtmlUtils {

    private static final String START_COMMENT = "<!--";
    private static final String END_COMMENT = "-->";

    public static String stripTags (String input, String allowedTags) {
        return removeUnknownTags(removeHtmlComments(input), allowedTags);
    }
    public static String stripTags (String input) {
        return removeUnknownTags(removeHtmlComments(input), "");
    }

    private static String removeUnknownTags(String input, String knownTags) {
        List<String> knownTagList = asList(knownTags.replaceAll("^<", "").replaceAll(">$", "").split("><"));
        return removeTags(input, knownTagList);
    }

    private static String removeTags(String input, List<String> knownTagList) {
        Pattern tag = compile("</?([^\\s>]*)\\s*[^>]*>", CASE_INSENSITIVE);
        Matcher matches = tag.matcher(input);
        while (matches.find()) {
            if (!knownTagList.contains(matches.group(1))) {
                input = input.replaceAll(quote(matches.group()), "");
            }
        }
        return input;
    }

    private static String removeTags (String input, String startTag, String endTag) {
        while (input.contains(startTag)) {
            int start = input.indexOf(startTag);
            int end = input.substring(start + startTag.length()).indexOf(endTag);

            if (end == -1) input = input.substring(0, start);
            else input = input.substring(0, start) + input.substring(start + startTag.length() + end + endTag.length());
        }

        return input;
    }

    private static String removeHtmlComments (String input) {
        return removeTags(input, START_COMMENT, END_COMMENT);
    }
}
