package org.example;

import java.util.*;

public class KruskalAlgorithm {

    public static class Result {
        public List<Edge> mstEdges = new ArrayList<>();
        public int totalCost;
        public int operationsCount;
        public double executionTimeMs;
    }

    public Result run(List<String> nodes, List<Edge> edges) {
        long start = System.nanoTime();
        Result r = new Result();

        edges.sort(Comparator.comparingInt(e -> e.weight));
        r.operationsCount += edges.size();

        Map<String, String> parent = new HashMap<>();
        for (String n : nodes) parent.put(n, n);

        java.util.function.Function<String, String> find = new java.util.function.Function<>() {
            @Override
            public String apply(String node) {
                r.operationsCount++;
                if (!parent.get(node).equals(node))
                    parent.put(node, apply(parent.get(node)));
                return parent.get(node);
            }
        };

        java.util.function.BiConsumer<String, String> union = (a, b) -> {
            r.operationsCount++;
            parent.put(find.apply(a), find.apply(b));
        };

        for (Edge e : edges) {
            String root1 = find.apply(e.from);
            String root2 = find.apply(e.to);
            if (!root1.equals(root2)) {
                r.mstEdges.add(e);
                r.totalCost += e.weight;
                union.accept(root1, root2);
            }
        }

        r.executionTimeMs = (System.nanoTime() - start) / 1_000_000.0;
        return r;
    }
}