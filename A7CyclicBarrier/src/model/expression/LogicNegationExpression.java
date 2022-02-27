package model.expression;

import exceptions.expression.InvalidOperandException;
import model.adts.IDictionary;
import model.adts.IHeap;
import model.types.BoolType;
import model.types.Type;
import model.values.BoolValue;
import model.values.Value;

public class LogicNegationExpression implements Expression {
    private final Expression expression;

    public LogicNegationExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Expression deepCopy() {
        return new LogicNegationExpression(expression.deepCopy());
    }

    @Override
    public Value evaluate(IDictionary<String, Value> symbolTable, IHeap<Integer, Value> heap) throws Exception {
        Value expressionValue = expression.evaluate(symbolTable, heap);

        if (expressionValue.getType() != BoolType.T) {
            throw new Exception("[LogicNegation.evaluate()] Error: expression Value is " + expressionValue + "expected boolean");
        }

//        BoolValue expressionBooleanValue = (BoolValue) expressionValue;
//        return expressionBooleanValue.getValue() ? BoolValue.FALSE : BoolValue.TRUE;
        return expressionValue == BoolValue.TRUE ? BoolValue.FALSE : BoolValue.TRUE;
    }

    @Override
    public Type typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type expressionType;
        expressionType = expression.typecheck(typeEnv);

        if (!expressionType.equals(BoolType.T)) {
            throw new InvalidOperandException("LogicNegation: operand must be boolean");
        }

        return BoolType.T;
    }

    @Override
    public String toString() {
        return "!(" + expression + ")";
    }
}
