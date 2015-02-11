package org.jtwig.acceptance.operators;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.jtwig.AbstractJtwigTest;
import static org.jtwig.util.SyntacticSugar.given;
import static org.jtwig.util.SyntacticSugar.then;
import org.junit.Test;

public class RelationalOperators extends AbstractJtwigTest {
    @Test
    public void diffOperatorWhenTrue() throws Exception {
        withResource("{% if ('one' != 'two') %}Hi{% endif %}");
        assertThat(theResult(), is(equalTo("Hi")));
    }

    @Test
    public void diffOperatorWhenFalse() throws Exception {
        withResource("{% if ('one' != 'one') %}Hi{% endif %}");
        assertThat(theResult(), is(equalTo("")));
    }
    
    @Test
    public void differentTypesWithSameStringValueAreEqual() throws Exception {
        given(theModel().withModelAttribute("var1", 5L).withModelAttribute("var2", "5"));
        withResource("{{ var1 == var2 }}");
        then(theResult(), is(equalTo("1")));
    }
    @Test
    public void differentTypesWithSameStringValueAreEqual2() throws Exception {
        given(theModel().withModelAttribute("var1", 'a').withModelAttribute("var2", "a"));
        withResource("{{ var1 == var2 }}");
        then(theResult(), is(equalTo("1")));
    }
}
