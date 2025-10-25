package org.example;

import java.util.List;

/**
 * Represents a graph data structure with an identifier, type, nodes, and edges.
 * Provides methods to access and modify the graph's components and check for vertices and edges.
 */
public class GraphData {
    /** Unique identifier for the graph */
    private int id;
    /** Type or category of the graph */
    private String type;
    /** List of node identifiers in the graph */
    private List<String> nodes;
    /** List of edges connecting nodes in the graph */
    private List<Edge> edges;

    /**
     * Returns the type of the graph.
     * @return the type as a String
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the graph.
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the unique identifier of the graph.
     * @return the graph id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the graph.
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the list of nodes in the graph.
     * @return list of node identifiers
     */
    public List<String> getNodes() {
        return nodes;
    }

    /**
     * Sets the list of nodes in the graph.
     * @param nodes the list of node identifiers to set
     */
    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    /**
     * Returns the list of edges in the graph.
     * @return list of edges
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * Sets the list of edges in the graph.
     * @param edges the list of edges to set
     */
    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    /**
     * Checks if the graph contains a vertex with the specified node identifier.
     *
     * @param node the node identifier to check for
     * @return true if the node exists in the graph, false otherwise
     */
    public boolean hasVertex(String node) {
        return nodes != null && nodes.contains(node);
    }

    /**
     * Checks if there is an edge between two given nodes in the graph.
     * This method considers edges undirected, so it returns true if an edge
     * exists from node1 to node2 or from node2 to node1.
     *
     * @param node1 the first node
     * @param node2 the second node
     * @return true if an edge exists between node1 and node2, false otherwise
     */
    public boolean hasEdgeBetween(String node1, String node2) {
        // Return false immediately if edges list is null (no edges exist)
        if (edges == null) return false;

        // Iterate through all edges to check for a connection between node1 and node2
        for (Edge e : edges) {
            // Check if the edge connects node1 to node2 or node2 to node1 (undirected)
            if ((e.getFrom().equals(node1) && e.getTo().equals(node2)) ||
                (e.getFrom().equals(node2) && e.getTo().equals(node1))) {
                // Edge found, return true
                return true;
            }
        }
        // No edge found between the two nodes, return false
        return false;
    }
}