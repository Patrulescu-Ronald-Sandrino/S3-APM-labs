package model.statement;

import javafx.util.Pair;
import model.adts.IDictionary;
import model.expression.Expression;
import model.state.ProgramState;
import model.types.IntType;
import model.types.Type;
import model.values.IntValue;
import model.values.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NewBarrier implements IStatement{
    private final String variable;
    private final Expression expression;
    // TODO: what if 2 thread are executing NewBarrier concurrently?
    //  I think that without synchronized read/write of the nextFreeAddress we would have race condition.
    private static final AtomicInteger nextFreeAddress = new AtomicInteger(1);

    public NewBarrier(String variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        Value expressionValue = expression.evaluate(programState.getSymbolTable(), programState.getHeap());
        if (expressionValue.getType() != IntType.T) {
            throw new Exception("[NewBarrier.execute()] Error: expression type is " + expressionValue.getType() + ", expected int");
        }

        IDictionary<Integer, Pair<Integer, List<Integer>>> barrierTable =  programState.getBarrierTable();
        int Nr = ((IntValue)expressionValue).getValue();
        int newAddress = nextFreeAddress.getAndIncrement();
        barrierTable.update(newAddress, new Pair<>(Nr, new ArrayList<>()));


        Value value = programState.getSymbolTable().lookUp(variable);
        if (value == null) {
            throw new Exception("[Await.execute()] Error: variable " + variable + " not found in SymbolTable");
        }
        if (value.getType() != IntType.T) {
            throw new Exception("[Await.execute()] Error: variable " + variable + " has type " + value.getType() + ", expected int");
        }
        programState.getSymbolTable().update(variable, new IntValue(newAddress));

        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type variableType = typeEnv.lookUp(variable);

        if (variableType != IntType.T) {
            throw new Exception("[NewBarrier.typecheck()] Error: variable has type " + variableType + ", expected int");
        }

        Type expressionType = expression.typecheck(typeEnv);

        if (expressionType != IntType.T) {
            throw new Exception("[NewBarrier.typecheck()] Error: expression has type " + expressionType + ", expected int");
        }

        return typeEnv;
    }


    @Override
    public IStatement deepCopy() {
        return new NewBarrier(variable, expression.deepCopy());
    }

    @Override
    public String toString() {
        return "newBarrier(" + variable + ", " + expression + ")";
    }
}
