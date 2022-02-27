package model.expression;

import model.adts.IDictionary;
import model.adts.IHeap;
import model.types.Type;
import model.values.Value;

public class ValueExpression implements Expression {
    private final Value value;

    public ValueExpression(Value value) {
        this.value = value;
    }

    @Override
    public Value evaluate(IDictionary<String, Value> symbolTable, IHeap<Integer, Value> heap) {
        return value;
    }

    @Override
    public Type typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        return value.getType();
    }


    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public Expression deepCopy() {
        return new ValueExpression(value.deepCopy());
    }
}
