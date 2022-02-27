package exceptions.adts.symbol_table;

public class SymbolTableEmptyException extends SymbolTableException {
    private final String message;

    public SymbolTableEmptyException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
