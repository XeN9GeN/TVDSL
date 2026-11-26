package HW4;
import HW4.Blocks.Block;

import java.util.*;

public class GreedyGraph {
    private Map<String, Set<String>> graph = new HashMap<>();

    public void buildGraph(List<Set<String>> liveSets) {
        graph.clear();

        for (Set<String> vars : liveSets) {
            for (String var : vars) {
                graph.putIfAbsent(var, new HashSet<>());
            }

            List<String> list = new ArrayList<>(vars);

            for (int i = 0; i < list.size(); i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    String v1 = list.get(i);
                    String v2 = list.get(j);

                    graph.get(v1).add(v2);
                    graph.get(v2).add(v1);
                }
            }
        }
    }

    public Map<String, Integer> greedyColoring(){
        Map<String, Integer> colors = new HashMap<>();
        List<String> vars = new ArrayList<>(graph.keySet());

        //сортировка по убыванию степени вершины (алгоритм Уэлша-Пауэлла)
        vars.sort((a, b) -> Integer.compare(graph.get(b).size(), graph.get(a).size()));

        for(String var: vars){
            Set<Integer> usedColorsByNeighbors = new HashSet<>();

            for(String neib : graph.getOrDefault(var, Collections.emptySet())){
                if(colors.containsKey(neib)){
                    usedColorsByNeighbors.add(colors.get(neib));
                }
            }

            int color = 0;
            while(usedColorsByNeighbors.contains(color)){
                color++;
            }
            colors.put(var, color);
        }
        return colors;
    }

    public Map<String, Set<String>> getGraph() {
        return graph;
    }
}
