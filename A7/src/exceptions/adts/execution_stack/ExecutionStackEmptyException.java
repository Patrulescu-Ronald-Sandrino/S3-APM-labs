package exceptions.adts.execution_stack;

public class ExecutionStackEmptyException extends ExecutionStackException {
    private final String message;

    public ExecutionStackEmptyException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}