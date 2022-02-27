package exceptions.adts.out_list;

public class OutListFullException extends OutListException {
    private final String message;

    public OutListFullException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
