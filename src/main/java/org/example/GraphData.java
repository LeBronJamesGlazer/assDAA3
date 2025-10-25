package org.example;

import java.util.List;

public class GraphData {
private int id;
private String type;
private List<String> nodes;
private List<Edge> edges;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public boolean hasVertex(String node) {
        return nodes != null && nodes.contains(node);
    }

    public boolean hasEdgeBetween(String node1, String node2) {
        if (edges == null) return false;
        for (Edge e : edges) {
            if ((e.getFrom().equals(node1) && e.getTo().equals(node2)) ||
                (e.getFrom().equals(node2) && e.getTo().equals(node1))) {
                return true;
            }
        }
        return false;
    }
}