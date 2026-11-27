import AST.Nodes.ProgramNode;
import AST.ParserFunny;
import DFA.DFAAutomaton;
import HW3.CheckAnalyzer;
import HW4.Blocks.Block;
import HW4.Blocks.CFGBlocks;
import HW4.HW4Saver;
import HW4.IRCode.IRGeneration;
import HW4.Optimizations.FoldConst;
import HW4.Optimizations.LDCE;
import HW4.Optimizations.Optimizer;
import HW4.Optimizations.SimplifyAlgem;
import lexer.FunnyLexer;
import org.junit.jupiter.api.Test;
import HW4.LifeInterval;
import HW4.GreedyGraph;

import java.util.*;
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
    public void testFoldConstWithChainedAssignments() {
        Block block = new Block("B0");
        block.addLBody("a = 6");
        block.addLBody("b = a");
        block.addLBody("c = b * 7");
        block.addLBody("d = c - 2");
        block.addLBody("zero = d / 0");

        assertTrue(FoldConst.applyFoldConst(block));

        assertEquals("a = 6", block.label_body.get(0));
        assertEquals("b = 6", block.label_body.get(1));
        assertEquals("c = 42", block.label_body.get(2));
        assertEquals("d = 40", block.label_body.get(3));
        assertEquals("zero = d / 0", block.label_body.get(4));
    }

    @Test
    public void testCFGLoopBackEdge() {
        List<String> irCode = List.of(
                "i = 0",
                "L L0",
                "if i < n goto L1",
                "goto L2",
                "L L1",
                "i = i + 1",
                "goto L0",
                "L L2",
                "Return i"
        );

        List<Block> blocks = CFGBlocks.buildCFG(irCode);

        Block conditionBlock = blocks.stream()
                .filter(block -> block.label_body.contains("if i < n goto L1"))
                .findFirst().orElseThrow();
        Block startBlock = blocks.stream()
                .filter(block -> block.label.equals("L0"))
                .findFirst().orElseThrow();
        Block loopBlock = blocks.stream()
                .filter(block -> block.label.equals("L1"))
                .findFirst().orElseThrow();
        Block endBlock = blocks.stream()
                .filter(block -> block.label.equals("L2"))
                .findFirst().orElseThrow();

        assertEquals(2, conditionBlock.blocks_to.size());
        assertTrue(conditionBlock.blocks_to.contains(loopBlock));
        assertTrue(loopBlock.blocks_to.contains(startBlock));
        assertTrue(endBlock.blocks_to.isEmpty());
    }

    @Test
    public void testSimplifyAlgebraicExpressions() {
        Block block = new Block("B0");
        block.addLBody("a = value + 0");
        block.addLBody("b = 1 * a");
        block.addLBody("c = b * 0");
        block.addLBody("d = c / 1");
        block.addLBody("e = 0 + d");
        block.addLBody("f = e - 0");
        block.addLBody("g = 9 / 0");

        assertTrue(SimplifyAlgem.applySimAlg(block));

        assertEquals(List.of(
                "a = value",
                "b = a",
                "c = 0",
                "d = c",
                "e = d",
                "f = e",
                "g = 9 / 0"
        ), block.label_body);
    }

    @Test
    public void testCFGLoopArrayAndNestedIf() {
        String source = """
            function main(a: int[], n: int) returns r: int {
                i = 0;
                total = 0;

                while (i < n) {
                    x = a[i];

                    if (x > 0) {
                        total = total + x;
                    } else {
                        total = total + 1;
                    }

                    i = i + 1;
                }

                r = total;
                assert r >= 0;
            }
            """;

        FunnyLexer lexer = new FunnyLexer(automaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));
        ProgramNode ast = parser.parseProgram();

        CheckAnalyzer analyzer = new CheckAnalyzer();
        analyzer.analyze(ast);

        assertTrue(analyzer.getErrors().isEmpty(),
                "Программа должна пройти семантический анализ");

        IRGeneration irGen = new IRGeneration(ast);
        irGen.generateProgram(ast);

        List<String> irCode = irGen.getGeneratedCode();

        assertTrue(irCode.stream().anyMatch(s -> s.matches("\\$t\\d+ = a\\[i\\]")),
                "Должно генерироваться чтение массива");

        assertTrue(irCode.stream().anyMatch(s -> s.matches("if i < n goto L\\d+")),
                "Должно генерироваться условие цикла");

        assertTrue(irCode.stream().anyMatch(s -> s.matches("if x > 0 goto L\\d+")),
                "Должно генерироваться вложенное условие");

        List<Block> blocks = CFGBlocks.buildCFG(irCode);

        assertFalse(blocks.isEmpty());

        assertTrue(blocks.stream().anyMatch(block -> block.blocks_to.size() == 2),
                "Условный переход должен иметь два исходящих ребра");

        boolean hasBackEdge = false;

        for(int i = 0; i < blocks.size(); i++) {
            for(Block successor : blocks.get(i).blocks_to) {
                if(blocks.indexOf(successor) <= i) {
                    hasBackEdge = true;
                }
            }
        }

        assertTrue(hasBackEdge,
                "CFG должен содержать обратное ребро цикла");
    }

    @Test
    public void testConstantPropagationSimplificationAndDCE() {
        String source = """
                function main(a: int) returns r: int {
                    dead = a * 0;
                
                    x = 2 + 3;
                    y = x * 1;
                    z = y + 0;
                
                    r = z + a;
                }
                """;

        FunnyLexer lexer = new FunnyLexer(automaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));
        ProgramNode ast = parser.parseProgram();

        CheckAnalyzer analyzer = new CheckAnalyzer();
        analyzer.analyze(ast);

        assertTrue(analyzer.getErrors().isEmpty());

        IRGeneration irGen = new IRGeneration(ast);
        irGen.generateProgram(ast);

        List<String> irCode = irGen.getGeneratedCode();
        List<Block> blocks = CFGBlocks.buildCFG(irCode);

        Optimizer optimizer = new Optimizer();
        optimizer.optimize(blocks, true, true, true, Set.of("r"));

        List<String> optimizedCode = new java.util.ArrayList<>();

        for (Block block : blocks) {
            optimizedCode.addAll(block.label_body);
        }

        assertTrue(optimizedCode.contains("z = 5"),
                "Вычисленная константа должна сохраниться в z");

        assertFalse(optimizedCode.stream().anyMatch(s -> s.startsWith("dead =")),
                "Мёртвое присваивание должно удалиться");

        assertFalse(optimizedCode.stream().anyMatch(s -> s.contains("* 0")),
                "Умножение на 0 должно упроститься");

        assertFalse(optimizedCode.stream().anyMatch(s -> s.contains("* 1")),
                "Умножение на 1 должно упроститься");

        assertFalse(optimizedCode.stream().anyMatch(s -> s.contains("+ 0")),
                "Сложение с 0 должно упроститься");

        assertTrue(optimizedCode.stream().anyMatch(s -> s.contains("z + a")),
                "Вычисленная константа должна использоваться в итоговом выражении");

        assertTrue(optimizedCode.contains("Return r"),
                "Инструкция Return должна сохраниться");
    }

    @Test
    public void testDCEKeepsFunctionCallsAndArrayWrites() {
        String source = """
            function twice(x: int) returns r: int {
                r = x * 2;
            }

            function main(a: int[]) returns r: int {
                arr = a;
                arr[0] = 9;

                x = twice(5);
                unused = 17;

                r = arr[0] + x;
            }
            """;

        FunnyLexer lexer = new FunnyLexer(automaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));
        ProgramNode ast = parser.parseProgram();

        CheckAnalyzer analyzer = new CheckAnalyzer();
        analyzer.analyze(ast);

        assertTrue(analyzer.getErrors().isEmpty(),
                "Программа должна пройти семантический анализ");

        IRGeneration irGen = new IRGeneration(ast);
        irGen.generateProgram(ast);

        List<String> irCode = irGen.getGeneratedCode();
        List<Block> blocks = CFGBlocks.buildCFG(irCode);

        Optimizer optimizer = new Optimizer();
        optimizer.optimize(blocks, false, false, true, Set.of("r"));

        List<String> optimizedCode = new java.util.ArrayList<>();

        for(Block block : blocks) {
            optimizedCode.addAll(block.label_body);
        }

        assertTrue(optimizedCode.stream().anyMatch(s -> s.equals("arr[0] = 9")),
                "Запись в массив должна сохраниться");

        assertTrue(optimizedCode.stream().anyMatch(s -> s.matches("\\$t\\d+ = call twice, 5")),
                "Вызов функции должен сохраниться");

        assertFalse(optimizedCode.stream().anyMatch(s -> s.startsWith("unused =")),
                "Неиспользуемая переменная должна удалиться");

        assertTrue(optimizedCode.stream().anyMatch(s -> s.startsWith("r =")),
                "Возвращаемое значение должно сохраниться");

        assertTrue(optimizedCode.contains("Return r"),
                "Инструкция Return должна сохраниться");
    }

    @Test
    public void testLifeIntervalsAndGraphColoring() {
        String source = """
            function main(a: int, b: int, c: int) returns r: int {
                x = a + b;
                y = b + c;
                z = x + y;
                r = z + a;
            }
            """;

        FunnyLexer lexer = new FunnyLexer(automaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));
        ProgramNode ast = parser.parseProgram();

        CheckAnalyzer analyzer = new CheckAnalyzer();
        analyzer.analyze(ast);

        assertTrue(analyzer.getErrors().isEmpty());

        IRGeneration irGen = new IRGeneration(ast);
        irGen.generateProgram(ast);

        List<String> irCode = irGen.getGeneratedCode();
        List<Block> blocks = CFGBlocks.buildCFG(irCode);

        assertEquals(1, blocks.size(),
                "Программа должна содержать один базовый блок");

        Block block = blocks.get(0);

        LifeInterval lifeInterval = new LifeInterval(null, 0, 0);
        Map<String, LifeInterval> intervals =
                lifeInterval.countLifenessTime(block, Set.of());

        assertFalse(intervals.isEmpty(),
                "Интервалы жизни не должны быть пустыми");

        assertTrue(intervals.containsKey("a"));
        assertTrue(intervals.containsKey("b"));
        assertTrue(intervals.containsKey("c"));

        List<Set<String>> liveSets = new java.util.ArrayList<>();

        for(int i = 0; i < block.label_body.size(); i++) {
            Set<String> liveVars = new java.util.HashSet<>();

            for(Map.Entry<String, LifeInterval> entry : intervals.entrySet()) {
                LifeInterval interval = entry.getValue();

                if(interval.getStart() <= i && interval.getEnd() >= i) {
                    liveVars.add(entry.getKey());
                }
            }

            liveSets.add(liveVars);
        }

        GreedyGraph graph = new GreedyGraph();
        graph.buildGraph(liveSets);

        assertFalse(graph.getGraph().isEmpty(),
                "Граф конфликтов не должен быть пустым");

        assertTrue(graph.getGraph().get("a").contains("b"),
                "Переменные a и b должны конфликтовать");

        assertTrue(graph.getGraph().get("b").contains("c"),
                "Переменные b и c должны конфликтовать");

        Map<String, Integer> colors = graph.greedyColoring();

        assertFalse(colors.isEmpty(),
                "Раскраска графа не должна быть пустой");

        assertNotEquals(colors.get("a"), colors.get("b"),
                "Конфликтующие переменные не должны иметь одинаковые цвета");

        assertNotEquals(colors.get("b"), colors.get("c"),
                "Конфликтующие переменные не должны иметь одинаковые цвета");

        for(Map.Entry<String, Set<String>> entry : graph.getGraph().entrySet()) {
            for(String neighbor : entry.getValue()) {
                assertNotEquals(colors.get(entry.getKey()), colors.get(neighbor),
                        "Соседние вершины не должны иметь одинаковый цвет");
            }
        }

        assertTrue(colors.values().stream().allMatch(color -> color >= 0),
                "Номера регистров не должны быть отрицательными");

        int usedRegisters = colors.values().stream()
                .max(Integer::compare)
                .orElse(-1) + 1;

        assertTrue(usedRegisters >= 3,
                "Треугольник конфликтующих переменных требует минимум 3 цвета");
    }

    @Test
    public void testLivenessAcrossBranchesAndDCE() {
        String source = """
            function main(a: int) returns r: int {
                x = a + 10;

                if (a > 0) {
                    y = x + 1;
                } else {
                    y = x - 1;
                }

                r = y + x;
            }
            """;

        FunnyLexer lexer = new FunnyLexer(automaton);
        ParserFunny parser = new ParserFunny(lexer.tokenize(source));
        ProgramNode ast = parser.parseProgram();

        CheckAnalyzer analyzer = new CheckAnalyzer();
        analyzer.analyze(ast);

        assertTrue(analyzer.getErrors().isEmpty(),
                "Программа должна пройти семантический анализ");

        IRGeneration irGen = new IRGeneration(ast);
        irGen.generateProgram(ast);

        List<String> irCode = irGen.getGeneratedCode();
        List<Block> blocks = CFGBlocks.buildCFG(irCode);

        assertTrue(blocks.stream().anyMatch(block -> block.blocks_to.size() == 2),
                "CFG должен содержать условное ветвление");

        Optimizer optimizer = new Optimizer();
        optimizer.optimize(blocks, false, false, true, Set.of("r"));

        List<String> optimizedCode = new java.util.ArrayList<>();

        for(Block block : blocks) {
            optimizedCode.addAll(block.label_body);
        }

        assertTrue(optimizedCode.stream().anyMatch(s -> s.startsWith("x = ")),
                "Присваивание x должно сохраниться, так как x используется после ветвления");

        long yAssignments = optimizedCode.stream()
                .filter(s -> s.startsWith("y = "))
                .count();

        assertEquals(2, yAssignments,
                "Присваивания y из обеих веток должны сохраниться");

        assertTrue(optimizedCode.stream().anyMatch(s -> s.startsWith("r =")),
                "Присваивание возвращаемой переменной должно сохраниться");

        assertTrue(optimizedCode.contains("Return r"),
                "Инструкция Return должна сохраниться");
    }

    @Test
    public void testLifeIntervalIgnoresFunctionHeader() {
        Block block = new Block("B0");

        block.addLBody("Function main(a) returns r:");
        block.addLBody("x = a + 1");
        block.addLBody("r = x");
        block.addLBody("Return r");

        LifeInterval lifeInterval = new LifeInterval(null, 0, 0);
        Map<String, LifeInterval> intervals =
                lifeInterval.countLifenessTime(block, Set.of());

        assertTrue(intervals.containsKey("a"));
        assertTrue(intervals.containsKey("x"));
        assertTrue(intervals.containsKey("r"));

        assertFalse(intervals.containsKey("main"));
        assertFalse(intervals.containsKey("returns"));
        assertFalse(intervals.containsKey("Return"));
    }
}