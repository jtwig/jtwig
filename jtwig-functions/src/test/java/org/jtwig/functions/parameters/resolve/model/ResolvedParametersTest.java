package org.jtwig.functions.parameters.resolve.model;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jtwig.functions.reflection.JavaMethodParameter;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;

public class ResolvedParametersTest {
    private ResolvedParameters underTest;

    @Test
    public void unresolvedParametersShouldReturnParameters() throws Exception {
        underTest = new ResolvedParameters(exampleMethod());
        assertThat(underTest.unresolvedParameters(), hasSize(1));
    }

    @Test
    public void unresolvedParametersShouldReturnParametersWithVarArgs() throws Exception {
        underTest = new ResolvedParameters(exampleMethodWithVarArgs());
        assertThat(underTest.unresolvedParameters(), hasSize(2));
    }

    @Test
    public void afterResolvingOneParameterThenItShouldDisappearFromTheUnresolvedList() throws Exception {
        underTest = new ResolvedParameters(exampleMethod());
        underTest.resolve(underTest.unresolvedParameters().iterator().next(), null);
        assertThat(underTest.unresolvedParameters(), is(empty()));
    }

    @Test
    public void afterResolvingOneParameterThenItShouldDisappearFromTheUnresolvedListWithVarArgs() throws Exception {
        underTest = new ResolvedParameters(exampleMethodWithVarArgs());
        underTest.resolve(underTest.unresolvedParameters().iterator().next(), null);
        assertThat(underTest.unresolvedParameters(), hasSize(1));
        assertThat(underTest.unresolvedParameters(), hasItem(withPosition(equalTo(1))));
    }

    private Method exampleMethod() {
        try {
            return this.getClass().getDeclaredMethod("example", String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Method exampleMethodWithVarArgs() {
        try {
            return this.getClass().getDeclaredMethod("example", String.class, Object[].class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public String example (String example) {
        return example;
    }

    public String example (String example, Object... values) {
        return example;
    }

    private Matcher<JavaMethodParameter> withPosition(Matcher<Integer> matcher) {
        return new FeatureMatcher<JavaMethodParameter, Integer>(matcher, "position", "") {
            @Override
            protected Integer featureValueOf(JavaMethodParameter actual) {
                return actual.position();
            }
        };
    }
}
