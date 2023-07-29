package ca.mcmaster.cas.se2aa4.pathfinder;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.util.List;

public interface Pathfinder<T> {

    List<T> findShortestPath(int startNode, int endNode);

}
