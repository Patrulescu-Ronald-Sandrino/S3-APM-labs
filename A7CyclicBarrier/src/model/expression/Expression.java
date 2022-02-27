package model.expression;

import model.adts.IDictionary;
import model.adts.IHeap;
import model.interfaces.Cloneable;
import model.types.Type;
import model.values.Value;

public interface Expression extends Cloneable<Expression> {
    Value evaluate(IDictionary<String, Value> symbolTable, IHeap<Integer, Value> heap) throws Exception;

    Type typecheck(IDictionary<String, Type> typeEnv) throws Exception;
}
