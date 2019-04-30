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
        File file  = new File("/Users/liuzeyu/Documents/idea/neo4jReachQueryAlgo/graph/agrocyc");
        //For my own MBP:
        //File file  = new File("/Users/luke/Documents/毕设/workplace/neo4jReachQueryAlgo/graph/agrocyc");

        File testFile = new File("/Users/liuzeyu/Documents/code/testdata/positive/agrocyc.test");

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
            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
