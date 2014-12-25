package org.jtwig.acceptance.functions;

import org.jtwig.AbstractJtwigTest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.jtwig.util.SyntacticSugar.then;

public class NullVarArgsTest extends AbstractJtwigTest {
    @Test
    public void canExecuteWithNullVarArgsPassed() throws Exception {
        withResource("{{ concat('foo', 'bar', null) }}");
        then(theResult(), is(equalTo("foobar")));
    }
}
