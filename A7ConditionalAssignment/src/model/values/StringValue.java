package model.values;

import model.types.StringType;
import model.types.Type;

import java.util.Objects;

public class StringValue implements Value {
    private final String value;

    public StringValue() {
        this("");
    }

    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return StringType.T;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Value deepCopy() {
        return new StringValue(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StringValue && Objects.equals(((StringValue) obj).value, value);
    }
}
