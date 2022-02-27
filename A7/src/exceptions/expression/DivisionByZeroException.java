package exceptions.expression;

public class DivisionByZeroException extends Exception {
    private final String message;

    public DivisionByZeroException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
