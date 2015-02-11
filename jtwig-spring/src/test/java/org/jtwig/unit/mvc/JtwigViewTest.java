/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.unit.mvc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.jtwig.mvc.JtwigView;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import org.springframework.beans.BeansException;

public class JtwigViewTest {
    @Test(expected = BeansException.class)
    public void ensureAppCtxInitializationCapturesServletException() throws Throwable {
        
        JtwigView underTest = new JtwigView(){
            @Override
            protected GenericServlet getGenericServlet() {
                GenericServlet servlet = spy(super.getGenericServlet());
                try {
                    doThrow(ServletException.class).when(servlet).init(any(ServletConfig.class));
                } catch (ServletException ex) {}
                return servlet;
            }
        };
        
        // Let's just use reflection to call the method
        try {
            Method meth = JtwigView.class.getDeclaredMethod("initApplicationContext");
            meth.setAccessible(true);
            meth.invoke(underTest);
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }
}