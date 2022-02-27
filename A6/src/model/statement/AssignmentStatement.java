package model.statement;

import exceptions.VariableNotDefinedException;
import model.adts.IHeap;
import model.adts.IDictionary;
import model.expression.Expression;
import model.state.ProgramState;
import model.types.Type;
import model.values.Value;

public class AssignmentStatement implements IStatement {
    // Id = Exp
    private final String name;
    private final Expression expression;

    public AssignmentStatement(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        IDictionary<String, Value> symbolTable = programState.getSymbolTable();

        if (!symbolTable.isDefined(name)) {
            throw new VariableNotDefinedException("Variable " + name + " is not defined");
        }

        IHeap<Integer, Value> heap = programState.getHeap();
        Value value = expression.evaluate(symbolTable, heap);
        Value oldValue = symbolTable.lookUp(name);
        Type idType = oldValue.getType();

//        if (!value.getType().equals(idType)) { // old variant
        if (value.getType() != idType) {
            throw new Exception("Type of expression and type of variable " + name + " do not match");
        }

        symbolTable.update(name, value);
        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type idType = typeEnv.lookUp(name);
        Type expressionType = expression.typecheck(typeEnv);

        if (!idType.equals(expressionType)) {
            throw new Exception("Assignment: RHS " + expression.toString() + " and LHS " + name + " have different types");
        }

        return typeEnv;
    }

    public String toString() {
        return name + "=" + expression.toString();
    }

    @Override
    public IStatement deepCopy() {
        return new AssignmentStatement(name, expression.deepCopy());
    }
}
