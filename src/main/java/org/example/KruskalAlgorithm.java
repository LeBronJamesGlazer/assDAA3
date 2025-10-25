package org.example;

import java.util.*;

public class KruskalAlgorithm {

    /**
     * Result class stores the outcome of the Kruskal's algorithm execution.
     *
     * It contains the minimum spanning tree edges, the total cost of the MST,
     * the count of operations performed during the algorithm (useful for performance analysis),
     * and the total execution time in milliseconds.
     */
    public static class Result {
        /**
         * List of edges included in the minimum spanning tree (MST).
         */
        public List<Edge> mstEdges = new ArrayList<>();

        /**
         * Total cost (sum of weights) of all edges in the MST.
         */
        public int totalCost;

        /**
         * Number of operations performed during the algorithm.
         * This can be used to analyze the algorithm's efficiency.
         */
        public int operationsCount;

        /**
         * Execution time of the algorithm in milliseconds.
         */
        public double executionTimeMs;
    }

    // The following variables store the algorithm's results and the union-find parent relationships.
    private final Result result; // Holds the algorithm's final outputs (MST, total cost, operations, and time).
    private final Map<String, String> parent = new HashMap<>(); // Represents the disjoint-set parent map used for union-find operations.

    /**
     * Constructs the KruskalAlgorithm object and executes the Kruskal’s algorithm
     * on the provided graph data. It finds the Minimum Spanning Tree (MST)
     * by sorting edges and connecting nodes without forming cycles.
     *
     * @param graph The input graph data structure.
     * @param nodes The list of all vertices in the graph.
     * @param edges The list of all edges with their weights.
     */
    public KruskalAlgorithm(GraphData graph, List<String> nodes, List<Edge> edges) {
        long start = System.nanoTime();
        // Start measuring execution time in nanoseconds to later calculate total duration.
        result = new Result();

        // Step 1: Sort all edges in non-decreasing order based on their weights.
        // Kruskal’s algorithm always picks the smallest edge first to ensure minimal total cost.
        edges.sort(Comparator.comparingInt(Edge::getWeight));
        // Record the number of edges processed to track total operations for performance analysis.
        result.operationsCount += edges.size();

        // Step 2: Initialize the disjoint-set (union-find) structure.
        // Each node is initially its own parent, representing separate components.
        for (String n : nodes) parent.put(n, n);

        // Step 3: Iterate through all edges and decide whether to include them in the MST.
        for (Edge e : edges) {
            // Skip invalid edges that reference nonexistent vertices.
            if (!graph.hasVertex(e.getFrom()) || !graph.hasVertex(e.getTo())) {
                continue;
            }
            // Skip edges that do not exist in the graph structure.
            if (!graph.hasEdgeBetween(e.getFrom(), e.getTo())) {
                continue;
            }

            // Find the root (representative) of each vertex using the union-find structure.
            String root1 = find(e.getFrom());
            String root2 = find(e.getTo());
            // If the two vertices belong to different sets, including this edge will not create a cycle.
            if (!root1.equals(root2)) {
                // Include the edge in the MST and update total cost.
                result.mstEdges.add(e);
                result.totalCost += e.getWeight();
                // Merge the two sets so future edges connecting these nodes are recognized as part of the same component.
                union(root1, root2);
            }
        }
        // Step 4: Record total execution time in milliseconds.
        result.executionTimeMs = (System.nanoTime() - start) / 1_000_000.0;
    }

    public Result getResult() {
        return result;
    }

    /**
     * Finds the root (representative) of the given node using path compression.
     * Path compression optimizes the structure by making each node point directly
     * to its root, improving efficiency for future find operations.
     *
     * @param node The vertex whose representative is to be found.
     * @return The root (representative) node of the given vertex.
     */
    private String find(String node) {
        // Increment operation counter for performance analysis.
        result.operationsCount++;
        if (!parent.get(node).equals(node)) {
            parent.put(node, find(parent.get(node)));
        }
        return parent.get(node);
    }

    /**
     * Unites two disjoint sets (represented by their root nodes).
     * This effectively connects two components into one.
     *
     * @param a The first node to unite.
     * @param b The second node to unite.
     */
    private void union(String a, String b) {
        // Increment operation counter for performance analysis.
        result.operationsCount++;
        parent.put(find(a), find(b));
    }
}