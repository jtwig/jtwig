package com.lyncode.jtwig.functions.parameters.input;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class InputParameters {
    public static InputParameters parameters (Object... parameters) {
        return new InputParameters(parameters);
    }

    private List<Object> list;

    public InputParameters(Object... parameters) {
        list = asList(parameters);
    }

    public Object valueAt(int index) {
        return list.get(index);
    }

    public int length() {
        return list.size();
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InputParameters) {
            return ((InputParameters) obj).list.equals(list);
        }
        return super.equals(obj);
    }

    public List<Class> toTypeList() {
        List<Class> result = new ArrayList<>();
        for (Object o : list) {
            if (o == null) result.add(null);
            else result.add(o.getClass());
        }
        return result;
    }
}
