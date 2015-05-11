package org.jtwig.util.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

public class ExceptionMatcherBuilder extends BaseMatcher<Throwable> {
    public static ExceptionMatcherBuilder exception () {
        return new ExceptionMatcherBuilder();
    }

    private ExceptionMatcherBuilder() {
    }

    private Collection<Matcher<? super Throwable>> matchers = new ArrayList<>();

    public ExceptionMatcherBuilder withCause (Matcher<? super Throwable> exceptionMatcher) {
        matchers.add(new FeatureMatcher<Throwable, Iterable<Throwable>>(hasItem(exceptionMatcher), "causes", "causes"){
            @Override
            protected Iterable<Throwable> featureValueOf(Throwable actual) {
                return causesOf(actual);
            }
        });
        return this;
    }

    public ExceptionMatcherBuilder ofType (Class<?> type) {
        matchers.add(instanceOf(type));
        return this;
    }

    public ExceptionMatcherBuilder withMessage (Matcher<? super String> subMatcher) {
        matchers.add(new FeatureMatcher<Throwable, String>(subMatcher, "message", "message"){
            @Override
            protected String featureValueOf(Throwable actual) {
                return actual.getMessage();
            }
        });
        return this;
    }

    private static Collection<Throwable> causesOf(Throwable exception) {
        Collection<Throwable> result = new ArrayList<>();
        while (exception != null) {
            result.add(exception);
            exception = exception.getCause();
        }
        return result;
    }

    @Override
    public boolean matches(Object item) {
        return matcher().matches(item);
    }

    private Matcher<Throwable> matcher() {
        return AllOf.allOf(matchers);
    }

    @Override
    public void describeTo(Description description) {
        description.appendDescriptionOf(matcher());
    }
}
