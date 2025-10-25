package org.example;

/**
 * Represents an edge in a graph with a source node, a destination node, and a weight.
 */
public class Edge {
    // The starting node of the edge
    private String from;
    // The ending node of the edge
    private String to;
    // The weight or cost associated with the edge
    private int weight;

    /**
     * Constructs an Edge with specified source, destination, and weight.
     *
     * @param from   the starting node of the edge
     * @param to     the ending node of the edge
     * @param weight the weight or cost of the edge
     */
    public Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    /**
     * Gets the starting node of the edge.
     *
     * @return the source node
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the starting node of the edge.
     *
     * @param from the source node to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Gets the ending node of the edge.
     *
     * @return the destination node
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the ending node of the edge.
     *
     * @param to the destination node to set
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Gets the weight of the edge.
     *
     * @return the weight or cost
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets the weight of the edge.
     *
     * @param weight the weight or cost to set
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Returns a string representation of the edge in the format "(from - to, w=weight)".
     *
     * @return a string describing the edge
     */
    @Override
    public String toString() {
        return String.format("(%s - %s, w=%d)", from, to, weight);
    }
}