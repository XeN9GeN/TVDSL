import java.util.*;

import DFA.*;
import NFA.NFABuild;
import NFA.NFMiniA;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FunnyLexerTest {

    private static DFAAutomaton fullFunnyAutomaton;

    private static NFMiniA fromString(NFABuild builder, String s) {
        NFMiniA result = builder.fromChar(s.charAt(0));
        for (int i = 1; i < s.length(); i++) {
            result = builder.concatenate(result, builder.fromChar(s.charAt(i)));
        }
        return result;
    }

    @BeforeAll
    public static void setUpAutomaton() throws IOException {
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

        System.out.println("DFA before minimization: " + raw_dfa.size());
        System.out.println("DFA after minimization: " + minimized_dfa.size());

        fullFunnyAutomaton = new DFAAutomaton(minimized_dfa, regular_alphabet);

        fullFunnyAutomaton.saveToJsonFile("target/dfa_table.json");
        System.out.println("Финальный ДКА сохранен в target/dfa_table.json");
    }

    @Test
    public void testPositiveScenarios() {
        assertTrue(DFAInterpreter.validateString("0", fullFunnyAutomaton).success);
        assertTrue(DFAInterpreter.validateString("123456", fullFunnyAutomaton).success);

        DFAInterpreter.Result wsResult = DFAInterpreter.validateString("  \t\n\r", fullFunnyAutomaton);
        assertTrue(wsResult.success);
        assertEquals("skip", wsResult.token_type);

        DFAInterpreter.Result identResult = DFAInterpreter.validateString("my_variable_name_", fullFunnyAutomaton);
        assertTrue(identResult.success);
        assertEquals("IDENT", identResult.token_type);

        assertTrue(DFAInterpreter.validateString("==", fullFunnyAutomaton).success);

        //разделители
        assertEquals("LPAREN", DFAInterpreter.validateString("(", fullFunnyAutomaton).token_type);
        assertEquals("RPAREN", DFAInterpreter.validateString(")", fullFunnyAutomaton).token_type);
        assertEquals("LBRACKET", DFAInterpreter.validateString("[", fullFunnyAutomaton).token_type);
        assertEquals("RBRACKET", DFAInterpreter.validateString("]", fullFunnyAutomaton).token_type);
        assertEquals("LBRACE", DFAInterpreter.validateString("{", fullFunnyAutomaton).token_type);
        assertEquals("RBRACE", DFAInterpreter.validateString("}", fullFunnyAutomaton).token_type);
        assertEquals("COMMA", DFAInterpreter.validateString(",", fullFunnyAutomaton).token_type);
        assertEquals("SEMI", DFAInterpreter.validateString(";", fullFunnyAutomaton).token_type);
        assertEquals("COLON", DFAInterpreter.validateString(":", fullFunnyAutomaton).token_type);

        //операторы
        assertEquals("OP_PLUS", DFAInterpreter.validateString("+", fullFunnyAutomaton).token_type);
        assertEquals("OP_MINUS", DFAInterpreter.validateString("-", fullFunnyAutomaton).token_type);
        assertEquals("OP_STAR", DFAInterpreter.validateString("*", fullFunnyAutomaton).token_type);
        assertEquals("OP_SLASH", DFAInterpreter.validateString("/", fullFunnyAutomaton).token_type);
        assertEquals("OP_EQ", DFAInterpreter.validateString("=", fullFunnyAutomaton).token_type);
        assertEquals("OP_LT", DFAInterpreter.validateString("<", fullFunnyAutomaton).token_type);
        assertEquals("OP_GT", DFAInterpreter.validateString(">", fullFunnyAutomaton).token_type);
        assertEquals("OP_NEQ", DFAInterpreter.validateString("!=", fullFunnyAutomaton).token_type);
        assertEquals("OP_LE", DFAInterpreter.validateString("<=", fullFunnyAutomaton).token_type);
        assertEquals("OP_GE", DFAInterpreter.validateString(">=", fullFunnyAutomaton).token_type);

        DFAInterpreter.Result commentResult = DFAInterpreter.validateString("// this is a comment", fullFunnyAutomaton);
        assertTrue(commentResult.success);
        assertEquals("skip", commentResult.token_type);
    }

    @Test
    public void testNegativeScenarios() {
        assertFalse(DFAInterpreter.validateString("01", fullFunnyAutomaton).success);
        assertFalse(DFAInterpreter.validateString("00", fullFunnyAutomaton).success);
        assertFalse(DFAInterpreter.validateString("привет", fullFunnyAutomaton).success);

        assertFalse(DFAInterpreter.validateString("=>", fullFunnyAutomaton).success);
        assertFalse(DFAInterpreter.validateString("&", fullFunnyAutomaton).success);
    }

    @Test
    public void testKeywordVsIdentifierPriority() {
        // Ключевые слова должны распознаваться как ключевые слова, а не как IDENT
        assertEquals("KW_IF", DFAInterpreter.validateString("if", fullFunnyAutomaton).token_type);
        assertEquals("KW_ELSE", DFAInterpreter.validateString("else", fullFunnyAutomaton).token_type);
        assertEquals("KW_WHILE", DFAInterpreter.validateString("while", fullFunnyAutomaton).token_type);
        assertEquals("KW_FUNCTION", DFAInterpreter.validateString("function", fullFunnyAutomaton).token_type);
        assertEquals("KW_RETURNS", DFAInterpreter.validateString("returns", fullFunnyAutomaton).token_type);
        assertEquals("KW_ASSERT", DFAInterpreter.validateString("assert", fullFunnyAutomaton).token_type);
        assertEquals("KW_ASSUME", DFAInterpreter.validateString("assume", fullFunnyAutomaton).token_type);
        assertEquals("KW_INVARIANT", DFAInterpreter.validateString("invariant", fullFunnyAutomaton).token_type);
        assertEquals("KW_LENGTH", DFAInterpreter.validateString("length", fullFunnyAutomaton).token_type);
        assertEquals("KW_TRUE", DFAInterpreter.validateString("true", fullFunnyAutomaton).token_type);
        assertEquals("KW_FALSE", DFAInterpreter.validateString("false", fullFunnyAutomaton).token_type);
        assertEquals("KW_REQUIRES", DFAInterpreter.validateString("requires", fullFunnyAutomaton).token_type);
        assertEquals("KW_ENSURES", DFAInterpreter.validateString("ensures", fullFunnyAutomaton).token_type);
        assertEquals("KW_USES", DFAInterpreter.validateString("uses", fullFunnyAutomaton).token_type);
        assertEquals("KW_FORALL", DFAInterpreter.validateString("forall", fullFunnyAutomaton).token_type);
        assertEquals("KW_EXISTS", DFAInterpreter.validateString("exists", fullFunnyAutomaton).token_type);
        assertEquals("KW_NOT", DFAInterpreter.validateString("not", fullFunnyAutomaton).token_type);
        assertEquals("KW_AND", DFAInterpreter.validateString("and", fullFunnyAutomaton).token_type);
        assertEquals("KW_OR", DFAInterpreter.validateString("or", fullFunnyAutomaton).token_type);
        assertEquals("KW_INT", DFAInterpreter.validateString("int", fullFunnyAutomaton).token_type);

        // Идентификаторы, начинающиеся с ключевого слова, должны быть IDENT
        assertEquals("IDENT", DFAInterpreter.validateString("if1", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("if_", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("elsewhere", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("whileLoop", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("function1", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("returns_", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("assertion", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("assumeX", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("invariant1", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("length_", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("true1", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("false_", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("requires1", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("ensures1", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("uses1", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("forall1", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("exists1", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("not1", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("and1", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("or1", fullFunnyAutomaton).token_type);
        assertEquals("IDENT", DFAInterpreter.validateString("int1", fullFunnyAutomaton).token_type);
    }
}