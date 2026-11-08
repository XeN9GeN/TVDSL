import DFA.*;
import NFA.NFABuild;
import NFA.NFMiniA;

import java.io.IOException;
import java.util.*;

public class TestAutomatonProvider {
    private static DFAAutomaton fullFunnyAutomaton;

    public static DFAAutomaton getAutomaton() {
        if (fullFunnyAutomaton == null) {
            synchronized (TestAutomatonProvider.class) {
                if (fullFunnyAutomaton == null) {
                    try {
                        fullFunnyAutomaton = setUpAutomaton();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to build DFA", e);
                    }
                }
            }
        }
        return fullFunnyAutomaton;
    }

    private static NFMiniA fromString(NFABuild builder, String s) {
        NFMiniA result = builder.fromChar(s.charAt(0));
        for (int i = 1; i < s.length(); i++) {
            result = builder.concatenate(result, builder.fromChar(s.charAt(i)));
        }
        return result;
    }

    public static DFAAutomaton setUpAutomaton() throws IOException {
        Set<Character> regular_alphabet = new HashSet<>();
        for (int i = 0; i < 128; i++) {
            regular_alphabet.add((char) i);
        }

        NFABuild builder = new NFABuild();

        //INT
        NFMiniA digits_1_9 = builder.fromChar('1');
        for (char c = '2'; c <= '9'; c++) digits_1_9 = builder.or(digits_1_9, builder.fromChar(c));
        NFMiniA digits_0_9 = builder.fromChar('0');
        for (char c = '1'; c <= '9'; c++) digits_0_9 = builder.or(digits_0_9, builder.fromChar(c));
        NFMiniA non_zero_int = builder.concatenate(digits_1_9, builder.cliniStar(digits_0_9));
        NFMiniA token_int = builder.or(builder.fromChar('0'), non_zero_int);
        token_int.finish_state.is_final_state = true;
        token_int.finish_state.token_final_type = "INT";

        //WS (skip)
        NFMiniA spaces = builder.or(builder.fromChar(' '), builder.fromChar('\t'));
        spaces = builder.or(spaces, builder.fromChar('\n'));
        spaces = builder.or(spaces, builder.fromChar('\r'));
        NFMiniA token_ws = builder.cliniStar(spaces);
        token_ws.finish_state.is_final_state = true;
        token_ws.finish_state.token_final_type = "skip";

        //IDENT
        NFMiniA letter_or_underscore = builder.fromChar('_');
        for (char c = 'a'; c <= 'z'; c++) letter_or_underscore = builder.or(letter_or_underscore, builder.fromChar(c));
        for (char c = 'A'; c <= 'Z'; c++) letter_or_underscore = builder.or(letter_or_underscore, builder.fromChar(c));

        NFMiniA any_id_char = builder.fromChar('_');
        for (char c = 'a'; c <= 'z'; c++) any_id_char = builder.or(any_id_char, builder.fromChar(c));
        for (char c = 'A'; c <= 'Z'; c++) any_id_char = builder.or(any_id_char, builder.fromChar(c));
        for (char c = '0'; c <= '9'; c++) any_id_char = builder.or(any_id_char, builder.fromChar(c));

        NFMiniA token_ident = builder.concatenate(letter_or_underscore, builder.cliniStar(any_id_char));
        token_ident.finish_state.is_final_state = true;
        token_ident.finish_state.token_final_type = "IDENT";

        NFMiniA global_nfa = builder.or(token_int, token_ws);
        global_nfa = builder.or(global_nfa, token_ident);
        Map<String, String> keywords = new LinkedHashMap<>();
        keywords.put("function", "KW_FUNCTION");
        keywords.put("returns", "KW_RETURNS");
        keywords.put("while", "KW_WHILE");
        keywords.put("if", "KW_IF");
        keywords.put("else", "KW_ELSE");
        keywords.put("assert", "KW_ASSERT");
        keywords.put("assume", "KW_ASSUME");
        keywords.put("invariant", "KW_INVARIANT");
        keywords.put("length", "KW_LENGTH");
        keywords.put("true", "KW_TRUE");
        keywords.put("false", "KW_FALSE");
        keywords.put("requires", "KW_REQUIRES");
        keywords.put("ensures", "KW_ENSURES");
        keywords.put("uses", "KW_USES");
        keywords.put("forall", "KW_FORALL");
        keywords.put("exists", "KW_EXISTS");
        keywords.put("not", "KW_NOT");
        keywords.put("and", "KW_AND");
        keywords.put("or", "KW_OR");
        keywords.put("int", "KW_INT");

        for (Map.Entry<String, String> e : keywords.entrySet()) {
            NFMiniA kw = fromString(builder, e.getKey());
            kw.finish_state.is_final_state = true;
            kw.finish_state.token_final_type = e.getValue();
            kw.finish_state.priority = 10;
            global_nfa = builder.or(global_nfa, kw);
        }

        //==
        NFMiniA op_eq = builder.concatenate(builder.fromChar('='), builder.fromChar('='));
        op_eq.finish_state.is_final_state = true;
        op_eq.finish_state.token_final_type = "OP_EEQ";
        global_nfa = builder.or(global_nfa, op_eq);

        //Одиночные разделители
        Map<String, String> delimiters = new LinkedHashMap<>();
        delimiters.put("(", "LPAREN");
        delimiters.put(")", "RPAREN");
        delimiters.put("[", "LBRACKET");
        delimiters.put("]", "RBRACKET");
        delimiters.put("{", "LBRACE");
        delimiters.put("}", "RBRACE");
        delimiters.put(",", "COMMA");
        delimiters.put(";", "SEMI");
        delimiters.put(":", "COLON");

        //Одиночные операторы
        delimiters.put("+", "OP_PLUS");
        delimiters.put("-", "OP_MINUS");
        delimiters.put("*", "OP_STAR");
        delimiters.put("/", "OP_SLASH");
        delimiters.put("=", "OP_EQ");
        delimiters.put("<", "OP_LT");
        delimiters.put(">", "OP_GT");

        for (Map.Entry<String, String> e : delimiters.entrySet()) {
            NFMiniA t = fromString(builder, e.getKey());
            t.finish_state.is_final_state = true;
            t.finish_state.token_final_type = e.getValue();
            global_nfa = builder.or(global_nfa, t);
        }

        Map<String, String> twoCharOps = new LinkedHashMap<>();
        twoCharOps.put("!=", "OP_NEQ");
        twoCharOps.put("<=", "OP_LE");
        twoCharOps.put(">=", "OP_GE");

        twoCharOps.put("->", "OP_IMPLIES");
        twoCharOps.put("=>", "OP_FORMULA_ARROW");

        for (Map.Entry<String, String> e : twoCharOps.entrySet()) {
            NFMiniA t = fromString(builder, e.getKey());
            t.finish_state.is_final_state = true;
            t.finish_state.token_final_type = e.getValue();
            global_nfa = builder.or(global_nfa, t);
        }

        NFMiniA anyExceptNewline = null;
        for (int i = 0; i < 128; i++) {
            if (i == '\n') continue;
            NFMiniA one = builder.fromChar((char) i);
            anyExceptNewline = (anyExceptNewline == null) ? one : builder.or(anyExceptNewline, one);
        }
        NFMiniA token_comment = builder.concatenate(fromString(builder, "//"), builder.cliniStar(anyExceptNewline));
        token_comment.finish_state.is_final_state = true;
        token_comment.finish_state.token_final_type = "skip";
        global_nfa = builder.or(global_nfa, token_comment);

        List<DFAState> raw_dfa = DFABuild.NFAConvertDFA(global_nfa.start_state, regular_alphabet);
        List<DFAState> minimized_dfa = MinimizeDFA.minimizeDFA(raw_dfa, regular_alphabet);

//        System.out.println("DFA before minimization: " + raw_dfa.size());
//        System.out.println("DFA after minimization: " + minimized_dfa.size());

        fullFunnyAutomaton = new DFAAutomaton(minimized_dfa, regular_alphabet);

        fullFunnyAutomaton.saveToJsonFile("target/dfa_table.json");
         //System.out.println("Финальный ДКА сохранен в target/dfa_table.json");

        return fullFunnyAutomaton;
    }
}
