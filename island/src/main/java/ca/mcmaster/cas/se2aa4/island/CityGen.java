package ca.mcmaster.cas.se2aa4.island;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.island.Extractors.TileTagExtractor;
import ca.mcmaster.cas.se2aa4.island.Properties.PropertyAdder;
import ca.mcmaster.cas.se2aa4.island.RandomNumberGenerator.RandomNumber;
import ca.mcmaster.cas.se2aa4.pathfinder.Pathfinder;
import ca.mcmaster.cas.se2aa4.pathfinder.ShortestPath;
import ca.mcmaster.cas.se2aa4.pathfinder.adt.Edge;
import ca.mcmaster.cas.se2aa4.pathfinder.adt.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CityGen {
    private static TileTagExtractor tileTagEx = new TileTagExtractor();
    private static Random rng = RandomNumber.getRandomInstance();


//    public static List<Structs.Segment> addRoads(List<Structs.Polygon> polygons, List<Structs.Vertex> vertices, int numCities) {
//        List<Structs.Vertex> cities = CityGen.addCities(polygons, vertices, numCities);
//
//        List<Edge> edges = new ArrayList<>();
//        for (int i = 0; i < cities.size(); i++) {
//            for (int j = i + 1; j < cities.size(); j++) {
//                Structs.Vertex v1 = cities.get(i);
//                Structs.Vertex v2 = cities.get(j);
//                double distance = Math.sqrt(Math.pow(v1.getX() - v2.getX(), 2) + Math.pow(v1.getY() - v2.getY(), 2));
//                int weight = (int) Math.round(distance);
//                if (j == i + 1) { // adjacent vertices
////                    edges.add(new Edge(v1, v2, weight));
////                    edges.add(new Edge(v2, v1, weight)); // add reverse edge
////                } else { // non-adjacent vertices
////                    // do nothing
////                }
////            }
////        }
//
//                    List<Structs.Segment> roadList = new ArrayList<>();
////        Graph graph = new Graph(edges, cities);
//
//                    for (int i = 0; i < cities.size(); i++) {
//                        for (int j = i + 1; j < cities.size(); j++) {
////                List<Structs.Segment> path = ShortestPath.findShortestPath(i, j);
////                for (Structs.Segment segment : path) {
////                    Structs.Segment riverSeg = PropertyAdder.addProperty(segment, "rgb_color", "0,0,0");
////                    riverSeg = PropertyAdder.addProperty(riverSeg, "seg_tag", "road");
////                    riverSeg = PropertyAdder.addProperty(riverSeg, "thickness", "2");
////                    roadList.add(riverSeg);
//                        }
//                    }
//                }
//            }
//        }
//        return roadList;
//    }


    public static List<Structs.Vertex> addCities(List<Structs.Polygon> polygons, List<Structs.Vertex> vertices, int numCities) {
        List<List<Structs.Vertex>> citiesList = chooseCities(polygons, vertices, numCities);

        List<Structs.Vertex> cities = new ArrayList<>();
        List<Structs.Vertex> capitalList = citiesList.get(0);
        for (Structs.Vertex v : capitalList) {
            Structs.Vertex capital = PropertyAdder.addProperty(v, "rgb_color","255,0,0");
            capital = PropertyAdder.addProperty(capital, "seg_tag", "capital");
            capital = PropertyAdder.addProperty(capital, "thickness", "10");
            cities.add(capital);
        }

        List<Structs.Vertex> smallList = citiesList.get(1);
        for (Structs.Vertex v1 : smallList) {
            Structs.Vertex small = PropertyAdder.addProperty(v1, "rgb_color","0,0,0");
            small = PropertyAdder.addProperty(small, "seg_tag", "city");
            small = PropertyAdder.addProperty(small, "thickness", "5");
            cities.add(small);
        }
        return cities;
    }

    public static List<List<Structs.Vertex>> chooseCities(List<Structs.Polygon> polygons, List<Structs.Vertex> vertices, int numCities) {
        List<Structs.Polygon> roadPolygons = new ArrayList<>();
        for (Structs.Polygon p : polygons) {
            String tag = tileTagEx.extractValues(p.getPropertiesList());
            if(tag.equals("land")) {
                roadPolygons.add(p);
            }
        }
        ArrayList<Structs.Vertex> centroid = new ArrayList<>();
        for (Structs.Polygon p : roadPolygons) {
            int indexc = p.getCentroidIdx();
            Structs.Vertex v = vertices.get(indexc);
            centroid.add(v);
        }
        List<List<Structs.Vertex>> citiesList = new ArrayList<>();

        List<Structs.Vertex> capitalList = new ArrayList<>();
        Structs.Vertex capitalCity = centroid.get(rng.nextInt(centroid.size()));
        capitalList.add(capitalCity);

        List<Structs.Vertex> smallList = new ArrayList<>();
        for (int i = 0; i < numCities - 1; i++) {
            Structs.Vertex smallCity = centroid.get(rng.nextInt(centroid.size()));
            smallList.add(smallCity);
        }

        citiesList.add(capitalList);
        citiesList.add(smallList);
        return citiesList;
    }

    public static Graph createGraph(List<Structs.Vertex> vertices, List<Structs.Segment> roads) {
        List<Edge> edges = new ArrayList<>();
        for (Structs.Segment road : roads) {
            Structs.Vertex startVertex = vertices.get(road.getV1Idx());
            Structs.Vertex endVertex = vertices.get(road.getV2Idx());

            double weight = Math.sqrt(Math.pow(endVertex.getX() - startVertex.getX(), 2) + Math.pow(endVertex.getY() - startVertex.getY(), 2));
            Edge edge = new Edge(road.getV1Idx(), road.getV2Idx(), (int) weight);
            edges.add(edge);
        }

        return new Graph();
    }



}
