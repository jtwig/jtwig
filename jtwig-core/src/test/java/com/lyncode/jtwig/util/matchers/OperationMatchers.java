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

package com.lyncode.jtwig.util.matchers;

public class OperationMatchers {
//
//    public static Matcher<OperationBinary> anOperation(OperationBinaryMatcherBuilder builder) {
//        return builder.build();
//    }
//
//    public static Matcher<OperationUnary> anOperation(OperationUnaryMatcherBuilder builder) {
//        return builder.build();
//    }


//
//    public static class OperationBinaryMatcherBuilder {
//        private List<Matcher<OperationBinary>> matchers = new ArrayList<Matcher<OperationBinary>>();
//
//        public OperationBinaryMatcherBuilder whichOperand(final int index, final Matcher<? extends Object> matcher) {
//            matchers.add(new TypeSafeMatcher<OperationBinary>() {
//                @Override
//                protected boolean matchesSafely(OperationBinary item) {
//                    if (item..getOperands().operandsCount() > index) {
//                        return matcher.matches(item.getOperands().retrieve(index));
//                    }
//                    return false;
//                }
//
//                @Override
//                public void describeTo(Description description) {
//                    description.appendText("Operand "+index+" ").appendDescriptionOf(matcher);
//                }
//            });
//            return this;
//        }
//
//        public OperationBinaryMatcherBuilder whichOperator(final int index, final Operator operator) {
//            matchers.add(new TypeSafeMatcher<OperationBinary>() {
//                @Override
//                protected boolean matchesSafely(OperationBinary item) {
//                    if (item.getOperators().operandsCount() > index)
//                        return item.getOperators().retrieve(index) == operator;
//                    return false;
//                }
//
//                @Override
//                public void describeTo(Description description) {
//                    description.appendText("Operator "+index+" is ").appendValue(operator);
//                }
//            });
//            return this;
//        }
//
//        public Matcher<OperationBinary> build () {
//            Matcher<? super OperationBinary>[] matcherArray = new Matcher[matchers.operandsCount()];
//            return AllOf.allOf(this.matchers.toArray(matcherArray));
//        }
//
//
//        public OperationBinaryMatcherBuilder and() {
//            return this;
//        }
//    }
//
//    public static class OperationUnaryMatcherBuilder {
//        private List<Matcher<OperationUnary>> matchers = new ArrayList<Matcher<OperationUnary>>();
//
//        public OperationUnaryMatcherBuilder withOperand (final Matcher<? extends Object> matcher) {
//            matchers.add(new TypeSafeMatcher<OperationUnary>() {
//                @Override
//                protected boolean matchesSafely(OperationUnary item) {
//                    return matcher.matches(getOperand(item.getOperand()));
//                }
//
//                @Override
//                public void describeTo(Description description) {
//                    description.appendText("Operand ");
//                    matcher.describeTo(description);
//                }
//            });
//            return this;
//        }
//
//        private Object getOperand (Object obj) {
//            if (obj instanceof OperationUnary)
//                return getOperand(((OperationUnary) obj).getOperand());
//            if (obj instanceof OperationBinary)
//                return getOperand(getItem((OperationBinary) obj, 0));
//
//            return obj;
//        }
//
//        private Object getItem(OperationBinary item, int index) {
//            return getOperand(item.getOperands().retrieve(index));
//        }
//
//        public OperationUnaryMatcherBuilder withOperator(final Operator operator) {
//            matchers.add(new TypeSafeMatcher<OperationUnary>() {
//                @Override
//                protected boolean matchesSafely(OperationUnary item) {
//                    return item.getOperator() == operator;
//                }
//
//                @Override
//                public void describeTo(Description description) {
//                    description.appendText("Operator is ").appendValue(operator);
//                }
//            });
//            return this;
//        }
//
//        public Matcher<OperationUnary> build () {
//            Matcher<? super OperationUnary>[] matcherArray = new Matcher[matchers.operandsCount()];
//            return AllOf.allOf(this.matchers.toArray(matcherArray));
//        }
//
//        public OperationUnaryMatcherBuilder and() {
//            return this;
//        }
//    }

}
