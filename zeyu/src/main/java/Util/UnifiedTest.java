package Util;

import DFS.DFS;
import DFS.DFSGraph;
import Grail.Grail;
import Grail.GrailGraph;
import PathTree.PathTree;
import PathTree.PathTreeGraph;
import PathTree.PathTreeGraphUtil;
import Util.RandomQueryGenerator;
import javafx.util.Pair;
import org.neo4j.graphalgo.CostEvaluator;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphalgo.impl.path.TraversalAStar;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class UnifiedTest {


    public static void main(String[] args){
        String graphname ="human";
        File file = new File("/Users/luke/Documents/毕设/workplace/neo4jReachQueryAlgo/graph/"+graphname);
        //File testFile = new File("/Users/liuzeyu/Documents/code/testdata/positive/agrocyc.test");
        GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
        GraphDatabaseService db = dbFactory.newEmbeddedDatabase(file);
        long starttime;
        long endtime;
        int positive=0;
        int negative=0;

        try (Transaction tx = db.beginTx()) {
            GrailGraph grailgraph = new GrailGraph(db);
            DFSGraph dfsgraph = new DFSGraph(db);
            PathTreeGraph ptgraph = new PathTreeGraph(db);

            starttime=System.currentTimeMillis();
            Grail g = new Grail(grailgraph,5);
            endtime=System.currentTimeMillis();
            System.out.println("Grail Construction Index time:"+String.valueOf(endtime-starttime));

            int gsize = ptgraph.num_vertices();
            ArrayList<Integer> reverse_topo_sort= new ArrayList<>();
            int[] sccmap = new int[gsize];
            PathTreeGraphUtil.mergeSCC(ptgraph,sccmap,reverse_topo_sort);

            starttime=System.currentTimeMillis();
            PathTree pt = new PathTree(ptgraph,reverse_topo_sort,sccmap);
            pt.createLabels();
            endtime=System.currentTimeMillis();
            System.out.println("PathTree Construction Index time:"+String.valueOf(endtime-starttime));

            DFS dfs = new DFS(dfsgraph);

            LinkedList<Pair> query = RandomQueryGenerator.gernatorRandomQuery(100000, grailgraph.getNumVertices());



            positive=0;
            negative=0;
            starttime=System.currentTimeMillis();
            for(Pair<Integer,Integer> p:query){
                if(g.reach(p.getKey(),p.getValue()))
                    positive++;
                else
                    negative++;
            }
            endtime=System.currentTimeMillis();
            System.out.println("Grail Query time:"+String.valueOf(endtime-starttime));
            System.out.println("Positve:"+String.valueOf(positive));
            System.out.println("Negative:"+String.valueOf(negative));

            positive=0;
            negative=0;
            starttime=System.currentTimeMillis();
            for(Pair<Integer,Integer> p:query){
                if(pt.reach(p.getKey(),p.getValue()))
                    positive++;
                else
                    negative++;
            }
            endtime=System.currentTimeMillis();
            System.out.println("PathTree Query time:"+String.valueOf(endtime-starttime));
            System.out.println("Positve:"+String.valueOf(positive));
            System.out.println("Negative:"+String.valueOf(negative));


            positive=0;
            negative=0;
            starttime=System.currentTimeMillis();
            for(Pair<Integer,Integer> p:query){
                if(dfs.reach(p.getKey(),p.getValue()))
                    positive++;
                else
                    negative++;
            }
            endtime=System.currentTimeMillis();
            System.out.println("DFS Query time:"+String.valueOf(endtime-starttime));
            System.out.println("Positve:"+String.valueOf(positive));
            System.out.println("Negative:"+String.valueOf(negative));





            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
