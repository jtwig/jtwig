/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.extension.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTimeZone;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.extension.SimpleExtension;
import org.jtwig.extension.api.filters.Filter;
import org.jtwig.extension.api.functions.Function;
import org.jtwig.extension.api.operator.Operator;
import org.jtwig.extension.api.test.Test;
import org.jtwig.extension.api.tokenparser.TokenParser;
import org.jtwig.extension.core.filters.AbsFilter;
import org.jtwig.extension.core.filters.BatchFilter;
import org.jtwig.extension.core.filters.CapitalizeFilter;
import org.jtwig.extension.core.filters.ConvertEncodingFilter;
import org.jtwig.extension.core.filters.DateFilter;
import org.jtwig.extension.core.filters.DateModifyFilter;
import org.jtwig.extension.core.filters.DefaultFilter;
import org.jtwig.extension.core.filters.EscapeFilter;
import org.jtwig.extension.core.filters.FirstFilter;
import org.jtwig.extension.core.filters.FormatFilter;
import org.jtwig.extension.core.filters.JoinFilter;
import org.jtwig.extension.core.filters.JsonEncodeFilter;
import org.jtwig.extension.core.filters.KeysFilter;
import org.jtwig.extension.core.filters.LastFilter;
import org.jtwig.extension.core.filters.LengthFilter;
import org.jtwig.extension.core.filters.LowerFilter;
import org.jtwig.extension.core.filters.MergeFilter;
import org.jtwig.extension.core.filters.Newline2BreakFilter;
import org.jtwig.extension.core.filters.NumberFormatFilter;
import org.jtwig.extension.core.filters.ReplaceFilter;
import org.jtwig.extension.core.filters.ReverseFilter;
import org.jtwig.extension.core.filters.RoundFilter;
import org.jtwig.extension.core.filters.SliceFilter;
import org.jtwig.extension.core.filters.SortFilter;
import org.jtwig.extension.core.filters.SplitFilter;
import org.jtwig.extension.core.filters.StripTagsFilter;
import org.jtwig.extension.core.filters.TitleFilter;
import org.jtwig.extension.core.filters.TrimFilter;
import org.jtwig.extension.core.filters.UpperFilter;
import org.jtwig.extension.core.filters.UrlEncodeFilter;
import org.jtwig.extension.core.functions.AttributeFunction;
import org.jtwig.extension.core.functions.BlockFunction;
import org.jtwig.extension.core.functions.ConstantFunction;
import org.jtwig.extension.core.functions.CycleFunction;
import org.jtwig.extension.core.functions.DateFunction;
import org.jtwig.extension.core.functions.DumpFunction;
import org.jtwig.extension.core.functions.IncludeFunction;
import org.jtwig.extension.core.functions.MaxFunction;
import org.jtwig.extension.core.functions.MinFunction;
import org.jtwig.extension.core.functions.ParentFunction;
import org.jtwig.extension.core.functions.RandomFunction;
import org.jtwig.extension.core.functions.RangeFunction;
import org.jtwig.extension.core.functions.SourceFunction;
import org.jtwig.extension.core.functions.TemplateFromStringFunction;
import org.jtwig.extension.core.operators.BinaryAdditionOperator;
import org.jtwig.extension.core.operators.BinaryAndOperator;
import org.jtwig.extension.core.operators.BinaryBitwiseAndOperator;
import org.jtwig.extension.core.operators.BinaryBitwiseOrOperator;
import org.jtwig.extension.core.operators.BinaryBitwiseXorOperator;
import org.jtwig.extension.core.operators.BinaryCompositionOperator;
import org.jtwig.extension.core.operators.BinaryConcatenationOperator;
import org.jtwig.extension.core.operators.BinaryDivisionOperator;
import org.jtwig.extension.core.operators.BinaryEndsWithOperator;
import org.jtwig.extension.core.operators.BinaryEqualOperator;
import org.jtwig.extension.core.operators.BinaryExponentOperator;
import org.jtwig.extension.core.operators.BinaryFloorDivisionOperator;
import org.jtwig.extension.core.operators.BinaryGreaterThanOperator;
import org.jtwig.extension.core.operators.BinaryGreaterThanOrEqualOperator;
import org.jtwig.extension.core.operators.BinaryInOperator;
import org.jtwig.extension.core.operators.BinaryIsNotOperator;
import org.jtwig.extension.core.operators.BinaryIsOperator;
import org.jtwig.extension.core.operators.BinaryLessThanOperator;
import org.jtwig.extension.core.operators.BinaryLessThanOrEqualOperator;
import org.jtwig.extension.core.operators.BinaryMatchesOperator;
import org.jtwig.extension.core.operators.BinaryModulusOperator;
import org.jtwig.extension.core.operators.BinaryMultiplicationOperator;
import org.jtwig.extension.core.operators.BinaryNotEqualOperator;
import org.jtwig.extension.core.operators.BinaryNotInOperator;
import org.jtwig.extension.core.operators.BinaryOrOperator;
import org.jtwig.extension.core.operators.BinaryRangeOperator;
import org.jtwig.extension.core.operators.BinarySelectionOperator;
import org.jtwig.extension.core.operators.BinaryStartsWithOperator;
import org.jtwig.extension.core.operators.BinarySubtractionOperator;
import org.jtwig.extension.core.operators.UnaryIsNotOperator;
import org.jtwig.extension.core.operators.UnaryIsOperator;
import org.jtwig.extension.core.operators.UnaryNegativeOperator;
import org.jtwig.extension.core.operators.UnaryNotOperator;
import org.jtwig.extension.core.operators.UnaryPositiveOperator;
import org.jtwig.extension.core.tests.ConstantTest;
import org.jtwig.extension.core.tests.DefinedTest;
import org.jtwig.extension.core.tests.DivisibleByTest;
import org.jtwig.extension.core.tests.EmptyTest;
import org.jtwig.extension.core.tests.EvenTest;
import org.jtwig.extension.core.tests.IterableTest;
import org.jtwig.extension.core.tests.NullTest;
import org.jtwig.extension.core.tests.OddTest;
import org.jtwig.extension.core.tests.SameAsTest;
import org.jtwig.extension.core.tokenparsers.BlockDefinitionParser;
import org.jtwig.extension.core.tokenparsers.CommentParser;
import org.jtwig.extension.core.tokenparsers.ConcurrentTag;
import org.jtwig.extension.core.tokenparsers.EmbedStatementParser;
import org.jtwig.extension.core.tokenparsers.ExtendsStatementParser;
import org.jtwig.extension.core.tokenparsers.FilterTag;
import org.jtwig.extension.core.tokenparsers.ForStatementParser;
import org.jtwig.extension.core.tokenparsers.FromImportStatementParser;
import org.jtwig.extension.core.tokenparsers.IfStatementParser;
import org.jtwig.extension.core.tokenparsers.ImportStatementParser;
import org.jtwig.extension.core.tokenparsers.IncludeStatementParser;
import org.jtwig.extension.core.tokenparsers.MacroDefinitionParser;
import org.jtwig.extension.core.tokenparsers.SetStatementParser;
import org.jtwig.extension.core.tokenparsers.SpacelessTag;
import org.jtwig.extension.core.tokenparsers.VerbatimTag;

public class CoreJtwigExtension extends SimpleExtension {
    private final JtwigConfiguration config;
    private String dateFormat = "d/m/Y h:i:sa";
    private DateTimeZone timeZone = DateTimeZone.UTC;
    
    public CoreJtwigExtension(final JtwigConfiguration config) {
        this.config = config;
    }

    @Override
    public String getName() {
        return "core";
    }

    @Override
    public Map<String, Operator> getUnaryOperators() {
        return new HashMap<String, Operator>(){{
            put("is not", new UnaryIsNotOperator("is not", 105));
            put("is", new UnaryIsOperator("is", 100));
            put("not", new UnaryNotOperator("not", 19));
            put("-", new UnaryNegativeOperator("-", 3));
            put("+", new UnaryPositiveOperator("+", 3));
        }};
    }
    
    @Override
    public Map<String, Operator> getBinaryOperators() {
        return new HashMap<String, Operator>(){{
            // These aren't operators in Twig, but they function like it
            put("is", new BinaryIsOperator("is", 4));
            put("is not", new BinaryIsNotOperator("is not", 4));
            put("|", new BinaryCompositionOperator("|", 5));
            
            // Twig operators
            put("or", new BinaryOrOperator("or", 10));
            put("and", new BinaryAndOperator("and", 15));
            put("b-or", new BinaryBitwiseOrOperator("b-or", 16));
            put("b-xor", new BinaryBitwiseXorOperator("b-xor", 17));
            put("b-and", new BinaryBitwiseAndOperator("b-and", 18));
            
            put("==", new BinaryEqualOperator("==", 20));
            put("!=", new BinaryNotEqualOperator("!=", 20));
            put("<=", new BinaryLessThanOrEqualOperator("<=", 21));
            put(">=", new BinaryGreaterThanOrEqualOperator(">=", 21));
            put("<", new BinaryLessThanOperator("<", 20));
            put(">", new BinaryGreaterThanOperator(">", 20));
            put("not in", new BinaryNotInOperator("not in", 20));
            put("in", new BinaryInOperator("in", 20));
            put("matches", new BinaryMatchesOperator("matches", 20));
            put("starts with", new BinaryStartsWithOperator("starts with", 20));
            put("ends with", new BinaryEndsWithOperator("ends with", 20));
            put("..", new BinaryRangeOperator("..", 25));
            
            put("+", new BinaryAdditionOperator("+", 30));
            put("-", new BinarySubtractionOperator("-", 30));
            
            put("~", new BinaryConcatenationOperator("~", 40));
            put("*", new BinaryMultiplicationOperator("*", 60));
            put("/", new BinaryDivisionOperator("/", 60));
            put("//", new BinaryFloorDivisionOperator("//", 60));
            put("%", new BinaryModulusOperator("%", 60));
            put("**", new BinaryExponentOperator("**", 200));
            put(".", new BinarySelectionOperator(".", 500));
        }};
    }

    @Override
    public Map<String, Function> getFunctions() {
        return new HashMap<String, Function>(){{
            put("attribute", new AttributeFunction());
            put("block", new BlockFunction());
            put("constant", new ConstantFunction());
            put("cycle", new CycleFunction());
            put("date", new DateFunction());
            put("dump", new DumpFunction());
            put("include", new IncludeFunction());
            put("max", new MaxFunction());
            put("min", new MinFunction());
            put("parent", new ParentFunction());
            put("random", new RandomFunction());
            put("range", new RangeFunction());
            put("source", new SourceFunction());
            put("template_from_string", new TemplateFromStringFunction());
        }};
    }

    @Override
    public Map<String, Filter> getFilters() {
        return new HashMap<String, Filter>(){{
            // Formatting
            put("date", new DateFilter());
            put("date_modify", new DateModifyFilter());
            put("format", new FormatFilter());
            put("replace", new ReplaceFilter());
            put("number_format", new NumberFormatFilter());
            put("abs", new AbsFilter());
            put("round", new RoundFilter());

            // Encoding
            put("url_encode", new UrlEncodeFilter());
            put("json_encode", new JsonEncodeFilter(config.getJsonConfiguration()));
            put("convert_encoding", new ConvertEncodingFilter());

            // String filters
            put("title", new TitleFilter());
            put("capitalize", new CapitalizeFilter());
            put("upper", new UpperFilter());
            put("lower", new LowerFilter());
            put("striptags", new StripTagsFilter());
            put("trim", new TrimFilter());
            put("nl2br", new Newline2BreakFilter());

            // Array helpers
            put("join", new JoinFilter());
            put("split", new SplitFilter());
            put("sort", new SortFilter());
            put("merge", new MergeFilter());
            put("batch", new BatchFilter());

            // String/array filters
            put("reverse", new ReverseFilter());
            put("length", new LengthFilter());
            put("slice", new SliceFilter());
            put("first", new FirstFilter());
            put("last", new LastFilter());

            // Iteration and runtime
            put("default", new DefaultFilter());
            put("keys", new KeysFilter());

            // Escaping
            put("escape", new EscapeFilter());
            put("e", new EscapeFilter());
        }};
    }
    
    @Override
    public Map<String, Test> getTests() {
        return new HashMap<String, Test>(){{
            put("even", new EvenTest());
            put("odd", new OddTest());
            put("defined", new DefinedTest());
            put("sameas", new SameAsTest());
            put("same as", new SameAsTest());
            put("null", new NullTest());
            put("divisibleby", new DivisibleByTest());
            put("divisible by", new DivisibleByTest());
            put("constant", new ConstantTest());
            put("empty", new EmptyTest());
            put("iterable", new IterableTest());
        }};
    }

    @Override
    public Collection<Class<? extends TokenParser>> getTokenParsers() {
        return Arrays.asList(
                ExtendsStatementParser.class,
                IncludeStatementParser.class,
                
                BlockDefinitionParser.class,
                MacroDefinitionParser.class,
                ImportStatementParser.class,
                FromImportStatementParser.class,
                EmbedStatementParser.class,
                CommentParser.class,
                
                ForStatementParser.class,
//                new DoTokenParser(),
                IfStatementParser.class,
                
//                UseTokenParser.class,
                SetStatementParser.class,
//                FlushTokenParser.class,
                
                ConcurrentTag.class,
                SpacelessTag.class,
                FilterTag.class,
                VerbatimTag.class
        );
    }
    
    public String getDateFormat() {
        return dateFormat;
    }
    public CoreJtwigExtension setDateFormat(final String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }
    public DateTimeZone getTimeZone() {
        return timeZone;
    }
    public CoreJtwigExtension setTimeZone(final DateTimeZone timeZone) {
        this.timeZone = timeZone;
        return this;
    }
    
}