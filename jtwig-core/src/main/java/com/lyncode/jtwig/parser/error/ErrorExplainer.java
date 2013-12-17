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

package com.lyncode.jtwig.parser.error;

import com.lyncode.jtwig.parser.JtwigKeyword;
import org.apache.commons.lang3.StringUtils;
import org.parboiled.errors.InvalidInputError;
import org.parboiled.support.MatcherPath;

import java.util.List;

public class ErrorExplainer {
    public String explain(InvalidInputError error) {
        ErrorInfo known = findKnownError(error.getFailedMatchers());
        if (known == null) return "Unknown Error";
        else {
            switch (known.getKnownError()) {
                case Keyword:
                    return "Expecting one of keywords (" + StringUtils.join(JtwigKeyword.keywords(), ", ") + ")";
                case SpecificKeyword:
                    return specificKeyword(known.getChild().element.matcher.getLabel());
            }
            throw new RuntimeException("Expecting explanation");
        }
    }

    private String specificKeyword(String keyword) {
        return "Expecting "+ keyword + " keyword";
    }

    private ErrorInfo findKnownError(List<MatcherPath> failedMatchers) {
        for (MatcherPath matcherPath : failedMatchers) {
            ErrorInfo knownError = findKnownError(matcherPath, matcherPath);
            if (knownError != null) return knownError;
        }
        return null;
    }

    private ErrorInfo findKnownError(MatcherPath root, MatcherPath matcherPath) {
        try {
            KnownError knownError = KnownError.valueOf(matcherPath.element.matcher.getLabel());
            return new ErrorInfo(knownError, root, matcherPath);
        } catch (IllegalArgumentException e) {
            if (matcherPath.parent != null) {
                return findKnownError(root, matcherPath.parent);
            }
        }
        return null;
    }

    private static class ErrorInfo {
        private KnownError knownError;
        private MatcherPath rootMatcherPath;
        private MatcherPath foundMatcherPath;

        private ErrorInfo(KnownError knownError, MatcherPath rootMatcherPath, MatcherPath foundMatcherPath) {
            this.knownError = knownError;
            this.rootMatcherPath = rootMatcherPath;
            this.foundMatcherPath = foundMatcherPath;
        }

        public KnownError getKnownError() {
            return knownError;
        }

        public MatcherPath getRootMatcherPath() {
            return rootMatcherPath;
        }

        public boolean hasChild () {
            return rootMatcherPath != foundMatcherPath;
        }

        public MatcherPath getChild () {
            if (!hasChild()) return null;
            else {
                MatcherPath current = rootMatcherPath;
                while (getParent(current) != foundMatcherPath)
                    current = getParent(current);
                return current;
            }
        }

        private MatcherPath getParent(MatcherPath current) {
            return current.parent;
        }

        public MatcherPath getFoundMatcherPath() {
            return foundMatcherPath;
        }
    }

    private static enum KnownError {
        Keyword,
        SpecificKeyword
    }
}
