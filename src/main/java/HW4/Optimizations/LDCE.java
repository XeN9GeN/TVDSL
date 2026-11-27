
package HW4.Optimizations;

import HW4.Blocks.Block;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LDCE {
    public static boolean applyDCE(Block block, Set<String> liveOutVars) {
        boolean changed = false;
        Set<String> live = new HashSet<>(liveOutVars);

        for (int i = block.label_body.size() - 1; i >= 0; i--) {
            String inst = block.label_body.get(i);

            if (inst.startsWith("if ")) {
                extractVars(inst, live);
                continue;
            }

            if (inst.startsWith("assert ") || inst.startsWith("assume ")) {
                extractVars(inst, live);
                continue;
            }

            if (inst.startsWith("Return")) {
                extractVars(inst, live);
                continue;
            }

            if (inst.contains("=")) {
                String[] parts = inst.split("=", 2);
                String left = parts[0].trim();
                String right = parts[1].trim();

                if (left.contains("[")) {
                    extractVars(left, live);
                    extractVars(right, live);
                    continue;
                }

                if (right.contains("call")) {
                    live.remove(left);
                    extractCallVars(right, live);
                    continue;
                }

                if (!live.contains(left)) {
                    block.label_body.remove(i);
                    changed = true;
                }
                else {
                    live.remove(left);
                    extractVars(right, live);
                }

                continue;
            }

            if (inst.startsWith("goto ") || inst.startsWith("L ") ||
                    inst.startsWith("Function ")) {
                continue;
            }

            extractVars(inst, live);
        }

        return changed;
    }


    public static void updateLiveness(String inst, Set<String> live) {
        if (inst.startsWith("if ")) {
            extractVars(inst, live);
            return;
        }

        if (inst.startsWith("assert ") || inst.startsWith("assume ") ||
                inst.startsWith("Return")) {
            extractVars(inst, live);
            return;
        }

        if (inst.contains("=")) {
            String[] parts = inst.split("=", 2);

            String left = parts[0].trim();
            String right = parts[1].trim();

            if (left.contains("[")) {
                extractVars(left, live);
                extractVars(right, live);
            }
            else {
                live.remove(left);

                if (right.contains("call")) {
                    extractCallVars(right, live);
                }
                else {
                    extractVars(right, live);
                }
            }
        }
        else if (!inst.startsWith("goto ") &&
                !inst.startsWith("L ") &&
                !inst.startsWith("Function ")) {
            extractVars(inst, live);
        }
    }


    private static void extractCallVars(String inst, Set<String> usedVars) {
        int callIndex = inst.indexOf("call");

        if (callIndex == -1) {
            return;
        }

        int commaIndex = inst.indexOf(",", callIndex);

        if (commaIndex == -1) {
            return;
        }

        extractVars(inst.substring(commaIndex + 1), usedVars);
    }


    static void extractVars(String inst, Set<String> usedVars) {
        Pattern pattern = Pattern.compile("[a-zA-Z_$][a-zA-Z0-9_$]*");
        Matcher matcher = pattern.matcher(inst);

        while (matcher.find()) {
            String token = matcher.group();
            if (token.matches("^L\\d+$")) continue;

            if (token.equals("L") || token.equals("goto") ||
                    token.equals("if") || token.equals("call") ||
                    token.equals("Function") || token.equals("assert") ||
                    token.equals("assume") || token.equals("Return") ||
                    token.equals("true") || token.equals("false") ||
                    token.equals("and") || token.equals("or") ||
                    token.equals("not")) {
                continue;
            }

            usedVars.add(token);
        }
    }
}
