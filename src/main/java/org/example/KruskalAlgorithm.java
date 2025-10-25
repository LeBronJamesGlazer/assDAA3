package org.example;

import java.util.*;

public class KruskalAlgorithm {

    public static class Result {
        public List<Edge> mstEdges = new ArrayList<>();
        public int totalCost;
        public int operationsCount;
        public double executionTimeMs;
    }

    private final Result result;
    private final Map<String, String> parent = new HashMap<>();

    public KruskalAlgorithm(GraphData graph, List<String> nodes, List<Edge> edges) {
        long start = System.nanoTime();
        result = new Result();

        edges.sort(Comparator.comparingInt(Edge::getWeight));
        result.operationsCount += edges.size();

        for (String n : nodes) parent.put(n, n);

        for (Edge e : edges) {
            if (!graph.hasVertex(e.getFrom()) || !graph.hasVertex(e.getTo())) {
                continue;
            }
            if (!graph.hasEdgeBetween(e.getFrom(), e.getTo())) {
                continue;
            }

            String root1 = find(e.getFrom());
            String root2 = find(e.getTo());
            if (!root1.equals(root2)) {
                result.mstEdges.add(e);
                result.totalCost += e.getWeight();
                union(root1, root2);
            }
        }
        result.executionTimeMs = (System.nanoTime() - start) / 1_000_000.0;
    }

    public Result getResult() {
        return result;
    }

    private String find(String node) {
        result.operationsCount++;
        if (!parent.get(node).equals(node)) {
            parent.put(node, find(parent.get(node)));
        }
        return parent.get(node);
    }

    private void union(String a, String b) {
        result.operationsCount++;
        parent.put(find(a), find(b));
    }
}