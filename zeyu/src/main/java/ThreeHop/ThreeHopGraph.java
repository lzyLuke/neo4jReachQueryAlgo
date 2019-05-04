package ThreeHop;


import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.ArrayList;
import java.util.HashMap;

public class ThreeHopGraph {
    ArrayList<ThreeHopNode> vl = new ArrayList<ThreeHopNode>();
    ArrayList<InOutList> graph = new ArrayList<InOutList>();
    int vsize;

    public ThreeHopGraph(GraphDatabaseService db){

        for(Node n:db.getAllNodes()){
            vl.add(new ThreeHopNode());

            graph.add(new InOutList());
        }

        for(Relationship rl:db.getAllRelationships()){
            addEdge((int)rl.getStartNodeId(),(int)rl.getEndNodeId());
        }
        vsize=vl.size();
    }

    public ThreeHopGraph(int size){
        vsize = size;
        vl = new ArrayList<>();
        graph=new ArrayList<>();
        for(int i=0;i<vsize;i++) {
            vl.add(new ThreeHopNode());
            graph.add(new InOutList());
        }
    }


    public void addEdge(int sid,int tid){
        graph.get(tid).inList.add(sid);
        graph.get(sid).outList.add(tid);
    }

    public int getNumVertices(){
        return vl.size();
    }

    ArrayList<Integer> getRoots(){
        ArrayList<Integer> roots = new ArrayList<>();
        for(int i=0;i<graph.size();i++){
            if(graph.get(i).inList.size()==0)
                roots.add(i);
        }
        return roots;
    }


    ThreeHopNode getNode(int id){
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
