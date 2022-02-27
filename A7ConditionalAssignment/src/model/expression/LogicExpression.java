package model.expression;

import exceptions.expression.InvalidOperandException;
import model.adts.IDictionary;
import model.adts.IHeap;
import model.types.BoolType;
import model.types.Type;
import model.values.BoolValue;
import model.values.Value;

public class LogicExpression implements Expression {
    private final String operator;
    private final Expression expression1;
    private final Expression expression2;

    public LogicExpression(String operator, Expression expression1, Expression expression2) {
        this.operator = operator;
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public Value evaluate(IDictionary<String, Value> symbolTable, IHeap<Integer, Value> heap) throws Exception {
        Value value1 = expression1.evaluate(symbolTable, heap);

        if (value1.getType() != BoolType.T) {
            throw new Exception("1st operand is not a boolean");
        }

        Value value2 = expression2.evaluate(symbolTable, heap);
        if (value2.getType() != BoolType.T) {
            throw new Exception("2nd operand is not a boolean");
        }

        BoolValue boolValue1 = (BoolValue) value1;
        BoolValue boolValue2 = (BoolValue) value2;

        boolean result;
        switch (operator) {
            case "&&" -> {
                result = boolValue1.getValue() && boolValue2.getValue();
            }
            case "||" -> {
                result = boolValue1.getValue() || boolValue2.getValue();
            }
            default -> {
                throw new Exception("Invalid operator for LogicExpression");
            }
        }

        return result ? BoolValue.TRUE : BoolValue.FALSE;
    }

    @Override
    public Type typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type type1, type2;
        type1 = expression1.typecheck(typeEnv);
        type2 = expression2.typecheck(typeEnv);

        if (!type1.equals(BoolType.T)) {
            throw new InvalidOperandException("Logic: 1st operand is not a boolean");
        }

        if (!type2.equals(BoolType.T)) {
            throw new InvalidOperandException("Logic: 2st operand is not a boolean");
        }

        return BoolType.T;
    }

    @Override
    public String toString() {
        return "" + expression1 + operator + expression2;
    }

    @Override
    public Expression deepCopy() {
        return new LogicExpression(operator, expression1.deepCopy(), expression2.deepCopy());
    }
}
