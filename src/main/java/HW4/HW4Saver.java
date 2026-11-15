package HW4;

import HW4.Blocks.Block;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HW4Saver {
    private static final String BASE_PATH = "C:\\Users\\user\\IdeaProjects\\TVDSL\\Funny lang";

    public static void saveIR(List<String> irCode, String fileName) {
        Path dir = Path.of(BASE_PATH);
        Path file = dir.resolve(fileName);
        try {
            Files.createDirectories(dir);
            Files.write(file, irCode);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save IR to " + file, e);
        }
    }

    public static void saveCFG(List<Block> blocks, String fileName) {
        Path dir = Path.of(BASE_PATH);
        Path file = dir.resolve(fileName);
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
                for (Block succ : block.blocks_to) {
                    int succIdx = blocks.indexOf(succ);
                    String succLabel = getBlockLabel(succ);
                    if (succLabel != null) {
                        sb.append(succLabel).append("(B").append(succIdx).append(") ");
                    } else {
                        sb.append("B").append(succIdx).append(" ");
                    }
                }
                sb.append("\n");
            }

            for (Block pred : block.blocks_into) {
                int predIdx = blocks.indexOf(pred);
                String predLabel = getBlockLabel(pred);
                if (predLabel != null) {
                    sb.append("-> Вход из: ").append(predLabel).append("(B").append(predIdx).append(")\n");
                } else {
                    sb.append("-> Вход из: B").append(predIdx).append("\n");
                }
            }
            sb.append("\n");
        }

        try {
            Files.createDirectories(dir);
            Files.writeString(file, sb.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save CFG to " + file, e);
        }
    }

    private static String getBlockLabel(Block block) {
        for (String inst : block.label_body) {
            if (inst.startsWith("L ")) {
                return inst.substring(2).trim();
            }
        }
        return null;
    }
}