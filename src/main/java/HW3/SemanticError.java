package HW3;

public class SemanticError {
    public final String message;
    public final int line;
    public final int column;

    public SemanticError(String message, int line, int column) {
        this.message = message;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return "[Ошибка] Строка " + line + ", Столбец " + column + ": " + message;
    }
}
