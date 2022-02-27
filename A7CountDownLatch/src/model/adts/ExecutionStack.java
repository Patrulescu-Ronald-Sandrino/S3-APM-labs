package model.adts;


import exceptions.adts.execution_stack.ExecutionStackFullException;
import model.statement.CompoundStatement;
import model.statement.IStatement;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ExecutionStack<V> implements IExecutionStack<V> {
    private final Deque<V> stack;

    public ExecutionStack() {
        this.stack = new ConcurrentLinkedDeque<>();
    }

    @Override
    public void push(V element) {
        stack.push(element);
    }

    @Override
    public V top() {
        return stack.peek();
    }

    @Override
    public V pop() throws ExecutionStackFullException {
        if (stack.isEmpty()) {
            throw new ExecutionStackFullException("ExecutionStack is empty");
        }

        return stack.pop();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public String toString() {
        String result = "";
        for (Object statement : stack.toArray()) {
            if (statement instanceof IStatement && !(statement instanceof CompoundStatement)) {
                result = result.concat(statement + "\n");
            }
            if (statement instanceof CompoundStatement) {
                for (IStatement atomStatement : ((CompoundStatement) statement).InOrderTraversal()) {
                    result = result.concat(atomStatement.toString() + "\n");
                }
            }
        }

        return result;
    }
}
