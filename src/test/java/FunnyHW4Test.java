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

import java.util.List;

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

        assertTrue(irCode.stream().anyMatch(s -> s.matches("t\\d+ = a\\[0\\]")), "Должен быть доступ к массиву");
        assertTrue(irCode.stream().anyMatch(s -> s.matches("t\\d+ = t\\d+ \\+ 5")), "Должна быть операция сложения");
        assertTrue(irCode.stream().anyMatch(s -> s.matches("if x > 10 goto L\\d+")), "Должен быть условный переход для if");
        assertTrue(irCode.stream().anyMatch(s -> s.startsWith("goto ")), "Должны быть безусловные переходы");
        assertTrue(irCode.stream().anyMatch(s -> s.matches("if i < 5 goto L\\d+")), "Должен быть условный переход для while");
        assertTrue(irCode.stream().anyMatch(s -> s.matches("t\\d+ = call foo, x")), "Должен быть вызов функции foo");
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
        irGen.generateStmt(ast.functions.get(0).body);

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
        optimizer.optimize(blocks, true, true, true);

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
}