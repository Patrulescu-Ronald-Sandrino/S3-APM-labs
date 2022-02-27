package exceptions.repository;

public class RepositoryEmptyException extends RepositoryException {
    private final String message;

    public RepositoryEmptyException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
