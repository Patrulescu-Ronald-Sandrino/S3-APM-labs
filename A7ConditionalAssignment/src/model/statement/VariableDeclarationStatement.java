package model.statement;

import model.adts.IDictionary;
import model.state.ProgramState;
import model.types.Type;
import model.values.Value;

public class VariableDeclarationStatement implements IStatement {
    // Type Id
    private final String name;
    private final Type type;

    public VariableDeclarationStatement(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        IDictionary<String, Value> symbolTable = programState.getSymbolTable();

        if (symbolTable.isDefined(name)) {
            throw new Exception("Variable is already defined");
        }

        symbolTable.update(name, type.getDefaultValue());
        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        typeEnv.update(name, type);
        return typeEnv;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }

    @Override
    public IStatement deepCopy() {
        return new VariableDeclarationStatement(name, type.deepCopy());
    }
}
