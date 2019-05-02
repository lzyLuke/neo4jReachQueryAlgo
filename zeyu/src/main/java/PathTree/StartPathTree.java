package PathTree;

import Util.RandomQueryGenerator;
import javafx.util.Pair;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class StartPathTree {
    public static void main(String[] args){
        //For my ByteDance MBP:
        //File file  = new File("/Users/liuzeyu/Documents/idea/neo4jReachQueryAlgo/graph/agrocyc");
        //For my own MBP:
        String graphname ="agrocyc";
        File file = new File("/Users/luke/Documents/毕设/workplace/neo4jReachQueryAlgo/graph/"+graphname);

        File testFile = new File("/Users/liuzeyu/Documents/code/testdata/positive/agrocyc.test");

        GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
        GraphDatabaseService db = dbFactory.newEmbeddedDatabase(file);
        long starttime;
        long endtime;
        int positive=0;
        int negative=0;
        try (Transaction tx = db.beginTx()) {
            PathTreeGraph g = new PathTreeGraph(db);
            int gsize = g.num_vertices();
            ArrayList<Integer> reverse_topo_sort= new ArrayList<>();
            int[] sccmap = new int[gsize];
            PathTreeGraphUtil.mergeSCC(g,sccmap,reverse_topo_sort);
            PathTree pt = new PathTree(g,reverse_topo_sort);
            starttime=System.currentTimeMillis();
            pt.createLabels();
            endtime=System.currentTimeMillis();

            LinkedList<javafx.util.Pair> query = RandomQueryGenerator.gernatorRandomQuery(100000, g.getNumVertices());
            System.out.println("Construction Index time:"+String.valueOf(endtime-starttime));
            starttime=System.currentTimeMillis();
            for(Pair<Integer,Integer> p:query){
                if(pt.rawReach(p.getKey(),p.getValue()))
                    positive++;
                else
                    negative++;
            }
            endtime=System.currentTimeMillis();
            System.out.println("Query time:"+String.valueOf(endtime-starttime));
            System.out.println("Positve:"+String.valueOf(positive));
            System.out.println("Negative:"+String.valueOf(negative));
            /**
            BufferedReader rd = new BufferedReader(new FileReader(testFile));
            String line;
            String[] tokens;
            int positive=0;
            int negative=0;
            while((line =rd.readLine())!=null){
                tokens=line.split("\\s+");
                if(pt.reach(sccmap[Integer.parseInt(tokens[0])],sccmap[Integer.parseInt(tokens[1])]))
                    positive++;
                else
                    negative++;
            }

            System.out.println(positive);
            System.out.println(negative);
             **/


            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
