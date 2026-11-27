import Compiler.FunnyCompiler;
import Compiler.FunnyCompiler.CompilationResult;
import lexer.Tokens.LexerException;
import lexer.Tokens.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FunnyCompilerIntegrationTest {

    @Test
    public void testProductionCompilerRunsAllStages() {
        String source = """
                function main(a: int) returns r: int {
                    x = a + 1;
                    r = x;
                }
                """;

        CompilationResult result = new FunnyCompiler().compile(source);

        assertTrue(result.tokens().stream()
                .anyMatch(token -> token.type == TokenType.KW_FUNCTION));
        assertTrue(result.astJson().contains("\"type\": \"Function\""));
        assertTrue(result.ir().stream().anyMatch(line -> line.startsWith("Function main")));
        assertFalse(result.cfg().isEmpty());
        assertFalse(result.optimizedCfg().isEmpty());
        assertFalse(result.intervals().isEmpty());
        assertFalse(result.conflictGraph().isEmpty());
        assertEquals(result.conflictGraph().keySet(), result.colors().keySet());
    }

    @Test
    public void testCompilerCanBeReusedForSeveralPrograms() {
        FunnyCompiler compiler = new FunnyCompiler();

        CompilationResult first = compiler.compile("""
                function first(a: int) returns r: int {
                    r = a;
                }
                """);

        CompilationResult second = compiler.compile("""
                function second(b: int) returns result: int {
                    result = b + 2;
                }
                """);

        assertTrue(first.astJson().contains("\"name\": \"first\""));
        assertFalse(first.astJson().contains("\"name\": \"second\""));
        assertTrue(second.astJson().contains("\"name\": \"second\""));
        assertFalse(second.astJson().contains("\"name\": \"first\""));
    }

    @Test
    public void testLexerErrorIsReportedToCaller() {
        String source = """
                function main(a: int) returns r: int {
                    r = a @ 1;
                }
                """;

        assertThrows(LexerException.class,
                () -> new FunnyCompiler().compile(source));
    }
}
