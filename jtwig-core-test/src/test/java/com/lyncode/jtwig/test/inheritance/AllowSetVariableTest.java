package com.lyncode.jtwig.test.inheritance;

import com.lyncode.jtwig.test.AbstractJtwigTest;
import org.junit.Test;

import static com.lyncode.jtwig.SyntacticSugar.after;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: Vitali Carbivnicii
 * Date: 2014-03-18
 * Time: 15:51
 */
public class AllowSetVariableTest extends AbstractJtwigTest {

    @Test
    public void testExtendedOverrides() throws Exception {
        after(jtwigRenders(templateResource("templates/allowSetVariable/extends.twig")));
        assertThat(theRenderedTemplate(), containsString("extends-extends"));
    }

    @Test
    public void testBaseKeeps() throws Exception {
        after(jtwigRenders(templateResource("templates/allowSetVariable/base.twig")));
        assertThat(theRenderedTemplate(), containsString("base-base"));
    }

}
