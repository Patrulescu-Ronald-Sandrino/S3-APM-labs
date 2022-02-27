package model.statement;

import model.adts.IHeap;
import model.adts.IOutList;
import model.adts.IDictionary;
import model.expression.Expression;
import model.state.ProgramState;
import model.types.Type;
import model.values.Value;

public class PrintStatement implements IStatement {
    // Print(Exp)
    private final Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        IDictionary<String, Value> symbolTable = programState.getSymbolTable();
        IOutList<Value> outList = programState.getOutList();

        IHeap<Integer, Value> heap = programState.getHeap();
        Value value = expression.evaluate(symbolTable, heap);
        outList.add(value);
        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        expression.typecheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString() {
        return "print(" + expression.toString() + ")";
    }

    @Override
    public IStatement deepCopy() {
        return new PrintStatement(expression.deepCopy());
    }
}
