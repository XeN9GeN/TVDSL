import Compiler.FunnyCompiler;
import Compiler.FunnyCompiler.CompilationResult;
import lexer.Tokens.Token;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    private static final List<String> SHOW_ORDER = List.of(
            "tokens", "ast", "ir", "cfg", "optimized", "intervals", "graph");

    public static void main(String[] args) {
        int exitCode = run(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    private static int run(String[] args) {
        try {
            CliOptions options = parseArguments(args);
            if (options.help()) {
                printUsage();
                return 0;
            }

            if (!Files.isRegularFile(options.source())) {
                throw new IllegalArgumentException(
                        "Файл не найден или не является обычным файлом: " + options.source());
            }

            String source = Files.readString(options.source(), StandardCharsets.UTF_8);
            CompilationResult result = new FunnyCompiler().compile(source);
            Map<String, String> artifacts = createArtifacts(result);

            Path outputDirectory = Path.of("output").resolve(outputName(options.source()));
            Files.createDirectories(outputDirectory);
            for (Map.Entry<String, String> artifact : artifacts.entrySet()) {
                Files.writeString(
                        outputDirectory.resolve(artifact.getKey()),
                        artifact.getValue(),
                        StandardCharsets.UTF_8);
            }

            for (String section : SHOW_ORDER) {
                if (options.show().contains(section)) {
                    String output = artifacts.get(fileNameFor(section));
                    System.out.println("===== " + section.toUpperCase() + " =====");
                    System.out.print(output);
                    if (!output.endsWith("\n")) {
                        System.out.println();
                    }
                }
            }

            System.out.println("Результаты сохранены в: " + outputDirectory);
            return 0;
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            return 1;
        }
    }

    private static CliOptions parseArguments(String[] args) {
        Path source = null;
        Set<String> show = null;
        boolean help = false;

        for (String arg : args) {
            if (arg.equals("--help") || arg.equals("-h")) {
                help = true;
            } else if (arg.startsWith("--show=")) {
                if (show != null) {
                    throw new IllegalArgumentException("Параметр --show указан несколько раз");
                }
                show = parseShowValue(arg.substring("--show=".length()));
            } else if (arg.startsWith("-")) {
                throw new IllegalArgumentException("Неизвестный параметр: " + arg);
            } else if (source == null) {
                source = Path.of(arg);
            } else {
                throw new IllegalArgumentException("Указано несколько входных файлов");
            }
        }

        if (help) {
            return new CliOptions(source, Set.of(), true);
        }
        if (source == null) {
            throw new IllegalArgumentException("Не указан входной файл");
        }
        if (show == null) {
            show = new LinkedHashSet<>(SHOW_ORDER);
        }
        return new CliOptions(source, show, false);
    }

    private static Set<String> parseShowValue(String value) {
        if (value.equals("all")) {
            return new LinkedHashSet<>(SHOW_ORDER);
        }
        if (value.equals("none")) {
            return Set.of();
        }

        Set<String> result = new LinkedHashSet<>();
        for (String item : value.split(",")) {
            String section = item.trim();
            if (!SHOW_ORDER.contains(section)) {
                throw new IllegalArgumentException(
                        "Неизвестная секция вывода: " + section
                                + ". Допустимые значения: all, none, "
                                + String.join(", ", SHOW_ORDER));
            }
            result.add(section);
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("Список --show не должен быть пустым");
        }
        return result;
    }

    private static Map<String, String> createArtifacts(CompilationResult result) {
        Map<String, String> artifacts = new LinkedHashMap<>();

        artifacts.put("tokens.txt", result.tokens().stream()
                .map(Token::toString)
                .collect(Collectors.joining("\n", "", "\n")));
        artifacts.put("ast.json", result.astJson());
        artifacts.put("ir.txt", String.join("\n", result.ir()) + "\n");
        artifacts.put("cfg.txt", FunnyCompiler.formatCfg(result.cfg()));
        artifacts.put("optimized.txt", FunnyCompiler.formatCfg(result.optimizedCfg()));
        artifacts.put("intervals.txt",
                FunnyCompiler.formatIntervals(result.intervals()));
        artifacts.put("graph.txt",
                FunnyCompiler.formatGraph(result.conflictGraph(), result.colors()));

        return artifacts;
    }

    private static String fileNameFor(String section) {
        return switch (section) {
            case "tokens" -> "tokens.txt";
            case "ast" -> "ast.json";
            case "ir" -> "ir.txt";
            case "cfg" -> "cfg.txt";
            case "optimized" -> "optimized.txt";
            case "intervals" -> "intervals.txt";
            case "graph" -> "graph.txt";
            default -> throw new IllegalArgumentException("Неизвестная секция: " + section);
        };
    }

    private static String outputName(Path source) {
        String fileName = source.getFileName().toString();
        int extension = fileName.lastIndexOf('.');
        return extension > 0 ? fileName.substring(0, extension) : fileName;
    }

    private static void printUsage() {
        System.out.println("""
                Использование:
                  java -cp target/classes Main <файл.funny> [--show=<режим>]

                Режимы вывода:
                  all         показать все этапы (по умолчанию)
                  tokens      токены
                  ast         AST
                  ir          промежуточный код
                  cfg         исходный CFG
                  optimized   оптимизированный CFG
                  intervals   интервалы жизни переменных
                  graph       конфликтный граф и раскраска
                  none        не выводить этапы в консоль

                Несколько секций можно перечислить через запятую:
                  --show=tokens,ast,ir

                Все результаты сохраняются в output/<имя-файла>/.
                """);
    }

    private record CliOptions(Path source, Set<String> show, boolean help) {
    }
}
