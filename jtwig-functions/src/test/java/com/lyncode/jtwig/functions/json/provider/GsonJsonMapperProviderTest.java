package com.lyncode.jtwig.functions.json.provider;

import com.google.gson.Gson;
import com.lyncode.jtwig.functions.json.mapper.GsonJsonMapper;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class GsonJsonMapperProviderTest {
    private GsonJsonMapperProvider underTest = new GsonJsonMapperProvider();

    @Test
    public void classNameReturnsGsonClassName() throws Exception {
        assertThat(underTest.className(), equalTo(Gson.class.getName()));
    }

    @Test
    public void jsonMapperReturnsGsonImplementation() throws Exception {
        assertThat(underTest.jsonMapper(), instanceOf(GsonJsonMapper.class));
    }
}