package org.example;

import java.util.*;

/**
 * Implementation of Prim’s Algorithm for finding the Minimum Spanning Tree (MST).
 * The algorithm starts from a single vertex and repeatedly adds the smallest edge
 * that connects a new vertex to the growing MST.
 * It uses a priority queue for efficient selection of minimum-weight edges.
 */
public class PrimAlgorithm {

    /**
     * Helper class to store the results of Prim’s algorithm:
     * - mstEdges: list of edges in the resulting MST.
     * - totalCost: sum of weights of all MST edges.
     * - operationsCount: number of significant algorithmic steps performed.
     * - executionTimeMs: total time taken to execute the algorithm.
     */
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

    /**
     * Constructs a PrimAlgorithm object and executes Prim’s algorithm.
     *
     * @param graph The input graph data.
     * @param nodes List of vertices in the graph.
     * @param edges List of edges with weights.
     */
    public PrimAlgorithm(GraphData graph, List<String> nodes, List<Edge> edges) {
        long start = System.nanoTime();

        // Build adjacency list
        adj = new HashMap<>();
        for (String node : nodes) {
            adj.put(node, new ArrayList<>());
        }

        // Step 2: Populate adjacency list with all valid edges.
        // Each edge is added in both directions since the graph is undirected.
        for (Edge e : edges) {
            if (!graph.hasVertex(e.getFrom()) || !graph.hasVertex(e.getTo())) continue;
            if (!graph.hasEdgeBetween(e.getFrom(), e.getTo())) continue;
            adj.get(e.getFrom()).add(e);
            adj.get(e.getTo()).add(new Edge(e.getTo(), e.getFrom(), e.getWeight()));
        }

        // Step 3: Initialize helper structures:
        // 'marked' tracks visited vertices, 'pq' stores edges by smallest weight,
        // and 'result' keeps MST details.
        marked = new HashSet<>();
        pq = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
        result = new Result();

        // Step 4: Start Prim’s algorithm from the first node in the list.
        prim(nodes.get(0));

        // Step 5: Record total execution time of the algorithm in milliseconds.
        result.executionTimeMs = (System.nanoTime() - start) / 1_000_000.0;
    }

    public Result getResult() {
        return result;
    }

    /**
     * Core implementation of Prim’s algorithm.
     * It expands the MST by repeatedly selecting the smallest available edge
     * connecting a visited vertex to an unvisited vertex.
     *
     * @param start The starting vertex of the MST.
     */
    private void prim(String start) {
        // Add all edges connected to the starting vertex into the priority queue.
        scan(start);

        // Continue until all vertices are visited or no more edges are available.
        while (!pq.isEmpty() && marked.size() < adj.size()) {
            // Retrieve the smallest edge from the priority queue.
            Edge e = pq.poll();
            result.operationsCount++;
            // Skip edges leading to already-visited vertices to avoid cycles.
            if (marked.contains(e.getTo())) continue;
            // Add this edge to the MST and update the total cost.
            result.mstEdges.add(e);
            result.totalCost += e.getWeight();
            // Explore the new vertex and add its outgoing edges to the priority queue.
            scan(e.getTo());
        }
    }

    /**
     * Marks a vertex as visited and adds all its adjacent edges to the priority queue
     * if the target vertex has not yet been visited.
     *
     * @param vertex The vertex to scan.
     */
    private void scan(String vertex) {
        // Mark the current vertex as visited.
        marked.add(vertex);
        // Iterate through all edges from this vertex.
        for (Edge e : adj.get(vertex)) {
            result.operationsCount++;
            // Add edges leading to unvisited vertices to the priority queue for consideration.
            if (!marked.contains(e.getTo())) {
                pq.add(e);
            }
        }
    }
}