package exceptions.expression;

public class InvalidOperandException extends ExpressionException {
    private final String message;

    public InvalidOperandException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
