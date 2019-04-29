package PathTree;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class StartPathTree {
    public static void main(String[] args){
        //For my ByteDance MBP:
        //File file  = new File("/Users/liuzeyu/Documents/idea/neo4jReachQueryAlgo/graph/agrocyc");
        //For my own MBP:
        File file  = new File("/Users/luke/Documents/毕设/workplace/neo4jReachQueryAlgo/graph/agrocyc");
        GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
        GraphDatabaseService db = dbFactory.newEmbeddedDatabase(file);
        try (Transaction tx = db.beginTx()) {
            PathTreeGraph g = new PathTreeGraph(db);
            ArrayList<Integer> reverse_topo_sort= new ArrayList<>();
            int gsize = g.num_vertices();
            int[] sccmap = new int[gsize];
            PathTreeGraphUtil.mergeSCC(g,sccmap,reverse_topo_sort);
            PathTree pt = new PathTree(g,reverse_topo_sort);
            pt.createLabels();
            System.out.println("123");
            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
