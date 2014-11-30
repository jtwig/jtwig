package org.jtwig.functions.builtin;

import org.jtwig.functions.config.JsonConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonFunctionsTest {
    private JsonFunctions underTest = new JsonFunctions(new JsonConfiguration());

    @Test
    public void testExecute() throws Exception {
        Hello world = new Hello("world");
        assertEquals("{\"hello\":\"world\"}", underTest.jsonEncode(world));
    }

    private static class Hello {
        private String hello;

        private Hello(String hello) {
            this.hello = hello;
        }

        public String getHello() {
            return hello;
        }
    }
}
