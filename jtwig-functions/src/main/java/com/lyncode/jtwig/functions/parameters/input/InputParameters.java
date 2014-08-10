package com.lyncode.jtwig.functions.parameters.input;

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
}
