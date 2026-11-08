import java.util.*;

import AST.ASTPrinter;
import AST.Nodes.*;
import AST.Nodes.ExprNodes.BinaryOpNode;
import AST.Nodes.ExprNodes.IntNode;
import AST.Nodes.StmtNodes.*;
import AST.ParserFunny;
import DFA.DFAInterpreter;
import DFA.DFAAutomaton;
import HW3.CheckAnalyzer;
import lexer.FunnyLexer;
import lexer.Tokens.Token;
import lexer.Tokens.TokenType;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FunnyHW1_2Test {

    private final DFAAutomaton fullFunnyAutomaton = TestAutomatonProvider.getAutomaton();


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

        //assertFalse(DFAInterpreter.validateString("=>", fullFunnyAutomaton).success);
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




    /// Тесты под парсер лексер и дерево, HW2
    @Test
    public void testTokenizeProgram() {
        FunnyLexer lexer = new FunnyLexer(fullFunnyAutomaton);

        String source = "function f(a: int) returns r: int {\n" + "    r = (a + 1);\n" + "}";

        List<Token> tokens = lexer.tokenize(source);

        assertEquals(TokenType.KW_FUNCTION, tokens.get(0).type);
        assertEquals(TokenType.IDENT, tokens.get(1).type);
        assertEquals(TokenType.LPAREN, tokens.get(2).type);

        assertEquals(TokenType.EOF, tokens.get(tokens.size() - 1).type);
        for (Token token : tokens) {
            System.out.println(token.type);
        }
    }

    @Test
    public void testParseProgram1() {
        String source = "function add(x: int) returns r: int { r = (x + 2) * 3; }";
        FunnyLexer lexer = new FunnyLexer(fullFunnyAutomaton);
        List<Token> tokens = lexer.tokenize(source);

        ParserFunny parser = new ParserFunny(tokens);
        ProgramNode ast = parser.parseProgram();

        assertNotNull(ast);
        assertNotNull(ast.functions);
        assertEquals(1, ast.functions.size());

        FunctionNode func = ast.functions.get(0);
        assertEquals("add", func.name);

        assertEquals(1, func.params.size());
        assertEquals("x", func.params.get(0).name);
        assertEquals("int", func.params.get(0).type);

        assertEquals(1, func.returns.size());
        assertEquals("r", func.returns.get(0).name);
        assertEquals("int", func.returns.get(0).type);

        assertNotNull(func.body);
        assertEquals(1, func.body.statements.size());

        System.out.println("Программа успешно распарсена. Найдено функций: " + ast.functions.size());
    }

    @Test
    public void testParseProgram2() {
        String source = """
        function foo(a: int[])
            requires length(a) > 0
            returns r: int
            ensures r >= 0
        {
            r = 0;
            while (r < length(a))
                invariant r <= length(a)
            {
                assert a[r] >= 0;
                assume a[r] == 0;
                if (a[r] > 0) { r = r + 1; } else { r = r + 2; }
            }
        }
        """;

        // 1. Лексер
        FunnyLexer lexer = new FunnyLexer(fullFunnyAutomaton);
        List<Token> tokens = lexer.tokenize(source);

        // 2. Парсер
        ParserFunny parser = new ParserFunny(tokens);
        ProgramNode ast = parser.parseProgram();

        // 3. Базовые проверки
        assertNotNull(ast);
        assertEquals(1, ast.functions.size());

        FunctionNode func = ast.functions.get(0);
        assertEquals("foo", func.name);

        // 4. Проверка параметров и возвращаемых значений (Массивы!)
        assertEquals(1, func.params.size());
        assertEquals("a", func.params.get(0).name);
        assertEquals("int[]", func.params.get(0).type); // Проверка массива

        assertEquals(1, func.returns.size());
        assertEquals("r", func.returns.get(0).name);
        assertEquals("int", func.returns.get(0).type);

        // 5. Проверка контрактов (requires / ensures)
        assertNotNull(func.requires, "Блок requires не должен быть null");
        assertNotNull(func.ensures, "Блок ensures не должен быть null");

        // 6. Проверка тела функции
        assertNotNull(func.body);
        // Внутри тела: r = 0; и while (...)
        assertEquals(2, func.body.statements.size());

        // Проверяем, что первый оператор — это присваивание r = 0
        assertTrue(func.body.statements.get(0) instanceof AssignNode);

        // Проверяем, что второй оператор — это while
        assertTrue(func.body.statements.get(1) instanceof WhileNode);

        WhileNode whileNode = (WhileNode) func.body.statements.get(1);

        // 7. Проверка инварианта цикла
        assertNotNull(whileNode.invariant, "Инвариант цикла не должен быть null");

        // 8. Проверка тела цикла (Внутри: assert, assume, if-else)
        assertTrue(whileNode.body instanceof BlockNode);
        BlockNode whileBody = (BlockNode) whileNode.body;
        assertEquals(3, whileBody.statements.size());

        assertTrue(whileBody.statements.get(0) instanceof AssertNode);
        assertTrue(whileBody.statements.get(1) instanceof AssumeNode);
        assertTrue(whileBody.statements.get(2) instanceof IfNode);

        // Проверяем, что у if есть else ветка
        IfNode ifNode = (IfNode) whileBody.statements.get(2);
        assertNotNull(ifNode.elseBranch, "Ветка else не должна быть null");
        ASTPrinter.print(ast);
        ASTPrinter.printToJson(ast);

        System.out.println("Сложная программа успешно распарсена! Все проверки AST пройдены.");
    }

    @Test
    public void testParserErrorRecovery() {
        String badSource = "function f() returns r: int { r = ; r = 5; }";
        FunnyLexer lexer = new FunnyLexer(fullFunnyAutomaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(badSource));

        ProgramNode ast = parser.parseProgram();

        //парсер должен найти ошибку, распарсить при этом r=5
        assertFalse(parser.errors.isEmpty());
        System.out.println("Найдены ошибки: " + parser.errors);

        assertEquals(1, ast.functions.size());
        //в блоке должен остаться хотя бы один оператор, если восстановление сработало
        assertTrue(ast.functions.get(0).body.statements.size() >= 1);
    }

    @Test
    public void testMathPrecedence() {
        // Умножение должно быть глубже в дереве, чем сложение
        String source = "function math() returns r: int { r = 1 + 2 * 3; }";
        FunnyLexer lexer = new FunnyLexer(fullFunnyAutomaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));

        ProgramNode ast = parser.parseProgram();
        assertTrue(parser.errors.isEmpty(), "Не должно быть ошибок парсинга");

        BlockNode body = ast.functions.get(0).body;
        AssignNode assign = (AssignNode) body.statements.get(0);

        // Корень выражения должен быть '+' (сложение)
        assertTrue(assign.value instanceof BinaryOpNode);
        BinaryOpNode plusNode = (BinaryOpNode) assign.value;
        assertEquals("+", plusNode.op);

        // Левая часть — это число 1
        assertTrue(plusNode.l instanceof IntNode);
        assertEquals(1, ((IntNode) plusNode.l).value);

        // Правая часть — это умножение (2 * 3)
        assertTrue(plusNode.r instanceof BinaryOpNode);
        BinaryOpNode multNode = (BinaryOpNode) plusNode.r;
        assertEquals("*", multNode.op);
    }

    @Test
    public void testArrayAssignAndIfWithoutElse() {
        String source = "function f(a: int[]) returns r: int { a[0] = 5; if (a[0] > 0) { r = 1; } }";
        FunnyLexer lexer = new FunnyLexer(fullFunnyAutomaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));

        ProgramNode ast = parser.parseProgram();
        assertTrue(parser.errors.isEmpty());

        BlockNode body = ast.functions.get(0).body;
        assertEquals(2, body.statements.size());

        // Первое выражение — это обновление массива a[0] = 5
        assertTrue(body.statements.get(0) instanceof ArrayAssignNode);
        ArrayAssignNode arrAssign = (ArrayAssignNode) body.statements.get(0);
        assertEquals("a", arrAssign.arrayName);

        // Второе выражение — это if
        assertTrue(body.statements.get(1) instanceof IfNode);
        IfNode ifNode = (IfNode) body.statements.get(1);
        assertNotNull(ifNode.thenBranch);
        assertNull(ifNode.elseBranch, "Ветки else не должно быть, она не была написана");
    }

    @Test
    public void testErrorRecovery() {
        // Ошибка: после '=' сразу идет ';', нет выражения
        String source = "function f() returns r: int { r = ; r = 5; }";
        FunnyLexer lexer = new FunnyLexer(fullFunnyAutomaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));

        // Если парсер не зациклился и не упал — он дойдет сюда
        ProgramNode ast = parser.parseProgram();

        // 1. Парсер должен зафиксировать ошибку
        assertFalse(parser.errors.isEmpty(), "Должна быть обнаружена синтаксическая ошибка");
        System.out.println("Найдена ожидаемая ошибка: " + parser.errors.get(0));

        // 2. Парсер должен восстановиться и прочитать второй оператор (r = 5;)
        BlockNode body = ast.functions.get(0).body;
        assertEquals(1, body.statements.size(), "Должен остаться только корректный оператор");
        assertTrue(body.statements.get(0) instanceof AssignNode);
        assertEquals("r", ((AssignNode) body.statements.get(0)).target);
    }

    @Test
    public void testMissingClosingBrace() {
        // Забыли закрыть блок функции
        String source = "function f() returns r: int { r = 1; ";
        FunnyLexer lexer = new FunnyLexer(fullFunnyAutomaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));

        parser.parseProgram();

        // Парсер должен сообщить об ошибке
        assertFalse(parser.errors.isEmpty());

        // В ошибке должно упоминаться, что не хватает скобки
        boolean hasBraceError = parser.errors.stream().anyMatch(e -> e.contains("Need '}'"));
        assertTrue(hasBraceError, "Сообщение об ошибке должно содержать информацию о пропущенной '}'");
    }

    @Test
    public void testic(){
        String source = """
                function test() returns r: int {
                r=x;
                }
                """;

        FunnyLexer lexer = new FunnyLexer(fullFunnyAutomaton);
        List<Token> tokens = lexer.tokenize(source);

        ParserFunny parserFunny = new ParserFunny(tokens);
        ProgramNode ast = parserFunny.parseProgram();

        CheckAnalyzer checkAnalyzer = new CheckAnalyzer();
        checkAnalyzer.analyze(ast);

        assertTrue(checkAnalyzer.getErrors()!=null);
        System.out.println(checkAnalyzer.getErrors());
    }
}