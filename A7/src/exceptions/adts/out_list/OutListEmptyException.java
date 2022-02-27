package exceptions.adts.out_list;

public class OutListEmptyException extends OutListException {
    private final String message;

    public OutListEmptyException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
