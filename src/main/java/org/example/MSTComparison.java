package org.example;

import com.google.gson.*;
import java.nio.file.*;
import java.util.*;
import java.io.*;

public class MSTComparison {

    static class GraphResult {
        int graph_id;
        String graph_type;
        Map<String, Integer> input_stats;
        PrimAlgorithm.Result prim;
        KruskalAlgorithm.Result kruskal;
    }

    static class Output {
        List<GraphResult> results;
    }

    public static void main(String[] args) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String json = Files.readString(Paths.get("ass_3_input.json"));
        var input = gson.fromJson(json, InputGraphs.class);

        String filterType = "all";
        if (args.length > 0) {
            filterType = args[0].toLowerCase();
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Select graph type to process (small, medium, large, all): ");
            filterType = scanner.nextLine().trim().toLowerCase();
        }

        switch (filterType) {
            case "small":
            case "medium":
            case "large":
            case "all":
                System.out.println("Filter mode: " + filterType);
                break;
            default:
                System.out.println("Unknown filter type: " + filterType);
                System.out.println("Valid options are: small, medium, large, all");
                return;
        }

        List<GraphResult> results = new ArrayList<>();

        for (GraphData g : input.graphs) {
            if (!filterType.equals("all") && !g.getType().trim().equalsIgnoreCase(filterType)) {
                continue;
            }
            GraphResult gr = new GraphResult();
            gr.graph_id = g.getId();
            gr.graph_type = g.getType();
            gr.input_stats = Map.of(
                    "vertices", g.getNodes().size(),
                    "edges", g.getEdges().size()
            );
            PrimAlgorithm primAlgo = new PrimAlgorithm(g, g.getNodes(), g.getEdges());
            gr.prim = primAlgo.getResult();

            KruskalAlgorithm kruskalAlgo = new KruskalAlgorithm(g, g.getNodes(), g.getEdges());
            gr.kruskal = kruskalAlgo.getResult();
            results.add(gr);
        }

        Output out = new Output();
        if (filterType.equals("all")) {
            out.results = results;
        } else {
            List<GraphResult> filteredResults = new ArrayList<>();
            for (GraphResult r : results) {
                if (r.graph_type.trim().equalsIgnoreCase(filterType)) {
                    filteredResults.add(r);
                }
            }
            out.results = filteredResults;
        }

        // --- Output JSON ---
        String outputJson = gson.toJson(out);
        System.out.println(outputJson);
        Files.writeString(Paths.get("results.json"), outputJson);
        System.out.println("\nResults written to results.json");

        // --- Summary Section ---  ADD >>>
        System.out.println("\n=== Summary by Graph Type ===");
        printSummary(out.results);
    }

    private static void printSummary(List<GraphResult> results) {
        Map<String, List<GraphResult>> grouped = new LinkedHashMap<>();

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