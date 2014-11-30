package org.jtwig.functions.util;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class CastMatcher<V> extends BaseMatcher<Object> {
    private final Class<V> typeClass;
    private final Matcher<V> matcher;

    public CastMatcher(Class<V> typeClass, Matcher<V> matcher) {
        this.typeClass = typeClass;
        this.matcher = matcher;
    }

    @Override
    public boolean matches(Object item) {
        if (typeClass.isInstance(item))
            return matcher.matches(item);
        else
            return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendDescriptionOf(matcher);
    }
}
