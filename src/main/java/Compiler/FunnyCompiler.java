package Compiler;

import AST.ASTPrinter;
import AST.ParserFunny;
import AST.Nodes.ProgramNode;
import DFA.DFAAutomaton;
import DFA.FunnyAutomatonFactory;
import HW3.CheckAnalyzer;
import HW3.SemanticError;
import HW4.Blocks.Block;
import HW4.Blocks.CFGBlocks;
import HW4.GreedyGraph;
import HW4.LifeInterval;
import HW4.IRCode.IRGeneration;
import HW4.Optimizations.Optimizer;
import lexer.FunnyLexer;
import lexer.Tokens.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** Runs the implemented Funny language pipeline from HW1 through HW4. */
public final class FunnyCompiler {

    public record CompilationResult(
            List<Token> tokens,
            ProgramNode ast,
            String astJson,
            List<String> ir,
            List<Block> cfg,
            List<Block> optimizedCfg,
            Map<Integer, Map<String, LifeInterval>> intervals,
            Map<String, Set<String>> conflictGraph,
            Map<String, Integer> colors) {
    }

    public CompilationResult compile(String source) {
        if (source == null) {
            throw new IllegalArgumentException("Исходный текст не должен быть null");
        }

        DFAAutomaton automaton = FunnyAutomatonFactory.getAutomaton();
        List<Token> tokens = new FunnyLexer(automaton).tokenize(source);

        ParserFunny parser = new ParserFunny(tokens);
        ProgramNode ast = parser.parseProgram();
        if (!parser.errors.isEmpty()) {
            throw new CompilationException("Синтаксические ошибки:\n"
                    + String.join("\n", parser.errors));
        }
        if (ast.functions.isEmpty()) {
            throw new CompilationException("В исходном файле не найдено ни одной функции");
        }

        CheckAnalyzer analyzer = new CheckAnalyzer();
        analyzer.analyze(ast);
        List<SemanticError> semanticErrors = analyzer.getErrors();
        if (!semanticErrors.isEmpty()) {
            String details = semanticErrors.stream()
                    .map(SemanticError::toString)
                    .collect(Collectors.joining("\n"));
            throw new CompilationException("Семантические ошибки:\n" + details);
        }

        String astJson = ASTPrinter.toJson(ast);

        IRGeneration irGeneration = new IRGeneration(ast);
        irGeneration.generateProgram(ast);
        List<String> ir = List.copyOf(irGeneration.getGeneratedCode());

        // Optimizer mutates its input blocks. Preserve a separate unoptimized CFG.
        List<Block> cfg = CFGBlocks.buildCFG(ir);
        List<Block> optimizedCfg = CFGBlocks.buildCFG(ir);

        // Return instructions are already recognized as liveness roots by LDCE.
        // A global set of return names would keep unrelated locals in other functions.
        new Optimizer().optimize(optimizedCfg, true, true, true, Set.of());

        Map<Integer, Map<String, LifeInterval>> intervals = new HashMap<>();
        List<Set<String>> liveSets = new ArrayList<>();

        for (int blockIndex = 0; blockIndex < optimizedCfg.size(); blockIndex++) {
            Block block = optimizedCfg.get(blockIndex);
            Map<String, LifeInterval> blockIntervals =
                    new LifeInterval(null, 0, 0).countLifenessTime(block, Set.of());
            intervals.put(blockIndex, Map.copyOf(blockIntervals));

            for (int instructionIndex = 0;
                 instructionIndex < block.label_body.size();
                 instructionIndex++) {
                Set<String> liveVariables = new HashSet<>();
                for (LifeInterval interval : blockIntervals.values()) {
                    if (interval.getStart() <= instructionIndex
                            && interval.getEnd() >= instructionIndex) {
                        liveVariables.add(interval.getVarName());
                    }
                }
                liveSets.add(liveVariables);
            }
        }

        GreedyGraph graph = new GreedyGraph();
        graph.buildGraph(liveSets);

        Map<String, Set<String>> graphCopy = new HashMap<>();
        graph.getGraph().forEach((name, neighbors) ->
                graphCopy.put(name, Set.copyOf(neighbors)));

        return new CompilationResult(
                List.copyOf(tokens),
                ast,
                astJson,
                ir,
                List.copyOf(cfg),
                List.copyOf(optimizedCfg),
                Map.copyOf(intervals),
                Map.copyOf(graphCopy),
                Map.copyOf(graph.greedyColoring()));
    }

    /** Formats a CFG without changing its blocks. */
    public static String formatCfg(List<Block> blocks) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            result.append("B").append(i).append(" [").append(block.label).append("]:\n");
            for (String instruction : block.label_body) {
                result.append("  ").append(instruction).append("\n");
            }
            result.append("  successors: ");
            if (block.blocks_to.isEmpty()) {
                result.append("none");
            } else {
                result.append(block.blocks_to.stream()
                        .map(successor -> "B" + blocks.indexOf(successor)
                                + " [" + successor.label + "]")
                        .collect(Collectors.joining(", ")));
            }
            result.append("\n\n");
        }
        return result.toString();
    }

    public static String formatIntervals(
            Map<Integer, Map<String, LifeInterval>> intervals) {
        StringBuilder result = new StringBuilder();
        intervals.keySet().stream().sorted().forEach(blockIndex -> {
            result.append("Block B").append(blockIndex).append(":\n");
            Map<String, LifeInterval> blockIntervals = intervals.get(blockIndex);
            blockIntervals.keySet().stream().sorted().forEach(name -> {
                LifeInterval interval = blockIntervals.get(name);
                result.append("  ").append(name)
                        .append(": [").append(interval.getStart())
                        .append(", ").append(interval.getEnd()).append("]\n");
            });
        });
        return result.toString();
    }

    public static String formatGraph(
            Map<String, Set<String>> graph, Map<String, Integer> colors) {
        StringBuilder result = new StringBuilder("Conflict graph:\n");
        graph.keySet().stream().sorted().forEach(name ->
                result.append("  ").append(name).append(" -> ")
                        .append(graph.get(name).stream().sorted()
                                .collect(Collectors.joining(", ")))
                        .append("\n"));
        result.append("\nGreedy coloring:\n");
        colors.keySet().stream().sorted().forEach(name ->
                result.append("  ").append(name).append(" -> color ")
                        .append(colors.get(name)).append("\n"));
        return result.toString();
    }

    public static final class CompilationException extends RuntimeException {
        public CompilationException(String message) {
            super(message);
        }
    }
}
