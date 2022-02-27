package model.types;

import model.values.StringValue;
import model.values.Value;

public class StringType implements Type {
    public static final StringType T = new StringType();

    private StringType() {
    }

    public boolean equals(Object another) {
        return another == StringType.T;
    }

    @Override
    public Value getDefaultValue() {
        return new StringValue("");
    }

    @Override
    public String toString() {
        return "string";
    }

    @Override
    public Type deepCopy() {
        return StringType.T;
    }
}
