package model.expression;

import model.adts.IDictionary;
import model.adts.IHeap;
import model.types.Type;
import model.values.Value;

public class VariableExpression implements Expression {
    private final String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public Value evaluate(IDictionary<String, Value> symbolTable, IHeap<Integer, Value> heap) {
        return symbolTable.lookUp(name);
    }

    @Override
    public Type typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        return typeEnv.lookUp(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Expression deepCopy() {
        return new VariableExpression(name);
    }
}
