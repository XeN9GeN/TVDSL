package lexer;
import DFA.DFAAutomaton;
import DFA.DFAState;
import lexer.Tokens.LexerException;
import lexer.Tokens.Token;
import lexer.Tokens.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FunnyLexer {
    private final DFAAutomaton automaton;
    private final DFAState start_state;
    private final Map<String, TokenType> keyw = new HashMap<>();
    private final Map<String, TokenType> fixed_tokens = new HashMap<>();

    public FunnyLexer(DFAAutomaton auto){
        this.automaton = auto;

        DFAState found_s = null;
        for(DFAState s : automaton.states){
            if(s.dfa_state_id == auto.start_state_id){
                found_s = s;
                break;
            }
        }

        if(found_s == null) throw new IllegalArgumentException("Didnt found start state\n");

        this.start_state = found_s;
        initKeyW();
        initFixedT();
    }

    //[A-Za-z_][A-Za-z0-9_]*
    private void initKeyW(){
        keyw.put("function", TokenType.KW_FUNCTION);
        keyw.put("returns", TokenType.KW_RETURNS);
        keyw.put("requires", TokenType.KW_REQUIRES);
        keyw.put("ensures", TokenType.KW_ENSURES);
        keyw.put("uses", TokenType.KW_USES);

        keyw.put("if", TokenType.KW_IF);
        keyw.put("else", TokenType.KW_ELSE);
        keyw.put("while", TokenType.KW_WHILE);
        keyw.put("invariant", TokenType.KW_INVARIANT);

        keyw.put("assert", TokenType.KW_ASSERT);
        keyw.put("assume", TokenType.KW_ASSUME);
        keyw.put("forall", TokenType.KW_FORALL);
        keyw.put("exists", TokenType.KW_EXISTS);

        keyw.put("true", TokenType.KW_TRUE);
        keyw.put("false", TokenType.KW_FALSE);
        keyw.put("not", TokenType.KW_NOT);
        keyw.put("and", TokenType.KW_AND);
        keyw.put("or", TokenType.KW_OR);

        keyw.put("int", TokenType.KW_INT);
        keyw.put("length", TokenType.KW_LENGTH);
    }
    private void initFixedT(){
        fixed_tokens.put("=", TokenType.OP_EQ);
        fixed_tokens.put("==", TokenType.OP_EEQ);
        fixed_tokens.put("!=", TokenType.OP_NEQ);
        fixed_tokens.put("<", TokenType.OP_LT);
        fixed_tokens.put("<=", TokenType.OP_LE);
        fixed_tokens.put(">", TokenType.OP_GT);
        fixed_tokens.put(">=", TokenType.OP_GE);

        fixed_tokens.put("+", TokenType.OP_PLUS);
        fixed_tokens.put("-", TokenType.OP_MINUS);
        fixed_tokens.put("*", TokenType.OP_STAR);
        fixed_tokens.put("/", TokenType.OP_SLASH);

        fixed_tokens.put("->", TokenType.OP_IMPLIES);
        fixed_tokens.put("=>", TokenType.OP_FORMULA_ARROW);

        fixed_tokens.put("(", TokenType.LPAREN);
        fixed_tokens.put(")", TokenType.RPAREN);
        fixed_tokens.put("[", TokenType.LBRACKET);
        fixed_tokens.put("]", TokenType.RBRACKET);
        fixed_tokens.put("{", TokenType.LBRACE);
        fixed_tokens.put("}", TokenType.RBRACE);
        fixed_tokens.put(",", TokenType.COMMA);
        fixed_tokens.put(";", TokenType.SEMI);
        fixed_tokens.put(":", TokenType.COLON);
    }

    //a->b один проход в ДКА
    //лексема - любая строка, на которой ДКА начиная из начального, заканчивает в принимающем состоянием
    public List<Token> tokenize(String sourse){
        List<Token> tokens = new ArrayList<>();

        int pos=0;//absolute ind
        int line=1;
        int ind=1;
        boolean previousWasCR = false;//\r\n - one, not double

        while(pos < sourse.length()){
            int start_line = line;
            int start_ind = ind;
            char first_char = sourse.charAt(pos);
            if (first_char >= 128) {
                throw new LexerException("Недопустимый символ: '" + first_char + "'", line, start_ind);
            }

            DFAState state = start_state;
            int j = pos;
            int lastEnd = -1;

            while(j < sourse.length()){
                char c = sourse.charAt(j);
                if (c>=128) break;

                DFAState next = state.transitions.get(c);
                if (next == null || next.dfa_state_id == automaton.trap_state_id){
                    break;
                }
                state = next;
                j++;
                if(state.is_final_state){
                    lastEnd = j;
                }
            }
            if (lastEnd == -1){
                throw new LexerException("Token error before: '" + first_char + "'", start_line, start_ind);
            }

            String lexeme = sourse.substring(pos, lastEnd);
            if(lexeme.equals("0") && lastEnd < sourse.length()){//HW1 ведущий ноль
                char next = sourse.charAt(lastEnd);
                if (next >= '0' && next <= '9')
                    throw new LexerException("Ведущий ноль", start_line, start_ind);
            }

            TokenType t = classify(lexeme);
            if(t !=null){
                tokens.add(new Token(t,lexeme,start_line,start_ind));
            }

            for(int i=pos; i<lastEnd;i++){
                char c = sourse.charAt(i);

                if(c=='\r'){
                    line++;
                    ind = 1;
                    previousWasCR = true;
                }
                else if (c=='\n'){
                    if(!previousWasCR){
                        line++;
                    }
                    ind = 1;
                    previousWasCR = false;
                }
                else{
                    ind++;
                    previousWasCR = false;
                }
            }
            pos = lastEnd;
        }
        tokens.add(new Token(TokenType.EOF, "", line, ind));
        return tokens;
    }

    private TokenType classify(String lexeme){
        if (lexeme.startsWith("//")) {
            return null;
        }
        if(isWhitespace(lexeme)){
            return null;
        }

        TokenType key_word = keyw.get(lexeme);
        if(key_word != null){
            return key_word;
        }

        if (lexeme.matches("[A-Za-z_][A-Za-z0-9_]*")) {
            return TokenType.IDENT;
        }
        if (lexeme.matches("0|[1-9][0-9]*")) {
            return TokenType.INT;
        }
        TokenType fixedToken = fixed_tokens.get(lexeme);

        if (fixedToken != null) {
            return fixedToken;
        }
        throw new IllegalStateException("ДКА принял неизвестную лексему: " + lexeme);
    }

    private boolean isWhitespace(String text){
        if (text.isEmpty()) {
            return false;
        }

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c != ' ' && c != '\t' && c != '\n' && c != '\r') {
                return false;
            }
        }
        return true;
    }
}
