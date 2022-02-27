package model.values;

import model.types.IntType;
import model.types.Type;

import java.util.Objects;

public class IntValue implements Value {
    private final Integer value;

    public IntValue() {
        this(0);
    }

    public IntValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return IntType.T;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public Value deepCopy() {
        return new IntValue(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IntValue && Objects.equals(((IntValue) obj).value, value);
    }
}
