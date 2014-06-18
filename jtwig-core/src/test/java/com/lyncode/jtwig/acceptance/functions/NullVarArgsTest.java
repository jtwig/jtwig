package com.lyncode.jtwig.acceptance.functions;

import com.lyncode.jtwig.acceptance.AbstractJtwigTest;
import org.junit.Test;

import static com.lyncode.jtwig.util.SyntacticSugar.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class NullVarArgsTest extends AbstractJtwigTest {
	@Test
	public void canExecuteWithNullVarArgsPassed() throws Exception {
		when(jtwigRenders(template("{{ concat('foo', 'bar', null) }}")));
		then(theRenderedTemplate(), is(equalTo("foobar")));
	}
}
