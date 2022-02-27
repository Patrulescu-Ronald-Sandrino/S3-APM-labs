package model.statement;

import model.adts.IHeap;
import model.adts.IDictionary;
import model.expression.Expression;
import model.state.ProgramState;
import model.types.RefType;
import model.types.Type;
import model.values.RefValue;
import model.values.Value;

public class HeapWriteStatement implements IStatement {
    private final String name;
    private final Expression expression;

    public HeapWriteStatement(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        IDictionary<String, Value> symbolTable = programState.getSymbolTable();
        IHeap<Integer, Value> heap = programState.getHeap();
        if (!symbolTable.isDefined(name)) {
            throw new Exception("[HeapWriteStatement.execute()] Variable " + name + " is not defined");
        }
        Value variableValue = symbolTable.lookUp(name);
        if (!(variableValue.getType() instanceof RefType)) {
            throw new Exception("[HeapWriteStatement.execute()] Variable " + name + " type is not RefType");
        }
        int address = ((RefValue)variableValue).getAddress();
        if (!heap.containsKey(address)) {
            throw new Exception("[HeapWriteStatement.execute()] Address " + address + " is not present in the Heap");
        }

        Value expressionValue = expression.evaluate(symbolTable, heap);
        RefType variableType = (RefType) variableValue.getType();
        if (!expressionValue.getType().equals(variableType.getInner())) {
            throw new Exception("[HeapWriteStatement.execute()] Expected expression type: " + variableType.getInner().toString() + "; got: " + expressionValue.getType().toString());
        }

        heap.put(address, expressionValue);

        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type nameType = typeEnv.lookUp(name);
        Type expressionType = expression.typecheck(typeEnv);

        if (!nameType.equals(new RefType(expressionType))) {
            throw new Exception("Heap Write: RHS and LHS have different types");
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new HeapWriteStatement(name, expression.deepCopy());
    }

    @Override
    public String toString() {
        return "write(" + name + ", " + expression.toString() + ")";
    }
}
