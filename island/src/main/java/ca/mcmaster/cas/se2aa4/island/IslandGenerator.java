package ca.mcmaster.cas.se2aa4.island;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.island.Altitude.AltitudeProfile;
import ca.mcmaster.cas.se2aa4.island.Biomes.BiomeProfile;
import ca.mcmaster.cas.se2aa4.island.Configuration.Configuration;
import ca.mcmaster.cas.se2aa4.island.Extractors.Extractor;
import ca.mcmaster.cas.se2aa4.island.Extractors.TileTagExtractor;
import ca.mcmaster.cas.se2aa4.island.Humidity.TileHumidifier;
import ca.mcmaster.cas.se2aa4.island.LakeGen.AquiferAdder;
import ca.mcmaster.cas.se2aa4.island.LakeGen.LakeAdder;
import ca.mcmaster.cas.se2aa4.island.Properties.PropertyAdder;
import ca.mcmaster.cas.se2aa4.island.RandomNumberGenerator.RandomNumber;
import ca.mcmaster.cas.se2aa4.island.RiverGen.AddRivers;
import ca.mcmaster.cas.se2aa4.island.Shape.Shape;
import ca.mcmaster.cas.se2aa4.island.SoilAbsorption.SoilProfile;
import ca.mcmaster.cas.se2aa4.pathfinder.Pathfinder;
import ca.mcmaster.cas.se2aa4.pathfinder.ShortestPath;
import ca.mcmaster.cas.se2aa4.pathfinder.adt.Edge;
import ca.mcmaster.cas.se2aa4.pathfinder.adt.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class IslandGenerator {

    static Structs.Mesh mesh;
    static Shape islandShape;
    static AltitudeProfile altProfile;
    static BiomeProfile biomeProfile;
    static SoilProfile soilProfile;
    static int numLakes;
    static int numAquifers;
    static int numRivers;
    static int numCities;

    public static Structs.Mesh Generate(Configuration config){
        setConfigValues(config);
        islandShape.create();
        List<Structs.Polygon> pList = mesh.getPolygonsList();
        List<Structs.Vertex> vList = mesh.getVerticesList();

        List<List<Structs.Polygon>> seperatedTiles = FindLandTiles.seperateTiles(islandShape, pList, vList);
        List<Structs.Polygon> pModList = seperatedTiles.get(0);

        List<Object> altLists;
        altLists = altProfile.addAltitudeValues(pModList,mesh.getSegmentsList(),vList);
        pModList = (List<Structs.Polygon>) altLists.get(0);
        vList = (List<Structs.Vertex>) altLists.get(2);

        pModList = LakeAdder.addLakes(pModList, numLakes);
        pModList = AquiferAdder.addAquifers(pModList, numAquifers);

        List<Structs.Segment> sList = (List<Structs.Segment>) altLists.get(1);
        sList = AddRivers.addRivers(pModList, sList, vList, numRivers);
        pModList = TileHumidifier.setHumidities(pModList, sList, soilProfile);
        pModList = biomeProfile.addBiomes(pModList);

        List<Object> lakeList = LakeAdder.fixLakeAltitudes(numLakes + numRivers, pModList, sList, vList);
        pModList = (List<Polygon>) lakeList.get(0);
        vList = (List<Vertex>) lakeList.get(1);

        List<Structs.Vertex> cities = CityGen.addCities(pModList, vList, numCities);
//         List<Structs.Segment> roadSegs = CityGen.addRoads(pModList, vList, numCities);


        return Structs.Mesh.newBuilder().addAllPolygons(pModList).addAllSegments(sList).addAllVertices(vList).addAllVertices(cities).build();
    }
    
    private static void setConfigValues(Configuration config){
        mesh = config.inputMesh;
        islandShape = config.shapeObj;
        altProfile = config.altProfile;
        biomeProfile = config.biomeProfile;
        numLakes = config.num_lakes;
        numAquifers = config.num_aquifers;
        numRivers = config.num_rivers;
        soilProfile = config.soilProfile;
        numCities = config.numCities;
    }
}
