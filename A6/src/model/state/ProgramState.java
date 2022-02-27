package model.state;


import model.adts.*;
import model.statement.IStatement;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class ProgramState {
    private final IExecutionStack<IStatement> executionStack;
    private final IDictionary<String, Value> symbolTable;
    private final IOutList<Value> outList;
    private final IFileTable<StringValue, BufferedReader> fileTable;
    private final IHeap<Integer, Value> heap;
    private final IStatement originalProgram;
    private static int id = 1;
    private int tid = getNewProgramStateId(1000);

    private synchronized int getNewProgramStateId(int  addition) {
        id = id + addition;
        return id;
    }

    public ProgramState(IStatement originalProgram) {
        this.executionStack = new ExecutionStack<>();
        this.symbolTable = new Dictionary<>();
        this.outList = new OutList<>();
        this.fileTable = new FileTable();
        this.heap = new Heap();
        this.originalProgram = originalProgram;
        this.executionStack.push(originalProgram);
    }

    public ProgramState(IExecutionStack<IStatement> executionStack, IDictionary<String, Value> symbolTable, IOutList<Value> outList, IFileTable<StringValue, BufferedReader> fileTable, IHeap<Integer, Value> heap, IStatement originalProgram) {
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.outList = outList;
        this.fileTable = fileTable;
        this.heap = heap;
        this.originalProgram = originalProgram.deepCopy(); // TODO: find out how this properly is done (do the same for the above constructor)
        this.executionStack.push(originalProgram);
    }

    public IExecutionStack<IStatement> getExecutionStack() {
        return executionStack;
    }

    public IDictionary<String, Value> getSymbolTable() {
        return symbolTable;
    }

    public IOutList<Value> getOutList() {
        return outList;
    }

    public IFileTable<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public IHeap<Integer, Value> getHeap() {
        return heap;
    }

    public IStatement getOriginalProgram() {
        return originalProgram;
    }

    @Override
    public String toString() {
        return "" + executionStack + '\n' + symbolTable + '\n' + outList + '\n' + fileTable + '\n' + heap;
    }

    public String prettyPrint() {
        String result = "";
        result += "\033[1;31mExecutionStack:\033[0m\n" + executionStack + '\n';
        result += "\033[1;31mSymbolTable:\033[0m\n" + symbolTable + '\n';
        result += "\033[1;31mOutList\033[0m\n" + outList + '\n';
        result += "\033[1;31mFileTable:\033[0m\n" + fileTable + '\n';
        result += "\033[1;31mHeap:\033[0m\n" + heap + '\n';
        result += "\n\n\n";
        return result;
    }

    public String prettyPrintFunctional(String prefix, String suffix) {
        Map<Integer, Map.Entry<String, String>> map = new HashMap<>();
        map.put(1, Map.entry("ExecutionStack", executionStack.toString()));
        map.put(2, Map.entry("SymbolTable", symbolTable.toString()));
        map.put(3, Map.entry("OutList", outList.toString()));
        map.put(4, Map.entry("FileTable", fileTable.toString()));
        map.put(5, Map.entry("Heap", heap.toString()));
        return String.format("ID: %4s\n", tid) + map.values().stream()
                .map(s -> prefix + s.getKey() + suffix + "\n" + s.getValue() + "\n")
                .reduce("", (acc, item) -> acc+ item).concat("\n\n\n");
    }

    public boolean isNotCompleted() {
        return !executionStack.isEmpty();
    }

    public ProgramState oneStep() throws Exception {
        if (executionStack.isEmpty()) {
            throw new Exception("ProgramState's ExecutionStack is empty!");
        }

        IStatement statement = executionStack.pop();
        return statement.execute(this);
    }

    private ProgramState(ProgramState programState, IStatement statement) {
        this.executionStack = new ExecutionStack<>();
        this.symbolTable = programState.symbolTable.deepcopy();
        this.outList = programState.outList;
        this.fileTable = programState.fileTable;
        this.heap = programState.heap;
        this.originalProgram = statement.deepCopy();
        this.executionStack.push(statement);
        this.tid = programState.tid + 1;
    }

    public ProgramState fork(IStatement statement) {
        // TODO implement or remove
        //      verify synchronization problems with static id
        return new ProgramState(this, statement);
    }
}
