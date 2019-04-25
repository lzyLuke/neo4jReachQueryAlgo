import Grail.GrailGraph;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import Grail.*;
public class StartAlgo {



    public static void main(String[] args){
        File file  = new File("/Users/liuzeyu/Documents/idea/neo4jReachQueryAlgo/graph/agrocyc");
        File testFile = new File("/Users/liuzeyu/Documents/code/testdata/positive/agrocyc.test");
        GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
        GraphDatabaseService db = dbFactory.newEmbeddedDatabase(file);
        try (Transaction tx = db.beginTx()) {
            GrailGraph gg = new GrailGraph(db);

            Grail g = new Grail(gg,5);
            BufferedReader rd = new BufferedReader(new FileReader(testFile));
            String line;
            String[] tokens;
            int positive=0;
            int negative=0;
            while((line =rd.readLine())!=null){
                tokens=line.split("\\s+");
                if(g.reach(Integer.parseInt(tokens[0]),Integer.parseInt(tokens[1])))
                    positive++;
                    else
                        negative++;
            }
            System.out.println(positive);
            System.out.println(negative);

            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
