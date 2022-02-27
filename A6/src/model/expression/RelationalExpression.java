package model.expression;

import exceptions.expression.InvalidOperandException;
import exceptions.expression.InvalidOperatorException;
import model.adts.IHeap;
import model.adts.IDictionary;
import model.types.BoolType;
import model.types.IntType;
import model.types.Type;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.Value;

public class RelationalExpression implements Expression {
    private final String operator;
    private final Expression expression1;
    private final Expression expression2;

    public RelationalExpression(String operator, Expression expression1, Expression expression2) {
        this.operator = operator;
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public Value evaluate(IDictionary<String, Value> symbolTable, IHeap<Integer, Value> heap) throws Exception {
        Value value1, value2;
        value1 = expression1.evaluate(symbolTable, heap);

        if (value1.getType() != IntType.T) {
            throw new InvalidOperandException("1st operand is not an integer");
        }

        value2 = expression2.evaluate(symbolTable, heap);
        if (value2.getType() != IntType.T) {
            throw new InvalidOperandException("2st operand is not an integer");
        }

        IntValue intValue1, intValue2;
        intValue1 = (IntValue) value1;
        intValue2 = (IntValue) value2;
        int operand1, operand2;
        operand1 = intValue1.getValue();
        operand2 = intValue2.getValue();

        boolean result;
        switch (operator) {
            case "<": {
                result = operand1 < operand2;
                break;
            }
            case "<=": {
                result = operand1 <= operand2;
                break;
            }
            case "==": {
                result = operand1 == operand2;
                break;
            }
            case "!=": {
                result = operand1 != operand2;
                break;
            }
            case ">": {
                result = operand1 > operand2;
                break;
            }
            case ">=": {
                result = operand1 >= operand2;
                break;
            }
            default: {
                throw new InvalidOperatorException("Invalid operator for RelationalExpression");
            }
        }
        return result ? BoolValue.TRUE : BoolValue.FALSE;
    }

    @Override
    public Type typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type type1, type2;
        type1 = expression1.typecheck(typeEnv);
        type2 = expression2.typecheck(typeEnv);

        if (!type1.equals(IntType.T)) {
            throw new InvalidOperandException("Relational: 1st operand is not an integer");
        }

        if (!type2.equals(IntType.T)) {
            throw new InvalidOperandException("Relational: 2st operand is not an integer");
        }

        return BoolType.T;
    }

    @Override
    public Expression deepCopy() {
        return new RelationalExpression(operator, expression1.deepCopy(), expression2.deepCopy());
    }

    @Override
    public String toString() {
        return "" + expression1 + operator + expression2;
    }
}
