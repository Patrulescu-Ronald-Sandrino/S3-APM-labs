package model.types;

import model.interfaces.Cloneable;
import model.values.Value;

public interface Type extends Cloneable<Type> {
    Value getDefaultValue();
}
