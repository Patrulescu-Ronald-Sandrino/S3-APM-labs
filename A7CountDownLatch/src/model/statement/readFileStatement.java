package model.statement;

import model.adts.IDictionary;
import model.adts.IFileTable;
import model.adts.IHeap;
import model.expression.Expression;
import model.state.ProgramState;
import model.types.IntType;
import model.types.StringType;
import model.types.Type;
import model.values.IntValue;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class readFileStatement implements IStatement {
    private final Expression expression;
    private final String name;

    public readFileStatement(Expression expression, String name) {
        this.expression = expression;
        this.name = name;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        IDictionary<String, Value> symbolTable = programState.getSymbolTable();
        Value variableValue = symbolTable.lookUp(name);
        if (variableValue == null) {
            throw new Exception("[readFileStatement.execute()] Variable name is not defined in the SymbolTable");
        }
        if (variableValue.getType() != IntType.T) {
            throw new Exception("[readFileStatement.execute()] Variable value is not of type IntType");
        }

        IHeap<Integer, Value> heap = programState.getHeap();
        Value expressionValue = expression.evaluate(symbolTable, heap);
        if (expressionValue.getType() != StringType.T) {
            throw new Exception("[readFileStatement.execute()] Evaluated expression is not of type StringType");
        }

        StringValue fileId = (StringValue) expressionValue;
        IFileTable<StringValue, BufferedReader> fileTable = programState.getFileTable();
        BufferedReader bufferedReader;
        if ((bufferedReader = fileTable.lookUp(fileId)) == null) {
            throw new Exception("[readFileStatement.execute()] The file " + fileId + " is not associated with any BufferedReader in the FileTable");
        }

        String line;
        try {
            line = bufferedReader.readLine();
        } catch (IOException ioException) {
            throw new Exception("[readFileStatement.execute()] Failed to read line from file " + fileId);
        }

        IntValue intValue;
        intValue = line == null ? new IntValue(0) : new IntValue(Integer.parseInt(line));
        symbolTable.update(name, intValue);

        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type nameType = typeEnv.lookUp(name);
        Type expressionType = expression.typecheck(typeEnv);

        if (!nameType.equals(IntType.T)) {
            throw new Exception("readFile: variable must be of type int");
        }

        if (!expressionType.equals(StringType.T)) {
            throw new Exception("readFile: expression must be of type string");
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new readFileStatement(expression.deepCopy(), name);
    }

    @Override
    public String toString() {
        return "readFile(" + expression.toString() + ", " + name + ")";
    }
}
