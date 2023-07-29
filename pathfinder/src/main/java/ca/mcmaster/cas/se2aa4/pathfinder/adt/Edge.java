package ca.mcmaster.cas.se2aa4.pathfinder.adt;

public class Edge {
    int src, dest, weight;

    public Edge(int src, int dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }
    public int src() {
        return this.src;
    }
    public int dest() {
        return this.dest;
    }
    public int weight() {
        return this.weight;
    }
}