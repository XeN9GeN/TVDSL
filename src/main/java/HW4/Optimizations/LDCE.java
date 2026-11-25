package HW4.Optimizations;

import HW4.Blocks.Block;

import java.util.HashSet;
import java.util.Set;


public class LDCE {
    private static Set<String> liveOutVars = new HashSet<>();

    public static void setLiveOutVars(Set<String> vars) {
        liveOutVars = vars;
    }

    public static Set<String> getLiveOutVars() {
        return liveOutVars;
    }

    public static boolean applyDCE(Block block, Set<String> liveOutVars) {
        boolean changed = false;
        java.util.Set<String> usedVars = new java.util.HashSet<>(liveOutVars);

        for (String inst : block.label_body) {
            if (inst.contains("=") || inst.contains("if")) {
                String[] parts = inst.split("=", 2);

                if (parts.length > 1) {
                    extractVars(parts[1].trim(), usedVars);
                }

                if(parts[0].contains("[")){
                    extractVars(parts[0].trim(), usedVars);
                }
            }
            else{
                extractVars(inst, usedVars);
            }
        }

        for (int i = block.label_body.size() - 1; i >= 0; i--) {
            String inst = block.label_body.get(i);

            if (inst.contains("call")) {
                continue;
            }

            if (inst.contains("=") && !inst.contains("if")) {
                String dest = inst.split("=", 2)[0].trim();
                if (dest.contains("[")) {
                    continue;
                }

                if (!usedVars.contains(dest)) {
                    block.label_body.remove(i);
                    changed = true;
                }
            }
        }
        return changed;
    }

    static void extractVars(String inst, java.util.Set<String> usedVars) {
        String[] tokens = inst.replaceAll("[^a-zA-Z0-9_$]", " ").split("\\s+");
        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (token.equals("L") || token.equals("goto") || token.equals("if") ||
                    token.equals("call") || token.equals("Function") || token.equals("assert") || token.equals("assume")) {
                continue;
            }

            try {
                Integer.parseInt(token);
            } catch (NumberFormatException e) {
                usedVars.add(token);
            }
        }
    }
}
