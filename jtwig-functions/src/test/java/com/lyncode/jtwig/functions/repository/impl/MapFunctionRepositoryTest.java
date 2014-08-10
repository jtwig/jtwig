package com.lyncode.jtwig.functions.repository.impl;

import com.lyncode.jtwig.functions.annotations.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.Parameter;
import com.lyncode.jtwig.functions.parameters.input.InputParameters;
import com.lyncode.jtwig.functions.repository.api.FunctionRepository;
import com.lyncode.jtwig.functions.repository.model.Function;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Collection;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MapFunctionRepositoryTest {
    private FunctionRepository underTest;
    private InputParameters parameters = InputParameters.parameters();

    @Before
    public void setUp() throws Exception {
        underTest = new MapFunctionRepository();
    }

    @Test
    public void noFunction() throws Exception {
        assertFalse(underTest.containsFunctionName("some"));
    }

    @Test
    public void addedFunctionShouldBeReturned() throws Exception {
        Function function = mock(Function.class);
        when(function.method()).thenReturn(exampleMethod());

        underTest.add("func", function);
        assertTrue(underTest.containsFunctionName("func"));
        assertThat(underTest.retrieve("func", parameters), hasItem(equalTo(function)));
    }

    private Method exampleMethod() {
        try {
            return getClass().getDeclaredMethod("exampleMethod");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void aliasesShouldShareTheSameListOfFunctions() throws Exception {
        Function function = mock(Function.class);
        when(function.method()).thenReturn(exampleMethod());

        underTest.add("test", function);
        underTest.aliases("test", new String[]{ "some" });

        Collection<Function> some = underTest.retrieve("some", parameters);
        Assert.assertThat(underTest.retrieve("test", parameters), contains(some.toArray(new Function[some.size()])));
    }

    @Test
    public void includeAddsAllDeclaredFunctions() throws Exception {
        underTest.include(new TestClass());

        assertThat(underTest.retrieve("echo", parameters), notNullValue());
    }

    @Test
    public void includesBuiltInFunctions() throws Exception {
        assertThat(underTest.retrieve("batch", InputParameters.parameters(null, 1)), not(empty()));
    }

    public static class TestClass {
        @JtwigFunction(name = "echo")
        public String echo (@Parameter String input) {
            return input;
        }
    }
}