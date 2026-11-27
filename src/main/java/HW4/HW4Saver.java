package HW4;

import HW4.Blocks.Block;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HW4Saver {
    private static final Path OUTPUT_DIRECTORY = Path.of("output");

    public static void saveIR(List<String> irCode, String fileName) {
        Path file = OUTPUT_DIRECTORY.resolve(fileName);
        try {
            Files.createDirectories(OUTPUT_DIRECTORY);
            Files.write(file, irCode);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save IR to " + file, e);
        }
    }

    public static void saveCFG(List<Block> blocks, String fileName) {
        Path file = OUTPUT_DIRECTORY.resolve(fileName);
        StringBuilder sb = new StringBuilder();

        sb.append("=== 1) БЛОКИ ===\n");
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            sb.append("B").append(i).append(":\n");
            for (String inst : block.label_body) {
                if (inst.startsWith("L ")) {
                    String label = inst.substring(2).trim();
                    sb.append("   ").append(label).append(":\n");
                } else {
                    sb.append("   ").append(inst).append("\n");
                }
            }
            sb.append("\n");
        }

        sb.append("=== 2) ПЕРЕХОДЫ ===\n");
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            sb.append("B").append(i).append(":\n");

            if (!block.blocks_to.isEmpty()) {
                sb.append("Переход в ->: ");
                for (Block successor : block.blocks_to) {
                    int successorIndex = blocks.indexOf(successor);
                    String successorLabel = getBlockLabel(successor);
                    if (successorLabel != null) {
                        sb.append(successorLabel).append("(B").append(successorIndex).append(") ");
                    } else {
                        sb.append("B").append(successorIndex).append(" ");
                    }
                }
                sb.append("\n");
            }

            for (Block predecessor : block.blocks_into) {
                int predecessorIndex = blocks.indexOf(predecessor);
                String predecessorLabel = getBlockLabel(predecessor);
                if (predecessorLabel != null) {
                    sb.append("-> Вход из: ").append(predecessorLabel)
                            .append("(B").append(predecessorIndex).append(")\n");
                } else {
                    sb.append("-> Вход из: B").append(predecessorIndex).append("\n");
                }
            }
            sb.append("\n");
        }

        try {
            Files.createDirectories(OUTPUT_DIRECTORY);
            Files.writeString(file, sb.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save CFG to " + file, e);
        }
    }

    private static String getBlockLabel(Block block) {
        for (String instruction : block.label_body) {
            if (instruction.startsWith("L ")) {
                return instruction.substring(2).trim();
            }
        }
        return null;
    }
}
