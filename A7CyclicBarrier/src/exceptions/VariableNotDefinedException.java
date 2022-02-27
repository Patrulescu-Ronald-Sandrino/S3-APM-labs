package exceptions;

public class VariableNotDefinedException extends Exception {
    private final String message;

    public VariableNotDefinedException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
