package Grail;

import Grail.GrailGraph;
import Util.RandomQueryGenerator;
import javafx.util.Pair;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;

import Grail.*;
public class StartAlgo {



    public static void main(String[] args){
        String graphname ="amaze";
        File file = new File("/Users/luke/Documents/毕设/workplace/neo4jReachQueryAlgo/graph/"+graphname);
        //File testFile = new File("/Users/liuzeyu/Documents/code/testdata/positive/agrocyc.test");
        GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
        GraphDatabaseService db = dbFactory.newEmbeddedDatabase(file);
        long starttime;
        long endtime;
        int positive=0;
        int negative=0;
        try (Transaction tx = db.beginTx()) {
            GrailGraph gg = new GrailGraph(db);
            starttime=System.currentTimeMillis();
            Grail g = new Grail(gg,5);
            endtime=System.currentTimeMillis();
            System.out.println("Construction Index time:"+String.valueOf(endtime-starttime));



            LinkedList<Pair> query = RandomQueryGenerator.gernatorRandomQuery(100000, gg.getNumVertices());
            System.out.println("Construction Index time:"+String.valueOf(endtime-starttime));
            starttime=System.currentTimeMillis();
            for(Pair<Integer,Integer> p:query){
                if(g.reach(p.getKey(),p.getValue()))
                    positive++;
                else
                    negative++;
            }
            endtime=System.currentTimeMillis();
            System.out.println("Query time:"+String.valueOf(endtime-starttime));
            System.out.println("Positve:"+String.valueOf(positive));
            System.out.println("Negative:"+String.valueOf(negative));

            /*
            BufferedReader rd = new BufferedReader(new FileReader(testFile));
            String line;
            String[] tokens;
            while((line =rd.readLine())!=null){
                tokens=line.split("\\s+");
                if(g.reach(Integer.parseInt(tokens[0]),Integer.parseInt(tokens[1])))
                    positive++;
                    else
                        negative++;
            }
            System.out.println(positive);
            System.out.println(negative);
            */
            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
