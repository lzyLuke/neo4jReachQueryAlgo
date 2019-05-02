package DFS;

import Grail.GrailNode;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.ArrayList;

public class DFSGraph {
    ArrayList<DFSNode> vl = new ArrayList<DFSNode>();


    public DFSGraph(GraphDatabaseService db){
        for(Node n:db.getAllNodes()){
            vl.add(new DFSNode());
        }

        for(Relationship rl:db.getAllRelationships()){
            int startid = (int)rl.getStartNodeId();
            int endid = (int)rl.getEndNodeId();
            vl.get(startid).outlist.add(endid);
        }
    }

    DFSNode getNode(int indexid){
        return vl.get(indexid);
    }

    int getSize(){
        return vl.size();
    }

    int getNumVertices(){ return vl.size();}


}
