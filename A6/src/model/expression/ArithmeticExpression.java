package model.expression;

import exceptions.expression.DivisionByZeroException;
import exceptions.expression.InvalidOperandException;
import exceptions.expression.InvalidOperatorException;
import model.adts.IHeap;
import model.adts.IDictionary;
import model.types.IntType;
import model.types.Type;
import model.values.IntValue;
import model.values.Value;


public class ArithmeticExpression implements Expression {
    private final String operator;
    private final Expression expression1;
    private final Expression expression2;

    public ArithmeticExpression(String operator, Expression expression1, Expression expression2) {
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
            throw new InvalidOperandException("2nd operand is not an integer");
        }

        IntValue intValue1 = (IntValue) value1;
        IntValue intValue2 = (IntValue) value2;
        int operand1, operand2;
        operand1 = intValue1.getValue();
        operand2 = intValue2.getValue();
        switch (operator) {
            case "+": {
                return new IntValue(operand1 + operand2);
            }
            case "-": {
                return new IntValue(operand1 - operand2);
            }
            case "*": {
                return new IntValue(operand1 * operand2);
            }
            case "/": {
                if (operand2 == 0)
                    throw new DivisionByZeroException("Division by zero");

                return new IntValue(operand1 / operand2);
            }
            default: {
                throw new InvalidOperatorException("Invalid operator for ArithmeticExpression");
            }
        }
    }

    @Override
    public Type typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type type1, type2;
        type1 = expression1.typecheck(typeEnv);
        type2 = expression2.typecheck(typeEnv);

        if (!type1.equals(IntType.T)) {
            throw new InvalidOperandException("Arithmetic: 1st operand is not an integer");
        }

        if (!type2.equals(IntType.T)) {
            throw new InvalidOperandException("Arithmetic: 2st operand is not an integer");
        }

        return IntType.T;
    }

    @Override
    public Expression deepCopy() {
        return new ArithmeticExpression(operator, expression1.deepCopy(), expression2.deepCopy());
    }

    @Override
    public String toString() {
        return "" + expression1 + operator + expression2;
    }
}
