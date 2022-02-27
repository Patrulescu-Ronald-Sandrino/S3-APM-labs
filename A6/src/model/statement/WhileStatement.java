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

public class WhileStatement implements IStatement {
    private final Expression condition;
    private final IStatement statement;

    public WhileStatement(Expression condition, IStatement statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public IStatement deepCopy() {
        return new WhileStatement(condition.deepCopy(), statement.deepCopy());
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        IDictionary<String, Value> symbolTable = programState.getSymbolTable();
        IHeap<Integer, Value> heap = programState.getHeap();
        Value expressionValue = condition.evaluate(symbolTable, heap);
        if (expressionValue.getType() != BoolType.T) {
            throw new Exception("[WhileStatement.execute()] Type of while expression must be BoolType");
        }

        BoolValue boolValue = (BoolValue) expressionValue;
        if (boolValue.getValue()) {
            IExecutionStack<IStatement> stack = programState.getExecutionStack();
            stack.push(this);
            stack.push(statement);
        }

        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type conditionType = condition.typecheck(typeEnv);

        if (!conditionType.equals(BoolType.T)) {
            throw new Exception("The condition of WHILE has not the type bool");
        }

        statement.typecheck(typeEnv.deepcopy());

        return typeEnv;
    }

    @Override
    public String toString() {
        return "while(" + condition.toString() + ") {" + statement.toString() + "}";
    }
}
