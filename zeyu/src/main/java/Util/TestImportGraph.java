package Util;

import CreateGraph.ImportGraph;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TestImportGraph {
    public static void main(String[] args) {
        String graphname ="amaze";
        File sourceFile = new File("/Users/luke/Documents/paper/code/sigmod08/"+graphname+"_dag_uniq.gra");
        File file = new File("/Users/luke/Documents/毕设/workplace/neo4jReachQueryAlgo/graph/"+graphname);

        GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
        GraphDatabaseService db = dbFactory.newEmbeddedDatabase(file);
        try (Transaction tx = db.beginTx()) {
            BufferedReader rd = new BufferedReader(new FileReader(sourceFile));
            if(!rd.readLine().equals("graph_for_greach")){
                System.out.println("Bad Format");
                return;
            }
            int size = Integer.valueOf(rd.readLine());
            String line;
            String[] tokens1;
            String[] tokens2;
            for(int i=0;i<size;i++){
                db.createNode();
            }

            for(int i=0;i<size;i++){
                line = rd.readLine();
                tokens1 = line.split(":");
                tokens1[1] = tokens1[1].trim();
                tokens2 = tokens1[1].split("\\s+");
                for(String trg:tokens2) {
                    if(!trg.equals("#")) {
                        System.out.print(i);
                        System.out.print(",");
                        System.out.println(trg);

                    }

                }
            }


            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
