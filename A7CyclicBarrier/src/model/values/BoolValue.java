package model.values;

import model.types.BoolType;
import model.types.Type;

public class BoolValue implements Value {
    public static final BoolValue TRUE = new BoolValue(true);
    public static final BoolValue FALSE = new BoolValue(false);

    private final boolean value;

    private BoolValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return BoolType.T;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    // it doesn't actually deep copy
    public Value deepCopy() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }
}
