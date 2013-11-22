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
package com.lyncode.jtwig.elements;

import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.render.Calculable;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author "Joao Melo <jmelo@lyncode.com>"
 */
public class For extends ObjectList {
    private static Logger log = LogManager.getLogger(For.class);
    private static final long serialVersionUID = 4648580478468941354L;
    private List<JtwigExpression> exp;
    private String variable;
    private Object value;

    public For(String variable, Object value) {
        this.variable = variable;
        this.value = value;
        this.exp = new ArrayList<JtwigExpression>();
        log.debug(this);
    }

    public String getVariable() {
        return variable;
    }

    public Object getValue() {
        return value;
    }

    @SuppressWarnings("unchecked")
    public String render(HttpServletRequest req, Map<String, Object> model) throws JtwigRenderException {
        String result = "";
        Object values = null;
        if (this.value instanceof Calculable) {
            values = ((Calculable) this.value).calculate(req, model);
        } else values = this.value;

        List<Object> forValues = null;

        if (values == null) forValues = new ObjectList();
        else if (values instanceof Iterable<?>) {
            forValues = new ArrayList<Object>();
            Iterator<Object> iterator = ((Iterable<Object>) values).iterator();
            while (iterator.hasNext())
                forValues.add(iterator.next());
        } else if (values.getClass().isArray()) {
            Object[] list = (Object[]) values;
            forValues = new ArrayList<Object>();
            for (int i = 0; i < list.length; i++)
                forValues.add(list[i]);
        } else {
            forValues = new ArrayList<Object>();
            forValues.add(values);
        }

        forValues = this.applyFilters(forValues, variable, req, model);

        for (int i = 0; i < forValues.size(); i++) {
            Object val = forValues.get(i);
            model.put(variable, val);
            model.put("position", i);
            model.put("first", i == 0);
            model.put("last", (i + 1) == forValues.size());

            result += super.render(req, model);
        }
        return result;
    }


    @SuppressWarnings("unchecked")
    public String render(Map<String, Object> model) throws JtwigRenderException {
        String result = "";
        Object values = null;
        if (this.value instanceof Calculable) {
            values = ((Calculable) this.value).calculate(model);
        } else values = this.value;

        List<Object> forValues = null;

        if (values == null) forValues = new ObjectList();
        else if (values instanceof Iterable<?>) {
            forValues = new ArrayList<Object>();
            Iterator<Object> iterator = ((Iterable<Object>) values).iterator();
            while (iterator.hasNext())
                forValues.add(iterator.next());
        } else if (values.getClass().isArray()) {
            Object[] list = (Object[]) values;
            forValues = new ArrayList<Object>();
            for (int i = 0; i < list.length; i++)
                forValues.add(list[i]);
        } else {
            forValues = new ArrayList<Object>();
            forValues.add(values);
        }

        forValues = this.applyFilters(forValues, variable, model);

        for (int i = 0; i < forValues.size(); i++) {
            Object val = forValues.get(i);
            model.putAll(model);
            model.put(variable, val);
            model.put("position", i);
            model.put("first", i == 0);
            model.put("last", (i + 1) == forValues.size());

            result += super.render(model);
        }
        return result;
    }

    private List<Object> applyFilters(List<Object> forValues, String variable2,
                                      Map<String, Object> model) throws JtwigRenderException {
        List<Object> filtered = new ArrayList<Object>(forValues);
        for (JtwigExpression e : exp) {
            List<Object> newList = new ArrayList<Object>();
            for (Object val : filtered) {
                Map<String, Object> newModel = new TreeMap<String, Object>();
                newModel.putAll(model);
                newModel.put(variable, val);

                if (JtwigExpression.isTrue(e.calculate(newModel)))
                    newList.add(val);
            }
            filtered = newList;
        }
        return filtered;
    }

    private List<Object> applyFilters(List<Object> forValues, String variable2,
                                      HttpServletRequest req, Map<String, Object> model) throws JtwigRenderException {
        List<Object> filtered = new ArrayList<Object>(forValues);
        for (JtwigExpression e : exp) {
            List<Object> newList = new ArrayList<Object>();
            for (Object val : filtered) {
                Map<String, Object> newModel = new TreeMap<String, Object>();
                newModel.putAll(model);
                newModel.put(variable, val);

                if (JtwigExpression.isTrue(e.calculate(req, newModel)))
                    newList.add(val);
            }
            filtered = newList;
        }
        return filtered;
    }

    public String toString() {
        return "FOR: " + variable + ", VALUE: " + value.toString();
    }

    public boolean addFilter(JtwigExpression jtwigExpression) {
        this.exp.add(jtwigExpression);
        return true;
    }
}
