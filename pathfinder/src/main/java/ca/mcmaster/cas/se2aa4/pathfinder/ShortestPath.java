package ca.mcmaster.cas.se2aa4.pathfinder;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.pathfinder.adt.Edge;
import ca.mcmaster.cas.se2aa4.pathfinder.adt.Graph;
import ca.mcmaster.cas.se2aa4.pathfinder.adt.Node;

import java.util.*;

public class ShortestPath implements Pathfinder {

    private final List<List<Graph.Node>> adjList;
    private final List<Structs.Vertex> centroid;
    private final int numNodes;

    public ShortestPath(List<Edge> edges, List<Structs.Vertex> centroid, int numNodes) {
        this.numNodes = numNodes;
        this.centroid = centroid;
        adjList = new ArrayList<>(numNodes);
        for (int i = 0; i < numNodes; i++) {
            adjList.add(new ArrayList<>());
        }
        for (Edge edge : edges) {
            adjList.get(edge.src()).add(new Graph.Node(edge.dest(), edge.weight()));
        }
    }

    @Override
    public List<Structs.Segment> findShortestPath(int startNode, int endNode) {
        int[] distances = new int[numNodes];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[startNode] = 0;
        PriorityQueue<Graph.Node> minHeap = new PriorityQueue<>(Comparator.comparingInt(node -> node.weight));
        minHeap.add(new Graph.Node(startNode, 0));
        boolean[] visited = new boolean[numNodes];
        int[] prev = new int[numNodes];
        Arrays.fill(prev, -1);
        while (!minHeap.isEmpty()) {
            Graph.Node currentNode = minHeap.poll();
            if (visited[currentNode.value]) {
                continue;
            }
            visited[currentNode.value] = true;
            if (currentNode.value == endNode) {
                break;
            }
            for (Graph.Node neighbor : adjList.get(currentNode.value)) {
                int newDistance = distances[currentNode.value] + neighbor.weight;
                if (newDistance < distances[neighbor.value]) {
                    distances[neighbor.value] = newDistance;
                    prev[neighbor.value] = currentNode.value;
                    minHeap.add(new Graph.Node(neighbor.value, newDistance));
                }
            }
        }
        List<Structs.Segment> shortestPath = new ArrayList<>();
        int prevIdx = -1;
        for (int node = endNode; node != -1; node = prev[node]) {
            if (prev[node] != -1) {
                int srcVertexIdx = node;
                int destVertexIdx = prev[node];
                if (prevIdx != -1) {
                    shortestPath.add(Structs.Segment.newBuilder().setV1Idx(prevIdx).setV2Idx(srcVertexIdx).build());
                }
                prevIdx = srcVertexIdx;
            }
        }

        Collections.reverse(shortestPath);
        return shortestPath;
    }

}