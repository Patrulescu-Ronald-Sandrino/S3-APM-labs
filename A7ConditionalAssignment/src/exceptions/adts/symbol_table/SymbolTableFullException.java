package exceptions.adts.symbol_table;

public class SymbolTableFullException extends SymbolTableException {
    private final String message;

    public SymbolTableFullException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
