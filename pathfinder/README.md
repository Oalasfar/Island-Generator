# Assignment A4: Urbanism

## Author

- Omar Al-Asfar [alasfaro@mcmaster.ca]

## Rationale

The pathfinder library consists of a Graph ADT with nodes and edges connecting said nodes.

The library will implement an algorithm to determine the shortest paths to other nodes.

## Explanations

The pathfinder library is extended by implementing the Dijkstra's algorithm.

Through the algorithm, the shortest paths connecting nodes can be defined as edges.

By applying this as a dependency of the island generator project, it becomes possible to model cities and roads through the graph structure.

Using the visualizing methods, the mesh is enriched, resulting in a network of interconnected roads among cities.

The network will follow a star network structure, with the various small cities connecting to the central hub, or capital city.

As a result, the mesh becomes enriched with urbanized elements, making it all the more realistic.