package model.statement;

import model.adts.IDictionary;
import model.adts.IFileTable;
import model.adts.IHeap;
import model.expression.Expression;
import model.state.ProgramState;
import model.types.StringType;
import model.types.Type;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class closeRFileStatement implements IStatement {
    private final Expression expression;

    public closeRFileStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        IDictionary<String, Value> symbolTable = programState.getSymbolTable();
        IHeap<Integer, Value> heap = programState.getHeap();
        Value value = expression.evaluate(symbolTable, heap);
        if (value.getType() != StringType.T) {
            throw new Exception("[closeRFileStatement.execute()] Evaluated expression is not of StringType");
        }

        IFileTable<StringValue, BufferedReader> fileTable = programState.getFileTable();
        BufferedReader bufferedReader;
        StringValue fileId = (StringValue) value;
        if ((bufferedReader = fileTable.lookUp(fileId)) == null) {
            throw new Exception("[closeRFileStatement.execute()] No entry in the File Table for file " + fileId);
        }

        try {
            bufferedReader.close();
            fileTable.remove(fileId);
        } catch (IOException ioException) {
            throw new Exception("[closeRFileStatement.execute()] Failed to close file " + fileId);
        } catch (NullPointerException nullPointerException) {
            // TODO ask whether exceptions thrown by calls on collection's methods should be caught (and rethrown?) and where that should be done
            //  for example line 36: fileTable.remove(fileId);
            throw new Exception("[closeRFileStatement.execute()] Failed to remove file " + fileId + " from the File Table");
        }

        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type type = expression.typecheck(typeEnv);
        if (!type.equals(StringType.T)) {
            throw new Exception("closeRFile: expression must be a string");
        }


        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new closeRFileStatement(expression.deepCopy());
    }

    @Override
    public String toString() {
        return "closeRFile(" + expression.toString() + ")";
    }
}
