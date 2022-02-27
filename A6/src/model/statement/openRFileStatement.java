package model.statement;

import model.adts.IFileTable;
import model.adts.IHeap;
import model.adts.IDictionary;
import model.expression.Expression;
import model.state.ProgramState;
import model.types.StringType;
import model.types.Type;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class openRFileStatement implements IStatement {
    private final Expression expression;

    public openRFileStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        IDictionary<String, Value> symbolTable = programState.getSymbolTable();
        IHeap<Integer, Value> heap = programState.getHeap();
        Value fileNameAsValue = expression.evaluate(symbolTable, heap);
        if (fileNameAsValue.getType() != StringType.T) {
            throw new Exception("[openRFileStatement.execute()] Expression type must be StringType");
        }

        IFileTable<StringValue, BufferedReader> fileTable = programState.getFileTable();
        StringValue fileName = (StringValue) fileNameAsValue;
        if (fileTable.lookUp(fileName) != null) {
            throw new Exception("[openRFileStatement.execute()] File " + fileName + " is already a key in the File Table");
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName.getValue()));
            fileTable.update(fileName, bufferedReader);
        } catch (FileNotFoundException fileNotFoundException) {
            throw new Exception("[openRFileStatement.execute()] FileTable Update failed: File " + fileName + " was not found");
        }

        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type type = expression.typecheck(typeEnv);

        if (!type.equals(StringType.T)) {
            throw new Exception("openRFile: expression must be a string");
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new openRFileStatement(expression.deepCopy());
    }

    @Override
    public String toString() {
        return "openRFileStatement(" + expression.toString() + ")";
    }
}
