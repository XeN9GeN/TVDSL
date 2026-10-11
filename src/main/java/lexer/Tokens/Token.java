package lexer.Tokens;
import lexer.Tokens.TokenType;

public class Token {
    public final TokenType type;
    public final String lexeme;
    public final int line;//строка
    public final int s_ind;//индекс символа

    public Token(TokenType t, String lx, int l, int c) {
        this.type = t;
        this.lexeme = lx;
        this.line = l;
        this.s_ind = c;
    }

    @Override
    public String toString(){
        return type + "('" + lexeme + "')" + " at " + line + ";" + s_ind;
    }

}
