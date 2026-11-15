import AST.Nodes.ProgramNode;
import AST.ParserFunny;
import DFA.DFAAutomaton;
import HW3.CheckAnalyzer;
import HW4.Blocks.Block;
import HW4.Blocks.CFGBlocks;
import HW4.HW4Saver;
import HW4.IRGeneration;
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
}