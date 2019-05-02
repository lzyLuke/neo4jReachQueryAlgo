package Util;

import javafx.util.Pair;
import org.neo4j.graphalgo.CostEvaluator;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.LinkedList;

public class Neo4jFindPath {
    public static void main(String[ ]args){
        String graphname ="agrocyc";
        File file = new File("/Users/luke/Documents/毕设/workplace/neo4jReachQueryAlgo/graph/"+graphname);
        //File testFile = new File("/Users/liuzeyu/Documents/code/testdata/positive/agrocyc.test");
        GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
        GraphDatabaseService db = dbFactory.newEmbeddedDatabase(file);
        long starttime;
        long endtime;
        int positive=0;
        int negative=0;
        LinkedList<Pair> query = RandomQueryGenerator.gernatorRandomQuery(10000, 1000);
        try (Transaction tx = db.beginTx()) {
        //neo4j method:
        PathExpander<Object> pathExpander = PathExpanders.allTypesAndDirections();

        Node node1 = db.getNodeById(1);
        Node node2 = db.getNodeById(2);

        CostEvaluator<Double> costEvaluator = new Evaluator();
        PathFinder<WeightedPath> dijkstraPathsFinder = GraphAlgoFactory.dijkstra(pathExpander, costEvaluator,1);
        PathFinder allPathFinder = GraphAlgoFactory.allSimplePaths(pathExpander, 100);

        starttime=System.currentTimeMillis();
        for(Pair<Integer,Integer> pair:query) {
            Path p = findpath(allPathFinder, db, pair.getKey(), pair.getValue());
            if(p.length()<=2)
                negative++;
            else
                positive++;
        }
        endtime=System.currentTimeMillis();

        System.out.println(endtime-starttime);
            System.out.println(positive);
            System.out.println(negative);
            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Path findpath(PathFinder<WeightedPath> pf,GraphDatabaseService db,int src, int trg){
        return pf.findSinglePath(db.getNodeById(src),db.getNodeById(trg) );

    }
}
