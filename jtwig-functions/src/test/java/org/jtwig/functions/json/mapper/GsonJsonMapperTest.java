package org.jtwig.functions.json.mapper;

import org.junit.Test;

import java.util.TreeMap;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class GsonJsonMapperTest {
    private GsonJsonMapper underTest = new GsonJsonMapper();

    @Test
    public void checkMapping() throws Exception {
        TreeMap<String, String> input = new TreeMap<String, String>();
        input.put("one", "two");

        String result = underTest.apply(input);

        assertThat(result, is(equalTo("{\"one\":\"two\"}")));
    }
}
