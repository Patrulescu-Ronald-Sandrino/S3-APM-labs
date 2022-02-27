package model.statement;

import model.adts.IDictionary;
import model.adts.IExecutionStack;
import model.state.ProgramState;
import model.types.Type;

import java.util.ArrayList;

public class CompoundStatement implements IStatement {
    // Statement1; Statement2
    private final IStatement first;
    private final IStatement second;

    public CompoundStatement(IStatement first, IStatement second) {
        this.first = first;
        this.second = second;
    }

    public IStatement getFirst() {
        return first.deepCopy();
    }

    public IStatement getSecond() {
        return second.deepCopy();
    }

    @Override
    public ProgramState execute(ProgramState programState) {
        IExecutionStack<IStatement> executionStack = programState.getExecutionStack();

        executionStack.push(second);
        executionStack.push(first);
        return null;
    }

    @Override
    public IDictionary<String, Type> typecheck(IDictionary<String, Type> typeEnv) throws Exception {
        return second.typecheck(first.typecheck(typeEnv));
    }

    @Override
    public String toString() {
//        return "(" + first + ";" + second + ")";
        return first + "; " + second;
    }

//    public String toStringWithInOrderTraversal() {
//        return InOrderTraversal().toString();
//    }

    @Override
    public IStatement deepCopy() {
        return new CompoundStatement(first.deepCopy(), second.deepCopy());
    }

    public ArrayList<IStatement> InOrderTraversal() {
        ArrayList<IStatement> result = new ArrayList<>();

        if (first instanceof CompoundStatement) {
            result.addAll(((CompoundStatement) first).InOrderTraversal());
        } else {
            result.add(first);
        }
        if (second instanceof CompoundStatement) {
            result.addAll(((CompoundStatement) second).InOrderTraversal());
        } else {
            result.add(second);
        }

        return result;
    }
}
