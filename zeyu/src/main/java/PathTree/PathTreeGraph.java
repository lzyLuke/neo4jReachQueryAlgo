package PathTree;



import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.ArrayList;
import java.util.HashMap;

public class PathTreeGraph {
    ArrayList<PathTreeNode> vl = new ArrayList<PathTreeNode>();
    ArrayList<InOutList> graph = new ArrayList<InOutList>();


    public PathTreeGraph(GraphDatabaseService db){

        for(Node n:db.getAllNodes()){
            vl.add(new PathTreeNode(n));

            graph.add(new InOutList());
        }

        for(Relationship rl:db.getAllRelationships()){
            addEdge((int)rl.getStartNodeId(),(int)rl.getEndNodeId());
        }
    }

    int getNumVertices(){
        return vl.size();
    }
    private void addEdge(int sid,int tid){
        graph.get(tid).inList.add(sid);
        graph.get(sid).outList.add(tid);
    }

    PathTreeNode getNode(int id){
        return vl.get(id);
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

    void extract(HashMap<Integer,ArrayList<Integer>> inlist, HashMap<Integer,ArrayList<Integer>> outlist){
        for(int i=0;i<vl.size();i++){
            inlist.put(i,graph.get(i).inList);
            outlist.put(i,graph.get(i).outList);
        }
    }
}
