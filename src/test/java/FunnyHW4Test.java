import AST.Nodes.ProgramNode;
import AST.ParserFunny;
import DFA.DFAAutomaton;
import HW3.CheckAnalyzer;
import HW4.Blocks.Block;
import HW4.Blocks.CFGBlocks;
import HW4.HW4Saver;
import HW4.IRCode.IRGeneration;
import HW4.Optimizations.LDCE;
import HW4.Optimizations.Optimizer;
import lexer.FunnyLexer;
import org.junit.jupiter.api.Test;
import HW4.LifeInterval;
import HW4.GreedyGraph;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class FunnyHW4Test {

    private final DFAAutomaton automaton = TestAutomatonProvider.getAutomaton();

    @Test
    public void testIRGeneration() {
        String source = """
                function foo(x: int) returns r: int {
                    r = x + 1;
                }
                function main(a: int[]) returns res: int {
                    x = a[0] + 5;
                    if (x > 10) {
                        y = foo(x);
                    } else {    
                        y = 0;
                    }
                    i = 0;
                    while (i < 5) {
                        i = i + 1;
                    }
                    res = y;
                }
                """;

        FunnyLexer lexer = new FunnyLexer(automaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));
        ProgramNode ast = parser.parseProgram();

        CheckAnalyzer analyzer = new CheckAnalyzer();
        analyzer.analyze(ast);
        assertTrue(analyzer.getErrors().isEmpty(), "Программа должна быть семантически корректной");

        IRGeneration irGen = new IRGeneration(ast);
        for (var func : ast.functions) {
            irGen.generateStmt(func.body);
        }

        List<String> irCode = irGen.getGeneratedCode();
        HW4Saver.saveIR(irCode, "ir_output.txt");

        assertTrue(irCode.stream().anyMatch(s -> s.matches("\\$t\\d+ = a\\[0\\]")), "Должен быть доступ к массиву");
        assertTrue(irCode.stream().anyMatch(s -> s.matches("\\$t\\d+ = \\$t\\d+ \\+ 5")), "Должна быть операция сложения");

        assertTrue(irCode.stream().anyMatch(s -> s.matches("if x > 10 goto L\\d+")), "Должен быть условный переход для if");
        assertTrue(irCode.stream().anyMatch(s -> s.startsWith("goto ")), "Должны быть безусловные переходы");

        assertTrue(irCode.stream().anyMatch(s -> s.matches("if i < 5 goto L\\d+")), "Должен быть условный переход для while");

        assertTrue(irCode.stream().anyMatch(s -> s.matches("\\$t\\d+ = call foo, x")), "Должен быть вызов функции foo");
    }

    @Test
    public void testCFGGeneration() {
        String source = """
                function main() returns res: int {
                    x = 0;
                    if (x > 10) {
                        y = 5;
                    } else {
                        y = 10;
                    }
                    res = y;
                }
                """;

        FunnyLexer lexer = new FunnyLexer(automaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));
        ProgramNode ast = parser.parseProgram();

        IRGeneration irGen = new IRGeneration(ast);
        irGen.generateProgram(ast);

        List<String> irCode = irGen.getGeneratedCode();
        List<Block> blocks = CFGBlocks.buildCFG(irCode);

        HW4Saver.saveCFG(blocks, "cfg_output.txt");

        assertFalse(blocks.isEmpty());
        assertTrue(blocks.get(0).label_body.get(0).contains("x = 0"));
        assertEquals(2, blocks.get(0).label_body.size(), "В первом блоке должно быть 2 инструкции");
        assertEquals(2, blocks.get(0).blocks_to.size(), "Условный переход должен вести в 2 блока");
    }

    @Test
    public void testComplexOptimizations() {
        String source = """
            function main() returns r: int {
                dead1 = 10 * 5 - 20 + 10 / 2;
                x = 2 + 3;
                if (x > 0) {
                    dead2 = 100 * 1 + 50 * 0;
                    y = x * 1;
                    r = y + 0;
                } else {
                    r = 0;
                }
            }
            """;

        FunnyLexer lexer = new FunnyLexer(automaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));
        ProgramNode ast = parser.parseProgram();

        CheckAnalyzer analyzer = new CheckAnalyzer();
        analyzer.analyze(ast);
        assertTrue(analyzer.getErrors().isEmpty());

        // 1. Генерируем IR
        IRGeneration irGen = new IRGeneration(ast);
        irGen.generateStmt(ast.functions.get(0).body);
        List<String> irCode = irGen.getGeneratedCode();

        System.out.println("=== IR ДО ОПТИМИЗАЦИЙ ===");
        for (String s : irCode) System.out.println(s);
        System.out.println();

        List<Block> blocks = CFGBlocks.buildCFG(irCode);

        LDCE.setLiveOutVars(java.util.Set.of("r"));
        Optimizer optimizer = new Optimizer();
        Set<String> returns = ast.functions.get(0).returns.stream().map(p->p.name).collect(Collectors.toSet());
        optimizer.optimize(blocks, true, true, true, returns);

        List<String> optimizedCode = new java.util.ArrayList<>();
        for (Block block : blocks) {
            optimizedCode.addAll(block.label_body);
        }

        System.out.println("=== IR ПОСЛЕ ОПТИМИЗАЦИЙ ===");
        for (String s : optimizedCode) System.out.println(s);

        HW4Saver.saveIR(optimizedCode, "ir_output.txt");


        assertTrue(optimizedCode.stream().anyMatch(s -> s.contains("= 5")),
                "Должна быть константа 5 (x = 2 + 3)");
        assertFalse(optimizedCode.stream().anyMatch(s -> s.contains("dead1")),
                "dead1 должна быть удалена");
        assertFalse(optimizedCode.stream().anyMatch(s -> s.contains("dead2")),
                "dead2 должна быть удалена");
        assertFalse(optimizedCode.stream().anyMatch(s -> s.contains("10 * 5")),
                "Мертвая математика должна быть удалена");
        assertFalse(optimizedCode.stream().anyMatch(s -> s.contains("100 * 1")),
                "Мертвая алгебра должна быть удалена");
        assertFalse(optimizedCode.stream().anyMatch(s -> s.contains("* 1")),
                "Умножение на 1 должно исчезнуть");
        assertFalse(optimizedCode.stream().anyMatch(s -> s.contains("+ 0")),
                "Сложение с 0 должно исчезнуть");
        assertTrue(optimizedCode.stream().anyMatch(s -> s.startsWith("if ")),
                "Условный переход должен остаться");
        assertTrue(optimizedCode.stream().anyMatch(s -> s.contains("r =")),
                "Возвращаемая переменная r должна остаться в обоих ветках");
    }

    @Test
    public void testAdvancedOptimizations() {
        String source = """
            function main(a: int, n: int) returns r: int {
                dead1 = 10 / 2 - 5;
                x = 2 + 3;
                r = x;
                i = 0;
                r = 0;
                while (i < n) {
                    dead2 = x * 0 + 100;
                    y = x * 1;
                    if (y > 0) {
                        r = r + y;
                    } else {
                        r = r - 1;
                    }
                    i = i + 1;
                }
            }
            """;

        FunnyLexer lexer = new FunnyLexer(automaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));
        ProgramNode ast = parser.parseProgram();

        CheckAnalyzer analyzer = new CheckAnalyzer();
        analyzer.analyze(ast);
        assertTrue(analyzer.getErrors().isEmpty());

        // 1. Генерируем IR
        IRGeneration irGen = new IRGeneration(ast);
        irGen.generateStmt(ast.functions.get(0).body);
        List<String> irCode = irGen.getGeneratedCode();

        System.out.println("=== СЛОЖНЫЙ IR ДО ОПТИМИЗАЦИЙ ===");
        for (String s : irCode) System.out.println(s);
        System.out.println();

        // 2. Строим CFG
        List<Block> blocks = CFGBlocks.buildCFG(irCode);

        // 3. Настраиваем оптимизации
        Set<String> returns = ast.functions.get(0).returns.stream().map(p -> p.name).collect(Collectors.toSet());
        Optimizer optimizer = new Optimizer();
        optimizer.optimize(blocks, true, true, true, returns);

        // Собираем все инструкции
        List<String> optimizedCode = new java.util.ArrayList<>();
        for (Block block : blocks) {
            optimizedCode.addAll(block.label_body);
        }

        System.out.println("=== СЛОЖНЫЙ IR ПОСЛЕ ОПТИМИЗАЦИЙ ===");
        for (String s : optimizedCode) System.out.println(s);

        // --- ПРОВЕРКИ ---

        // 1. Constant Folding: 2+3 -> 5, 10/2-5 -> 0
        assertTrue(optimizedCode.stream().anyMatch(s -> s.contains("= 5")),
                "Должна быть константа 5 (x = 2+3)");
        assertTrue(optimizedCode.stream().anyMatch(s -> s.contains("= 0")),
                "Должна быть константа 0 (dead1 = 10/2-5)");

        // 2. DCE: dead1 и dead2 удалены
        assertFalse(optimizedCode.stream().anyMatch(s -> s.contains("dead1")),
                "dead1 должна быть удалена");
        assertFalse(optimizedCode.stream().anyMatch(s -> s.contains("dead2")),
                "dead2 должна быть удалена");

        // 3. Algebraic Simplification: x * 1 -> x, x * 0 + 100 удалено
        assertFalse(optimizedCode.stream().anyMatch(s -> s.contains("* 1")),
                "Умножение на 1 должно исчезнуть");
        assertFalse(optimizedCode.stream().anyMatch(s -> s.contains("* 0")),
                "Умножение на 0 должно исчезнуть");

        // 4. Проверка, что r (возвращаемая) осталась в обеих ветках
        long rCount = optimizedCode.stream().filter(s -> s.startsWith("r =")).count();
        assertTrue(rCount >= 2, "r должна остаться в обеих ветках (найдено " + rCount + " вхождений)");

        // 5. Проверка, что while не сломался (метки и goto на месте)
        assertTrue(optimizedCode.stream().anyMatch(s -> s.startsWith("if ")),
                "Условный переход while/if должен остаться");
        assertTrue(optimizedCode.stream().anyMatch(s -> s.startsWith("goto ")),
                "Безусловный переход должен остаться");
    }

    @Test
    public void testInterferenceGraphAndColoring() {
        // Программа, в которой 'a' живет долго, а временные переменные коротко
        String source = """
            function main(a: int) returns r: int {
                b = a + 1;
                c = a + 2;
                r = b + c;
            }
            """;

        FunnyLexer lexer = new FunnyLexer(automaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));
        ProgramNode ast = parser.parseProgram();

        // 1. Генерируем IR
        IRGeneration irGen = new IRGeneration(ast);
        irGen.generateProgram(ast);
        List<String> irCode = irGen.getGeneratedCode();

        System.out.println("=== IR КОД ===");
        for (String s : irCode) System.out.println(s);
        System.out.println();

        // 2. Строим CFG (получится 1 блок)
        List<Block> blocks = CFGBlocks.buildCFG(irCode);
        Block block = blocks.get(0);

        // 3. Анализ живости (LifeInterval)
        LifeInterval lifeInterval = new LifeInterval(null, 0, 0);
        Map<String, LifeInterval> intervals = lifeInterval.countLifenessTime(block, Set.of("a","b"));

        System.out.println("=== ИНТЕРВАЛЫ ЖИЗНИ (Liveness) ===");
        intervals.forEach((var, interval) ->
                System.out.println(var + ": [" + interval.getStart() + ", " + interval.getEnd() + "]")
        );
        System.out.println();

        // 4. Строим граф конфликтов (GreedyGraph)
        GreedyGraph graph = new GreedyGraph();
        graph.buildGraph((List<Set<String>>) intervals);

        System.out.println("=== ГРАФ КОНФЛИКТОВ ===");
        graph.getGraph().forEach((v, edges) -> {
            if (!edges.isEmpty()) {
                System.out.println(v + " --- " + edges);
            }
        });
        System.out.println();

        // 5. Раскраска графа (Greedy Coloring)
        Map<String, Integer> colors = graph.greedyColoring();

        System.out.println("=== ЖАДНАЯ РАСКРАСКА (Регистры) ===");
        colors.forEach((v, color) -> System.out.println(v + " -> Регистр #" + color));
        System.out.println();

        // 6. Метрика
        int usedRegisters = colors.values().stream().max(Integer::compare).orElse(0) + 1;
        System.out.println("ИТОГО ИСПОЛЬЗОВАНО РЕГИСТРОВ: " + usedRegisters);

        // 7. Проверки
        assertFalse(intervals.isEmpty(), "Интервалы жизни не должны быть пустыми");
        assertFalse(colors.isEmpty(), "Раскраска не должна быть пустой");

        // Граф должен иметь ребра (переменные конфликтуют)
        boolean hasEdges = graph.getGraph().values().stream().anyMatch(edges -> !edges.isEmpty());
        assertTrue(hasEdges, "Граф должен содержать конфликты (ребра)");

        // Проверяем, что ни одна переменная не получила отрицательный регистр
        assertTrue(colors.values().stream().allMatch(c -> c >= 0));

        // Проверяем, что количество регистров адекватно (в данном примере должно быть больше 0 и меньше 10)
        assertTrue(usedRegisters > 0 && usedRegisters < 10, "Количество регистров должно быть разумным");
    }


}