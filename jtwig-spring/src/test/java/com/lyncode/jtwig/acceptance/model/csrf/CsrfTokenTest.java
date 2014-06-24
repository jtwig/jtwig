package com.lyncode.jtwig.acceptance.model.csrf;

import com.lyncode.jtwig.acceptance.AbstractJtwigAcceptanceTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static com.lyncode.jtwig.util.SyntacticSugar.then;
import static com.lyncode.jtwig.util.SyntacticSugar.when;
import static com.lyncode.jtwig.util.matchers.GetMethodMatchers.body;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created with IntelliJ IDEA.
 * User: Vitali Carbivnicii
 * Date: 2014-06-23
 * Time: 10:42
 */
@Controller
@ComponentScan(basePackageClasses = { CsrfTokenTest.class })
public class CsrfTokenTest extends AbstractJtwigAcceptanceTest {

    @Autowired
    HttpServletRequest request;

    @RequestMapping("/csrf")
    public String csrfAction() {
        request.setAttribute(CsrfToken.class.getName(), new DefaultCsrfToken("csrfHeader", "csrfParameter", "SOME-TOKEN"));
        return "csrf/csrf";
    }

    @Test
    public void csrfTest() throws Exception {
        // the csrf processing isn't triggered in this environment somewhat
        when(serverReceivesGetRequest("/csrf"));
        then(theGetResult(), body(is(equalTo("SOME-TOKEN-csrfParameter-csrfHeader"))));
    }

}
