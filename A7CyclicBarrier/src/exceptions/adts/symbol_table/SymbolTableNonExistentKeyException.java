package exceptions.adts.symbol_table;

public class SymbolTableNonExistentKeyException extends SymbolTableException {
    private final String message;

    public SymbolTableNonExistentKeyException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}