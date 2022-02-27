package model.types;

import model.values.RefValue;
import model.values.Value;

public class RefType implements Type {
    private final Type inner;

    public RefType(Type inner) {
        this.inner = inner;
    }

    public Type getInner() {
        return inner;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RefType && inner.equals(((RefType) obj).getInner());
    }

    @Override
    public Type deepCopy() {
        return new RefType(inner.deepCopy());
    }

    @Override
    public String toString() {
        return "Ref(" + inner.toString() + ")";
    }

    @Override
    public Value getDefaultValue() {
        return new RefValue(0, inner);
    }

}
