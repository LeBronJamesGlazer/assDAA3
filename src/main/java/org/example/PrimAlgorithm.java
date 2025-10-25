package org.example;

import java.util.*;

public class PrimAlgorithm {

    public static class Result {
        public List<Edge> mstEdges = new ArrayList<>();
        public int totalCost;
        public int operationsCount;
        public double executionTimeMs;
    }

    private Map<String, List<Edge>> adj;
    private Set<String> marked;
    private PriorityQueue<Edge> pq;
    private Result result;

    public PrimAlgorithm(GraphData graph, List<String> nodes, List<Edge> edges) {
        long start = System.nanoTime();

        // Build adjacency list
        adj = new HashMap<>();
        for (String node : nodes) {
            adj.put(node, new ArrayList<>());
        }
        for (Edge e : edges) {
            if (!graph.hasVertex(e.getFrom()) || !graph.hasVertex(e.getTo())) continue;
            if (!graph.hasEdgeBetween(e.getFrom(), e.getTo())) continue;
            adj.get(e.getFrom()).add(e);
            adj.get(e.getTo()).add(new Edge(e.getTo(), e.getFrom(), e.getWeight()));
        }

        marked = new HashSet<>();
        pq = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
        result = new Result();

        prim(nodes.get(0));

        result.executionTimeMs = (System.nanoTime() - start) / 1_000_000.0;
    }

    public Result getResult() {
        return result;
    }

    private void prim(String start) {
        scan(start);

        while (!pq.isEmpty() && marked.size() < adj.size()) {
            Edge e = pq.poll();
            result.operationsCount++;
            if (marked.contains(e.getTo())) continue;
            result.mstEdges.add(e);
            result.totalCost += e.getWeight();
            scan(e.getTo());
        }
    }

    private void scan(String vertex) {
        marked.add(vertex);
        for (Edge e : adj.get(vertex)) {
            result.operationsCount++;
            if (!marked.contains(e.getTo())) {
                pq.add(e);
            }
        }
    }
}