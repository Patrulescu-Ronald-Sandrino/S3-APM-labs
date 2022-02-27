package model.statement;

import model.adts.*;
import model.state.ProgramState;
import model.types.Type;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;

public class ForkStatement implements IStatement {
    private final IStatement statement;

    public ForkStatement(IStatement statement) {
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState programState) {
//        IExecutionStack<IStatement> newExecutionStack = new ExecutionStack<>();
//        ISymbolTable<String, Value> symbolTableDeepCopy = programState.getSymbolTable().deepcopy();
//        IOutList<Value> outList = programState.getOutList();
//        IFileTable<StringValue, BufferedReader> fileTable = programState.getFileTable();
//        IHeap<Integer, Value> heap = programState.getHeap();
//        return new ProgramState(newExecutionStack, symbolTableDeepCopy, outList, fileTable, heap, statement);
        return programState.fork(statement);
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        statement.typecheck(typeEnv.deepcopy());
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ForkStatement(statement.deepCopy());
    }

    @Override
    public String toString() {
        return "fork(" + statement.toString() + ")";
    }
}
