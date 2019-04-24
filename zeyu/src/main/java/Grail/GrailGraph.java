package Grail;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.ArrayList;

public class GrailGraph {
    ArrayList<GrailNode> vl = new ArrayList<GrailNode>();
    ArrayList<InOutList> graph = new ArrayList<InOutList>();


    GrailGraph(GraphDatabaseService db){

        for(Node n:db.getAllNodes()){
            vl.add(new GrailNode(n));

            graph.add(new InOutList());
        }

        for(Relationship rl:db.getAllRelationships()){
            addEdge((int)rl.getStartNodeId(),(int)rl.getEndNodeId());
        }




    }

    private void addEdge(int sid,int tid){
        graph.get(tid).inList.add(sid);
        graph.get(sid).outList.add(tid);
    }

    GrailNode getNode(int id){
        return vl.get(id);
    }

    int getNumVertices(){
        return vl.size();
    }

    ArrayList<Integer> inEdges(int trg){
        return graph.get(trg).inList;
    }

    ArrayList<Integer> outEdges(int src){
        return graph.get(src).outList;
    }
    int inDegree(int trg){
        return graph.get(trg).inList.size();
    }

    int outDegree(int src){
        return graph.get(src).outList.size();
    }

    //root is zero inDegree node
    ArrayList<Integer> getRoots(){
        ArrayList<Integer> roots = new ArrayList<>();
        for(int i=0;i<graph.size();i++){
            if(graph.get(i).inList.size()==0)
                roots.add(i);
        }
        return roots;
    }
}
