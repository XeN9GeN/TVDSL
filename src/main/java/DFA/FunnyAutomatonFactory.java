package DFA;

import NFA.NFABuild;
import NFA.NFMiniA;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Builds and caches the DFA used by the Funny lexer. */
public final class FunnyAutomatonFactory {
    private FunnyAutomatonFactory() {}
    private static final class Holder {
        private static final DFAAutomaton INSTANCE = buildAutomaton();
    }
    public static DFAAutomaton getAutomaton() { return Holder.INSTANCE; }

    private static DFAAutomaton buildAutomaton() {
        Set<Character> alphabet = new HashSet<>();
        for (int i = 0; i < 128; i++) alphabet.add((char) i);
        NFABuild builder = new NFABuild();

        NFMiniA digitsOneToNine = builder.fromChar('1');
        for (char c = '2'; c <= '9'; c++) digitsOneToNine = builder.or(digitsOneToNine, builder.fromChar(c));
        NFMiniA digitsZeroToNine = builder.fromChar('0');
        for (char c = '1'; c <= '9'; c++) digitsZeroToNine = builder.or(digitsZeroToNine, builder.fromChar(c));
        NFMiniA integer = builder.or(builder.fromChar('0'),
                builder.concatenate(digitsOneToNine, builder.cliniStar(digitsZeroToNine)));
        markFinal(integer, "INT");

        NFMiniA whitespace = builder.or(builder.fromChar(' '), builder.fromChar('\t'));
        whitespace = builder.or(whitespace, builder.fromChar('\n'));
        whitespace = builder.or(whitespace, builder.fromChar('\r'));
        NFMiniA whitespaceToken = builder.cliniStar(whitespace);
        markFinal(whitespaceToken, "skip");

        NFMiniA first = builder.fromChar('_');
        NFMiniA any = builder.fromChar('_');
        for (char c = 'a'; c <= 'z'; c++) {
            first = builder.or(first, builder.fromChar(c));
            any = builder.or(any, builder.fromChar(c));
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            first = builder.or(first, builder.fromChar(c));
            any = builder.or(any, builder.fromChar(c));
        }
        for (char c = '0'; c <= '9'; c++) any = builder.or(any, builder.fromChar(c));
        NFMiniA identifier = builder.concatenate(first, builder.cliniStar(any));
        markFinal(identifier, "IDENT");

        NFMiniA global = builder.or(builder.or(integer, whitespaceToken), identifier);
        Map<String, String> keywords = new LinkedHashMap<>();
        keywords.put("function", "KW_FUNCTION"); keywords.put("returns", "KW_RETURNS");
        keywords.put("while", "KW_WHILE"); keywords.put("if", "KW_IF");
        keywords.put("else", "KW_ELSE"); keywords.put("assert", "KW_ASSERT");
        keywords.put("assume", "KW_ASSUME"); keywords.put("invariant", "KW_INVARIANT");
        keywords.put("length", "KW_LENGTH"); keywords.put("true", "KW_TRUE");
        keywords.put("false", "KW_FALSE"); keywords.put("requires", "KW_REQUIRES");
        keywords.put("ensures", "KW_ENSURES"); keywords.put("uses", "KW_USES");
        keywords.put("forall", "KW_FORALL"); keywords.put("exists", "KW_EXISTS");
        keywords.put("not", "KW_NOT"); keywords.put("and", "KW_AND");
        keywords.put("or", "KW_OR"); keywords.put("int", "KW_INT");
        for (Map.Entry<String, String> e : keywords.entrySet()) {
            NFMiniA token = builder.fromString(e.getKey());
            markFinal(token, e.getValue());
            token.finish_state.priority = 10;
            global = builder.or(global, token);
        }

        Map<String, String> singles = new LinkedHashMap<>();
        singles.put("(", "LPAREN"); singles.put(")", "RPAREN");
        singles.put("[", "LBRACKET"); singles.put("]", "RBRACKET");
        singles.put("{", "LBRACE"); singles.put("}", "RBRACE");
        singles.put(",", "COMMA"); singles.put(";", "SEMI"); singles.put(":", "COLON");
        singles.put("+", "OP_PLUS"); singles.put("-", "OP_MINUS");
        singles.put("*", "OP_STAR"); singles.put("/", "OP_SLASH");
        singles.put("=", "OP_EQ"); singles.put("<", "OP_LT"); singles.put(">", "OP_GT");
        for (Map.Entry<String, String> e : singles.entrySet()) {
            NFMiniA token = builder.fromString(e.getKey());
            markFinal(token, e.getValue());
            global = builder.or(global, token);
        }

        Map<String, String> doubles = new LinkedHashMap<>();
        doubles.put("==", "OP_EEQ"); doubles.put("!=", "OP_NEQ");
        doubles.put("<=", "OP_LE"); doubles.put(">=", "OP_GE");
        doubles.put("->", "OP_IMPLIES"); doubles.put("=>", "OP_FORMULA_ARROW");
        for (Map.Entry<String, String> e : doubles.entrySet()) {
            NFMiniA token = builder.fromString(e.getKey());
            markFinal(token, e.getValue());
            global = builder.or(global, token);
        }

        NFMiniA commentChar = null;
        for (int i = 0; i < 128; i++) {
            if (i == '\n') continue;
            NFMiniA one = builder.fromChar((char) i);
            commentChar = commentChar == null ? one : builder.or(commentChar, one);
        }
        NFMiniA comment = builder.concatenate(builder.fromString("//"), builder.cliniStar(commentChar));
        markFinal(comment, "skip");
        global = builder.or(global, comment);

        List<DFAState> raw = DFABuild.NFAConvertDFA(global.start_state, alphabet);
        return new DFAAutomaton(MinimizeDFA.minimizeDFA(raw, alphabet), alphabet);
    }

    private static void markFinal(NFMiniA token, String type) {
        token.finish_state.is_final_state = true;
        token.finish_state.token_final_type = type;
    }
}
