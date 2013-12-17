/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.jtwig.mvc;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.AbstractTemplateView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JtwigView extends AbstractTemplateView {
    private static Logger log = LogManager.getLogger(JtwigView.class);

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContextUtils.getTheme(request).getName();
    }
}
