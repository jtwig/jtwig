package org.jtwig.acceptance.functions;

import org.jtwig.acceptance.AbstractJtwigTest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.jtwig.util.SyntacticSugar.then;
import static org.jtwig.util.SyntacticSugar.when;

public class NullVarArgsTest extends AbstractJtwigTest {
	@Test
	public void canExecuteWithNullVarArgsPassed() throws Exception {
		when(jtwigRenders(template("{{ concat('foo', 'bar', null) }}")));
		then(theRenderedTemplate(), is(equalTo("foobar")));
	}
}
