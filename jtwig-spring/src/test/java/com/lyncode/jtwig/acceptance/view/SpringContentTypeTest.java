package com.lyncode.jtwig.acceptance.view;

import com.lyncode.jtwig.acceptance.AbstractJtwigAcceptanceTest;
import org.apache.commons.httpclient.methods.GetMethod;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

import static com.lyncode.jtwig.util.SyntacticSugar.given;
import static com.lyncode.jtwig.util.SyntacticSugar.then;
import static org.hamcrest.core.IsEqual.equalTo;

@Controller
public class SpringContentTypeTest extends AbstractJtwigAcceptanceTest {
    @Test
    public void contentType() throws Exception {
        given(serverReceivesGetRequest("/"));
        then(theGetResult(), contentType(equalTo("text/xml")));
    }

    private Matcher<? super GetMethod> contentType(Matcher<String> matcher) {
        return new FeatureMatcher<GetMethod, String>(matcher, "Content-Type", "") {
            @Override
            protected String featureValueOf(GetMethod actual) {
                return actual.getResponseHeader("Content-Type").getValue();
            }
        };
    }

    @RequestMapping(value = "/")
    public String action (HttpServletResponse response) {
        response.setContentType("text/xml");
        return "view/test";
    }
}
