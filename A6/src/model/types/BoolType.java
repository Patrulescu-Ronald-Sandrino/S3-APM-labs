package model.types;

import model.values.BoolValue;
import model.values.Value;

public class BoolType implements Type {
    public static final BoolType T = new BoolType();

    private BoolType() {
    }

    public boolean equals(Object another) {
        return another == BoolType.T;
    }

    @Override
    public Value getDefaultValue() {
        return BoolValue.FALSE;
    }

    @Override
    public String toString() {
        return "boolean";
    }

    @Override
    public Type deepCopy() {
        return BoolType.T;
    }
}
