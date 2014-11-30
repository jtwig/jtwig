package org.jtwig.acceptance.resource;

import org.jtwig.acceptance.AbstractJtwigAcceptanceTest;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.jtwig.util.SyntacticSugar.then;
import static org.jtwig.util.SyntacticSugar.when;
import static org.jtwig.util.matchers.GetMethodMatchers.body;

/**
 * Created with IntelliJ IDEA.
 * User: Vitali Carbivnicii
 * Date: 2014-06-25
 * Time: 10:58
 */
@Controller
public class WebJtwigResourceTest extends AbstractJtwigAcceptanceTest {

    @Test
    public void testIncludeFromExtending() throws Exception {
        when(serverReceivesGetRequest("/"));
        then(theGetResult(), body(is(equalTo("level1-level2-level2"))));
    }

    @RequestMapping("/")
    public String testResult () {
        return "resource/level1/level1";
    }


}
