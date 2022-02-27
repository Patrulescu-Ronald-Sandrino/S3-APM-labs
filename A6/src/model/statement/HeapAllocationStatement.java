package model.statement;

import model.adts.Heap;
import model.adts.IHeap;
import model.adts.IDictionary;
import model.expression.Expression;
import model.state.ProgramState;
import model.types.RefType;
import model.types.Type;
import model.values.RefValue;
import model.values.Value;

public class HeapAllocationStatement implements IStatement {
    private final String name;
    private final Expression expression;

    public HeapAllocationStatement(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        IDictionary<String, Value> symbolTable = programState.getSymbolTable();
        IHeap<Integer, Value> heap = programState.getHeap();
        if (!symbolTable.isDefined(name)) {
            throw new Exception("[HeapAllocationStatement.execute()] Variable " + name + " not defined");
        }
        Value variableValue = symbolTable.lookUp(name);
        if (!(variableValue.getType() instanceof RefType)) {
            throw new Exception("[HeapAllocationStatement.execute()] Variable " + name + " is of type:" + variableValue.getType().toString() + " , expected: Ref(...)");
        }

        Value expressionValue = expression.evaluate(symbolTable, heap);
        if (!(expressionValue.getType().equals(((RefType) variableValue.getType()).getInner()))) {
            throw new Exception("[HeapAllocationStatement.execute()] Expression type is: " + expressionValue.getType() + ", but Variable Inner Type is: " + variableValue.getType());
        }


        int address = ((Heap) heap).getNextFreeAddress();
        heap.put(-1, expressionValue);

        symbolTable.update(name, new RefValue(address, ((RefType) variableValue.getType()).getInner()));

        return null;

    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type nameType = typeEnv.lookUp(name);
        Type expressionType = expression.typecheck(typeEnv);

        if (!nameType.equals(new RefType(expressionType))) {
            throw new Exception("Heap Allocation: RHS and LHS have different types");
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new HeapAllocationStatement(name, expression.deepCopy());
    }

    @Override
    public String toString() {
        return "new(" + name + ", " + expression.toString() + ")";
    }
}
