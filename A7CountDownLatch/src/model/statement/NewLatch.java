package model.statement;

import model.adts.IDictionary;
import model.expression.Expression;
import model.state.ProgramState;
import model.types.IntType;
import model.types.Type;
import model.values.IntValue;
import model.values.Value;

import java.util.concurrent.atomic.AtomicInteger;

public class NewLatch implements IStatement {
    private final String variable;
    private final Expression expression;
    private static final AtomicInteger newFreeAddress = new AtomicInteger(1);

    public NewLatch(String variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        Value expressionValue = expression.evaluate(programState.getSymbolTable(), programState.getHeap());
        if (expressionValue.getType() != IntType.T) {
            throw new Exception("[NewLatch.execute()] Error: expression type is " + expressionValue.getType() + ", expected int");
        }
        int num1 = ((IntValue)expressionValue).getValue();

        IDictionary<Integer, Integer> latchTable = programState.getLatchTable();
        int newAddress = newFreeAddress.getAndIncrement();
        latchTable.update(newAddress, num1);

        Value variableValue = programState.getSymbolTable().lookUp(variable);
        if (variableValue == null) {
            throw new Exception("[NewLatch.execute()] Error: variable " + variable + " not found in SymbolTable");
        }
        if (variableValue.getType() != IntType.T) {
            throw new Exception("[NewLatch.execute()] Error: variable type is " + variableValue.getType() + ", expected int");
        }
        programState.getSymbolTable().update(variable, new IntValue(newAddress));

        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type variableType = typeEnv.lookUp(variable);
        if (variableType != IntType.T) {
            throw new Exception("[NewLatch.typecheck()] Error: variable type is " + variableType + ", expected int");
        }

        Type expressionType = expression.typecheck(typeEnv);
        if (expressionType != IntType.T) {
            throw new Exception("[NewLatch.typecheck()] Error: expression type is " + expressionType + ", expected int");
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new NewLatch(variable, expression.deepCopy());
    }

    @Override
    public String toString() {
        return "newLatch(" + variable + "," + expression + ")";
    }
}
