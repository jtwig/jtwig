package org.jtwig.unit.beans;

import org.jtwig.beans.BeanResolver;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BeanResolverTest {
    private BeanResolver underTest;
    private ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        applicationContext = mock(ApplicationContext.class);
        underTest = new BeanResolver(applicationContext);
    }

    @Test
    public void getTest() throws Exception {
        Object bean = new Object();
        when(applicationContext.getBean("name")).thenReturn(bean);

        Object result = underTest.get("name");

        assertThat(result, equalTo(bean));
    }

    @Test
    public void containsKeyTest() throws Exception {
        when(applicationContext.containsBean("name")).thenReturn(true);

        boolean result = underTest.containsKey("name");

        assertThat(result, is(equalTo(true)));

    }

    @Test
    public void sizeTest() throws Exception {
        when(applicationContext.getBeanDefinitionCount()).thenReturn(1);

        int result = underTest.size();

        assertThat(result, is(equalTo(1)));
    }

    @Test
    public void isEmptyTest() throws Exception {
        when(applicationContext.getBeanDefinitionCount()).thenReturn(1);

        boolean result = underTest.isEmpty();

        assertThat(result, is(equalTo(false)));
    }

    @Test
    public void containsValueTest() throws Exception {
        try {
            underTest.containsValue("");
            fail();
        } catch (Exception exception) {
            assertThat(exception, instanceOf(UnsupportedOperationException.class));
        }
    }

    @Test
    public void putTest() throws Exception {
        try {
            underTest.put("", "");
            fail();
        } catch (Exception exception) {
            assertThat(exception, instanceOf(UnsupportedOperationException.class));
        }
    }

    @Test
    public void putAllTest() throws Exception {
        try {
            underTest.putAll(new HashMap());
            fail();
        } catch (Exception exception) {
            assertThat(exception, instanceOf(UnsupportedOperationException.class));
        }
    }

    @Test
    public void removeTest() throws Exception {
        try {
            underTest.remove("");
            fail();
        } catch (Exception exception) {
            assertThat(exception, instanceOf(UnsupportedOperationException.class));
        }
    }

    @Test
    public void clearTest() throws Exception {
        try {
            underTest.clear();
            fail();
        } catch (Exception exception) {
            assertThat(exception, instanceOf(UnsupportedOperationException.class));
        }
    }

    @Test
    public void keySetTest() throws Exception {
        try {
            underTest.keySet();
            fail();
        } catch (Exception exception) {
            assertThat(exception, instanceOf(UnsupportedOperationException.class));
        }
    }

    @Test
    public void valuesTest() throws Exception {
        try {
            underTest.values();
            fail();
        } catch (Exception exception) {
            assertThat(exception, instanceOf(UnsupportedOperationException.class));
        }
    }

    @Test
    public void entrySetTest() throws Exception {
        try {
            underTest.entrySet();
            fail();
        } catch (Exception exception) {
            assertThat(exception, instanceOf(UnsupportedOperationException.class));
        }
    }
}