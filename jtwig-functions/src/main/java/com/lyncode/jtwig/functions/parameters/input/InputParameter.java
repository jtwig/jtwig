package com.lyncode.jtwig.functions.parameters.input;

public class InputParameter {
    private final int position;
    private final Object value;

    public InputParameter(int position, Object value) {
        this.position = position;
        this.value = value;
    }

    public int position() {
        return position;
    }
}
