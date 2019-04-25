package CreateGraph;

import Grail.Grail;
import Grail.GrailGraph;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ImportGraph {
    public static void main(String[] args) {


        File file = new File("/Users/liuzeyu/Documents/idea/neo4jReachQueryAlgo/graph/agrocyc");
        File testFile = new File("/Users/liuzeyu/Documents/code/testdata/positive/agrocyc.test");
        GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
        GraphDatabaseService db = dbFactory.newEmbeddedDatabase(file);
        try (Transaction tx = db.beginTx()) {


            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
