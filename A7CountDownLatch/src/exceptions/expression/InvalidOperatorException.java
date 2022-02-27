package exceptions.expression;

public class InvalidOperatorException extends ExpressionException {
    private final String message;

    public InvalidOperatorException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}