package org.example;

import com.google.gson.*;
import java.nio.file.*;
import java.util.*;
import java.io.*;

public class MSTComparison {

    static class GraphResult {
        int graph_id;
        String graph_type; // ADD >>> store type ("small", "medium", "large")
        Map<String, Integer> input_stats;
        PrimAlgorithm.Result prim;
        KruskalAlgorithm.Result kruskal;
    }

    static class Output {
        List<GraphResult> results;
    }

    public static void main(String[] args) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // --- Read Input ---
        String json = Files.readString(Paths.get("ass_3_input.json"));
        var input = gson.fromJson(json, InputGraphs.class);

        PrimAlgorithm primAlgo = new PrimAlgorithm();
        KruskalAlgorithm kruskalAlgo = new KruskalAlgorithm();
        List<GraphResult> results = new ArrayList<>();

        for (GraphData g : input.graphs) {
            GraphResult gr = new GraphResult();
            gr.graph_id = g.id;
            gr.graph_type = g.type; // ADD >>>
            gr.input_stats = Map.of(
                    "vertices", g.nodes.size(),
                    "edges", g.edges.size()
            );
            gr.prim = primAlgo.run(g.nodes, g.edges);
            gr.kruskal = kruskalAlgo.run(g.nodes, g.edges);
            results.add(gr);
        }

        Output out = new Output();
        out.results = results;

        // --- Output JSON ---
        String outputJson = gson.toJson(out);
        System.out.println(outputJson);
        Files.writeString(Paths.get("results.json"), outputJson);
        System.out.println("\nResults written to results.json");

        // --- Summary Section ---  ADD >>>
        System.out.println("\n=== Summary by Graph Type ===");
        printSummary(results);
    }

    private static void printSummary(List<GraphResult> results) {
        Map<String, List<GraphResult>> grouped = new LinkedHashMap<>();

        // group by type
        for (GraphResult r : results) {
            grouped.computeIfAbsent(r.graph_type, k -> new ArrayList<>()).add(r);
        }

        for (var entry : grouped.entrySet()) {
            String type = entry.getKey();
            System.out.println("\n" + type.substring(0, 1).toUpperCase() + type.substring(1) + " Graphs:");
            for (GraphResult r : entry.getValue()) {
                System.out.println("Graph ID " + r.graph_id);
                System.out.println("Kruskal → Total Cost: " + r.kruskal.totalCost +
                        ", Edges: " + r.kruskal.mstEdges.size() +
                        ", Ops: " + r.kruskal.operationsCount +
                        ", Time: " + r.kruskal.executionTimeMs + " ms");

                System.out.println("Prim    → Total Cost: " + r.prim.totalCost +
                        ", Edges: " + r.prim.mstEdges.size() +
                        ", Ops: " + r.prim.operationsCount +
                        ", Time: " + r.prim.executionTimeMs + " ms");
                System.out.println();
            }
        }
    }

    static class InputGraphs {
        List<GraphData> graphs;
    }
}