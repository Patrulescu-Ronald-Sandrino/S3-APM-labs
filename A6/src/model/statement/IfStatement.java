package model.statement;

import model.adts.IExecutionStack;
import model.adts.IHeap;
import model.adts.IDictionary;
import model.expression.Expression;
import model.state.ProgramState;
import model.types.BoolType;
import model.types.Type;
import model.values.BoolValue;
import model.values.Value;

public class IfStatement implements IStatement {
    // If Exp Then Stmt1 Else Stmt 2
    private final Expression condition;
    private final IStatement thenStatement;
    private final IStatement elseStatement;

    public IfStatement(Expression condition, IStatement thenStatement, IStatement elseStatement) {
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        IDictionary<String, Value> symbolTable = programState.getSymbolTable();
        IHeap<Integer, Value> heap = programState.getHeap();
        Value conditionValue = condition.evaluate(symbolTable, heap);

        if (conditionValue.getType() != BoolType.T) {
            throw new Exception("Conditional expression is not a boolean");
        }

        IExecutionStack<IStatement> executionStack = programState.getExecutionStack();
        if (conditionValue == BoolValue.TRUE) {
            executionStack.push(thenStatement);
        } else {
            executionStack.push(elseStatement);
        }

        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type conditionType = condition.typecheck(typeEnv);

        if (!conditionType.equals(BoolType.T)) {
            throw new Exception("The condition of IF has not the type bool");
        }

        thenStatement.typecheck(typeEnv.deepcopy());
        elseStatement.typecheck(typeEnv.deepcopy());

        return typeEnv;
    }

    @Override
    public String toString() {
        return "IF(" + condition.toString() + ")THEN(" + thenStatement.toString() + ")ELSE(" + elseStatement.toString() + ")";
    }

    @Override
    public IStatement deepCopy() {
        return new IfStatement(condition.deepCopy(), thenStatement.deepCopy(), elseStatement.deepCopy());
    }
}
