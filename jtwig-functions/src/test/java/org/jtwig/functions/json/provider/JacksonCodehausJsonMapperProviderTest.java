package org.jtwig.functions.json.provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.jtwig.functions.json.mapper.JacksonCodehausJsonMapper;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class JacksonCodehausJsonMapperProviderTest {
    private JacksonCodehausJsonMapperProvider underTest = new JacksonCodehausJsonMapperProvider();

    @Test
    public void classNameReturnsGsonClassName() throws Exception {
        assertThat(underTest.className(), equalTo(ObjectMapper.class.getName()));
    }

    @Test
    public void jsonMapperReturnsGsonImplementation() throws Exception {
        assertThat(underTest.jsonMapper(), instanceOf(JacksonCodehausJsonMapper.class));
    }

}
