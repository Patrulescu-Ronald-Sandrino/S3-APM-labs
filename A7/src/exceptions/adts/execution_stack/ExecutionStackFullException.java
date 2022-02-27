package exceptions.adts.execution_stack;

public class ExecutionStackFullException extends ExecutionStackException {
    private final String message;

    public ExecutionStackFullException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}