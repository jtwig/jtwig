package com.lyncode.jtwig.acceptance.resource;

import com.lyncode.jtwig.acceptance.AbstractJtwigAcceptanceTest;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.lyncode.jtwig.util.SyntacticSugar.then;
import static com.lyncode.jtwig.util.SyntacticSugar.when;
import static com.lyncode.jtwig.util.matchers.GetMethodMatchers.body;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

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
