package org.example;

import java.util.*;

public class PrimAlgorithm {

    public static class Result {
        public List<Edge> mstEdges = new ArrayList<>();
        public int totalCost;
        public int operationsCount;
        public double executionTimeMs;
    }

    public Result run(List<String> nodes, List<Edge> edges) {
        long start = System.nanoTime();
        Result r = new Result();

        Map<String, List<Edge>> adj = new HashMap<>();
        for (String n : nodes) adj.put(n, new ArrayList<>());
        for (Edge e : edges) {
            adj.get(e.from).add(e);
            adj.get(e.to).add(new Edge(e.to, e.from, e.weight));
        }

        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        visited.add(nodes.get(0));
        pq.addAll(adj.get(nodes.get(0)));
        r.operationsCount += pq.size();

        while (!pq.isEmpty() && visited.size() < nodes.size()) {
            Edge e = pq.poll();
            r.operationsCount++;
            if (visited.contains(e.to)) continue;
            visited.add(e.to);
            r.mstEdges.add(e);
            r.totalCost += e.weight;

            for (Edge next : adj.get(e.to)) {
                r.operationsCount++;
                if (!visited.contains(next.to)) pq.add(next);
            }
        }

        r.executionTimeMs = (System.nanoTime() - start) / 1_000_000.0;
        return r;
    }
}