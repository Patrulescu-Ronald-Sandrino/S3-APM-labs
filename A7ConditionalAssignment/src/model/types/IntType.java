package model.types;

import model.values.IntValue;
import model.values.Value;

public class IntType implements Type {
    public static final IntType T = new IntType();

    private IntType() {
    }

    public boolean equals(Object another) {
        return another == IntType.T;
    }

    @Override
    public Value getDefaultValue() {
        return new IntValue(0);
    }

    @Override
    public String toString() {
        return "int";
    }

    @Override
    public Type deepCopy() {
        return IntType.T;
    }
}
