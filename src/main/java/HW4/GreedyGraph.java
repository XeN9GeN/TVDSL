package HW4;
import HW4.Blocks.Block;

import java.util.*;

public class GreedyGraph {
    private Map<String, Set<String>> graph = new HashMap<>();

    public void buildGraph(Map<String, LifeInterval> lifeIntervalMap){
        graph.clear();

        for(String var: lifeIntervalMap.keySet()){
            graph.put(var, new HashSet<>());
        }

        List<String> vars = new ArrayList<>(lifeIntervalMap.keySet());

        for(int i=0;i<vars.size();i++){
            for(int j =i+1; j<vars.size();j++){
                String v1 = vars.get(i);
                String v2 = vars.get(j);

                LifeInterval l1 = lifeIntervalMap.get(v1);
                LifeInterval l2 = lifeIntervalMap.get(v2);

                if(l1.getEnd() >= l2.getStart() && l1.getStart() <= l2.getEnd()){
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
