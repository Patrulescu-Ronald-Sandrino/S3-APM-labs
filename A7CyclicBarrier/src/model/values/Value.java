package model.values;

import model.interfaces.Cloneable;
import model.types.Type;

public interface Value extends Cloneable<Value> {
    Type getType();
}
