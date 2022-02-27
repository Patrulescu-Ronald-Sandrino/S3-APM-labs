package model.adts;

import exceptions.adts.execution_stack.ExecutionStackFullException;

public interface IExecutionStack<V> {
    void push(V element);

    V top();

    V pop() throws ExecutionStackFullException;

    boolean isEmpty();

}
