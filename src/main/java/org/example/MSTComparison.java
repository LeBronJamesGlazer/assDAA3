package org.example;

import com.google.gson.*;
import java.nio.file.*;
import java.util.*;
import java.io.*;

/**
 * The MSTComparison class compares the performance and results of
 * Prim’s and Kruskal’s algorithms on multiple graph datasets.
 * It reads graph data from a JSON file, executes both algorithms,
 * and outputs results in JSON format along with a console summary.
 */
public class MSTComparison {

    /**
     * Represents the result of MST computations for a single graph.
     * Stores metadata, input statistics, and results of Prim’s and Kruskal’s algorithms.
     */
    static class GraphResult {
        int graph_id;
        String graph_type;
        Map<String, Integer> input_stats;
        PrimAlgorithm.Result prim;
        KruskalAlgorithm.Result kruskal;
    }

    /**
     * Wrapper class used for serializing all graph results into JSON format.
     */
    static class Output {
        List<GraphResult> results;
    }

    /**
     * Entry point of the application.
     * Reads input data, filters graphs by type, runs both MST algorithms,
     * and writes results to a JSON file while printing a summary.
     */
    public static void main(String[] args) throws Exception {
        // Initialize Gson for JSON parsing and pretty printing.
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Read the input graph data from the 'ass_3_input.json' file.
        String json = Files.readString(Paths.get("ass_3_input.json"));
        var input = gson.fromJson(json, InputGraphs.class);

        // Determine which graph type(s) to process: small, medium, large, or all.
        String filterType = "all";
        if (args.length > 0) {
            filterType = args[0].toLowerCase();
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Select graph type to process (small, medium, large, all): ");
            filterType = scanner.nextLine().trim().toLowerCase();
        }

        // Validate user input for filter type.
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

        // Prepare a list to store MST results for each processed graph.
        List<GraphResult> results = new ArrayList<>();

        for (GraphData g : input.graphs) {
            if (!filterType.equals("all") && !g.getType().trim().equalsIgnoreCase(filterType)) {
                continue;
            }
            // Create a container to hold results (Prim and Kruskal) for this specific graph.
            GraphResult gr = new GraphResult();
            gr.graph_id = g.getId();
            gr.graph_type = g.getType();
            gr.input_stats = Map.of(
                    "vertices", g.getNodes().size(),
                    "edges", g.getEdges().size()
            );
            // Run both MST algorithms (Prim’s and Kruskal’s) for this graph to compare performance and results.
            PrimAlgorithm primAlgo = new PrimAlgorithm(g, g.getNodes(), g.getEdges()); // Compute MST using Prim’s algorithm.
            gr.prim = primAlgo.getResult();

            KruskalAlgorithm kruskalAlgo = new KruskalAlgorithm(g, g.getNodes(), g.getEdges()); // Compute MST using Kruskal’s algorithm.
            gr.kruskal = kruskalAlgo.getResult();
            // Store the computed MST results into the overall results list for JSON export and summary.
            results.add(gr);
        }

        // Prepare the output container to store filtered or complete results.
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
        // Convert the results into a formatted JSON string and save it to 'results.json'.
        String outputJson = gson.toJson(out);
        System.out.println(outputJson);
        Files.writeString(Paths.get("results.json"), outputJson);
        System.out.println("\nResults written to results.json");

        // --- Summary Section ---
        // Print detailed results grouped by graph type, showing cost, edges, operations, and execution time.
        System.out.println("\n=== Summary by Graph Type ===");
        printSummary(out.results);
    }

    /**
     * Prints a summary of MST results for each graph type.
     * Groups results by graph type and displays key metrics for both algorithms.
     *
     * @param results List of computed graph results to summarize.
     */
    private static void printSummary(List<GraphResult> results) {
        // Group results by graph type (small, medium, large).
        Map<String, List<GraphResult>> grouped = new LinkedHashMap<>();

        for (GraphResult r : results) {
            grouped.computeIfAbsent(r.graph_type, k -> new ArrayList<>()).add(r);
        }

        for (var entry : grouped.entrySet()) {
            String type = entry.getKey();
            // Print MST statistics for each graph within the same type group.
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

    /**
     * Represents the structure of the input JSON file containing a list of graphs.
     */
    static class InputGraphs {
        List<GraphData> graphs;
    }
}