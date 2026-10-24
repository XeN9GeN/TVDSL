package lexer.Tokens;

public class LexerException extends RuntimeException {
    public final int line;
    public final int column;

    public LexerException(String message, int line, int column) {

        super(message + " в строке " + line + ", столбце " + column);
        this.line = line;
        this.column = column;
    }
}