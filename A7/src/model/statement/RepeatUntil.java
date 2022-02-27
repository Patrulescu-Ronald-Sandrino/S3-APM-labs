package model.statement;

import exceptions.expression.InvalidOperandException;
import model.adts.IDictionary;
import model.adts.IExecutionStack;
import model.adts.IHeap;
import model.expression.Expression;
import model.expression.LogicNegationExpression;
import model.state.ProgramState;
import model.types.BoolType;
import model.types.Type;
import model.values.Value;

public class RepeatUntil implements IStatement {
    private final Expression condition;
    private final IStatement statement;

    public RepeatUntil(Expression condition, IStatement statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public IStatement deepCopy() {
        return new RepeatUntil(condition.deepCopy(), statement.deepCopy());
    }

    @Override
    public ProgramState execute(ProgramState programState) throws Exception {
        IDictionary<String, Value> symbolTable = programState.getSymbolTable();
        IHeap<Integer, Value> heap = programState.getHeap();
        Value conditionValue = condition.evaluate(symbolTable, heap);

        if (conditionValue.getType() != BoolType.T) {
            throw new Exception("[RepeatUntil.evaluate()] Error: condition type is " + conditionValue.getType() + ", expected boolean");
        }

        IStatement newStatement = new CompoundStatement(
               statement,
               new WhileStatement(
                       new LogicNegationExpression(condition),
                       statement
               )
        );

        IExecutionStack<IStatement> executionStack = programState.getExecutionStack();
        executionStack.push(newStatement);

        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        Type conditionType = condition.typecheck(typeEnv);

        if (conditionType != BoolType.T) {
            throw new InvalidOperandException("[RepeatUntil.typecheck()] Error: Condition type is " + conditionType + ", expected boolean");
        }

        statement.typecheck(typeEnv.deepcopy());

        return typeEnv;
    }

    @Override
    public String toString() {
        return "(repeat {" + statement +  "} until " + condition + ")";
    }
}
