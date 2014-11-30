package org.jtwig.functions.json.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jtwig.functions.json.mapper.JacksonFasterXmlJsonMapper;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class JacksonFasterXmlJsonMapperProviderTest {
    private JacksonFasterXmlJsonMapperProvider underTest = new JacksonFasterXmlJsonMapperProvider();

    @Test
    public void classNameReturnsGsonClassName() throws Exception {
        assertThat(underTest.className(), equalTo(ObjectMapper.class.getName()));
    }

    @Test
    public void jsonMapperReturnsGsonImplementation() throws Exception {
        assertThat(underTest.jsonMapper(), instanceOf(JacksonFasterXmlJsonMapper.class));
    }


}
