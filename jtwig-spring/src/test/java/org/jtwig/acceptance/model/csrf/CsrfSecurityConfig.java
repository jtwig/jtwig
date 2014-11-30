package org.jtwig.acceptance.model.csrf;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

/**
 * Created with IntelliJ IDEA.
 * User: Vitali Carbivnicii
 * Date: 2014-06-23
 * Time: 10:57
 */
@Configuration
@EnableWebMvcSecurity
public class CsrfSecurityConfig extends WebSecurityConfigurerAdapter {

}
