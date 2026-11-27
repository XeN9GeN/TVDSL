package HW4;

import HW4.Blocks.Block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LifeInterval {
    private final String var_name;
    private int start_life_ind;
    private int end_life_ind;

    public LifeInterval(String name, int st, int end){
        this.var_name = name;
        this.start_life_ind = st;
        this.end_life_ind = end;
    }

    public String getVarName() { return var_name; }
    public int getStart() { return start_life_ind; }
    public int getEnd() { return end_life_ind; }

    public Map<String, LifeInterval> countLifenessTime(
            Block block, Set<String> liveInVars) {

        Map<String, LifeInterval> lifeIntervalMap = new HashMap<>();

        for (String var : liveInVars) {
            lifeIntervalMap.put(var,
                    new LifeInterval(var, 0, block.label_body.size()));
        }

        for (int i = 0; i < block.label_body.size(); i++) {
            String instance = block.label_body.get(i).trim();

            if (instance.isEmpty() || instance.startsWith("L ") || instance.startsWith("Function ")) {
                continue;
            }

            if (instance.contains("=") && !instance.startsWith("if ")) {
                String[] parts = instance.split("=", 2);

                String left = parts[0].trim();
                String right = parts[1].trim();

                if (left.contains("[")) {
                    for (String var : extractVars(left)) {
                        updateInterval(lifeIntervalMap, var, i);
                    }
                }
                else {
                    lifeIntervalMap.putIfAbsent(left,
                            new LifeInterval(left, i, i));
                }

                for (String var : extractVars(right)) {
                    updateInterval(lifeIntervalMap, var, i);
                }
            }
            else {
                for (String var : extractVars(instance)) {
                    updateInterval(lifeIntervalMap, var, i);
                }
            }
        }

        return lifeIntervalMap;
    }

    private static void updateInterval(Map<String, LifeInterval> map, String var, int index) {
        if (!map.containsKey(var)) {
            map.put(var, new LifeInterval(var, index, index));//впервые увидели, ind - номер строки в блоке
        } else {
            map.get(var).end_life_ind = index;//где использовалась последний раз
        }
    }

    private static Set<String> extractVars(String expr) {
        Set<String> vars = new HashSet<>();
        if (expr == null || expr.isEmpty()) {
            return vars;
        }
        String cleaned = expr.replaceAll("\"[^\"]*\"", " ");

        String[] tokens = cleaned.replaceAll("[^a-zA-Z0-9_.$]", " ").split("\\s+");
        Set<String> keywords = Set.of(
                "if", "else", "goto", "call", "return", "Return",
                "param", "Function", "returns", "assert", "assume",
                "true", "false", "null"
        );
        boolean skipNext = false;

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (skipNext) {
                skipNext = false;
                continue;
            }

            if (token.equals("call")) {
                skipNext = true;
                continue;
            }

            if (keywords.contains(token)) continue;
            if (token.matches("^L\\d+$")) continue;
            if (token.matches("^\\d+(\\.\\d+)?$")) continue;

            vars.add(token);
        }
        return vars;
    }
}
