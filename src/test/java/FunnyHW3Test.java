import AST.Nodes.ProgramNode;
import AST.ParserFunny;
import DFA.DFAAutomaton;
import HW3.CheckAnalyzer;
import HW3.SemanticError;
import lexer.FunnyLexer;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FunnyHW3Test {
    private final DFAAutomaton automaton = TestAutomatonProvider.getAutomaton();

    private List<SemanticError> checkErrors(String source) {
        FunnyLexer lexer = new FunnyLexer(automaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));
        ProgramNode ast = parser.parseProgram();

        CheckAnalyzer analyzer = new CheckAnalyzer();
        analyzer.analyze(ast);
        System.out.println("ТАБЛИЦА ПЕРЕМЕННЫХ: " + analyzer.lastProcessedScope.getAllVars());//Таблица символов
        System.out.println("Функции: " + analyzer.global_funcs.keySet());//Сигнатуры функций
        return analyzer.getErrors();
    }



    @Test
    public void testValidFullProgram() {
        String source = """
            function double(x: int) returns y: int {
                y = x * 2;
            }
            
            function test(a: int[]) returns r: int {
                x = 5;
                y = double(x);
                z = a[0];
                r = x + y + z;
                if (r > 0) {
                    assert r > 0;
                }
            }
            """;
        List<SemanticError> errors = checkErrors(source);
        assertTrue(errors.isEmpty(), "Валидная программа не должна содержать ошибок. Найдено: " + errors);
    }

    @Test
    public void testComplexMathAndLogic() {
        String source = """
            function calc(a: int, b: int)
                requires a > 0 and b > 0
                returns res: int
                ensures res >= 0
            {
                res = (a + b) * 2 - 10 / 2;
            }
            """;
        List<SemanticError> errors = checkErrors(source);
        assertTrue(errors.isEmpty(), "Математика и логика должны работать без ошибок");
    }



    @Test
    public void testTypeMismatch() {
        String source =
                "function test(a: int[], b: int) returns r: int {\n" +
                        "    r = a;\n" +         // Ошибка 1: нельзя записать int[] в int
                        "    r = b + a;\n" +     // Ошибка 2: оператор '+' требует int
                        "    x = a * 2;\n" +     // Ошибка 3: оператор '*' требует int
                        "}";

        FunnyLexer lexer = new FunnyLexer(automaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));
        ProgramNode ast = parser.parseProgram();

        CheckAnalyzer analyzer = new CheckAnalyzer();
        analyzer.analyze(ast);
        List<SemanticError> errors = analyzer.getErrors();

        for (SemanticError err : errors) {
            System.out.println(err);
        }

        assertEquals(3, errors.size(), "Должно быть найдено ровно 3 ошибки");
        assertTrue(errors.get(0).message.contains("Incompatible types"));
        assertTrue(errors.get(1).message.contains("requires int operands"));
        assertTrue(errors.get(2).message.contains("requires int operands"));
    }

    @Test
    public void testReadOnlyParameters() {
        String source =
                "function test(a: int, b: int[]) returns r: int {\n" +
                        "    a = 5;\n" +       // Ошибка 1
                        "    b[0] = 10;\n" +   // Ошибка 2
                        "    r = a;\n" +
                        "}";

        FunnyLexer lexer = new FunnyLexer(automaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));
        ProgramNode ast = parser.parseProgram();

        CheckAnalyzer analyzer = new CheckAnalyzer();
        analyzer.analyze(ast);
        List<SemanticError> errors = analyzer.getErrors();

        for (SemanticError err : errors) {
            System.out.println(err);
        }

        assertEquals(2, errors.size());
        assertTrue(errors.get(0).message.contains("Cant assign for read-only parameter"));
        assertTrue(errors.get(1).message.contains("Cant assign array read-only parameter"));
    }

    @Test
    public void testFunctionCallErrors() {
        String source = """
                function foo(a: int, b: int[]) returns r: int {
                    r = a;
                }
                function test() returns r: int {
                    r = foo(1);
                    r = foo(1, 2, 3);
                    r = foo(1, 2);
                    r = bar(1);
                    r = length(1);
                    r = length();
                }
                """;
        List<SemanticError> errors = checkErrors(source);
        // 1. foo(1) - мало аргументов
        // 2. foo(1,2,3) - много аргументов
        // 3. foo(1,2) - тип 2-го аргумента int вместо int[]
        // 4. bar(1) - неизвестная функция
        // 5. length(1) - ожидает int[], передан int
        // 6. length() - ожидает 1 аргумент
        assertEquals(6, errors.size(), "Должно быть найдено 6 ошибок вызова. Найдено: " + errors);
    }


    @Test
    public void testScopeAndReadOnlyErrors() {
        String source = """
            function test(x: int, arr: int[]) returns r: int {
                x = 5;
                arr[0] = 10;
                r = unknown_var;
            }
            """;
        List<SemanticError> errors = checkErrors(source);
        // 1. x = 5 (read-only параметр)
        // 2. arr[0] = 10 (read-only массив)
        // 3. unknown_var (неизвестная переменная)
        assertEquals(3, errors.size(), "Должно быть 3 ошибки области видимости/чтения");

        assertTrue(errors.stream().anyMatch(e -> e.message.contains("Cant assign for read-only parameter")));
        assertTrue(errors.stream().anyMatch(e -> e.message.contains("Cant assign array read-only parameter")));
        assertTrue(errors.stream().anyMatch(e -> e.message.contains("Unknown variable")));
    }

    @Test
    public void testDoubleDeclaration() {
        String source = """
            function test(a: int) returns a: int {
                a = 5;
            }
            """;
        List<SemanticError> errors = checkErrors(source);
        // Ошибка: параметр 'a' и возвращаемое значение 'a' имеют одинаковое имя
        assertTrue(errors.stream().anyMatch(e -> e.message.contains("Repeated declaration of name")));
    }

    @Test
    public void testTypeInferenceFailure() {
        String source = """
            function test() returns r: int {
                x = unknown_var;
                y = x + 1;
                r = 5;
            }
            """;
        List<SemanticError> errors = checkErrors(source);
        // 1. unknown_var - неизвестная переменная.
        // Из-за этого x получает тип UNKNOWN.
        // 2. y = x + 1 -> y тоже получает UNKNOWN.
        // 3. В конце функции срабатывает проверка declaredLocals:
        //    'x' невозможно вывести (Coundnt withdraw type...)
        //    'y' невозможно вывести (Coundnt withdraw type...)
        assertEquals(2, errors.size(), "Должно быть 2 ошибки (unknown + 2 un-inferred). Найдено: " + errors);

        assertTrue(errors.stream().anyMatch(e -> e.message.contains("Unknown variable")));
        assertTrue(errors.stream().anyMatch(e -> e.message.contains("Coundnt withdraw type of local var: 'x'")));
        assertTrue(errors.stream().anyMatch(e -> e.message.contains("Coundnt withdraw type of local var: 'y'")));
    }

    @Test
    public void testComparisonTypeError() {
        String source = """
            function test(a: int[]) returns r: int {
                assert a > 0;
                assert 5 == a;
                r = 5;
            }
            """;
        List<SemanticError> errors = checkErrors(source);
        // 1. a > 0 (сравнение int[] и int)
        // 2. 5 == a (сравнение int и int[])
        assertEquals(2, errors.size(), "Должно быть 2 ошибки сравнения массивов");
        assertTrue(errors.stream().allMatch(e -> e.message.contains("requires int operands")));
    }
}

