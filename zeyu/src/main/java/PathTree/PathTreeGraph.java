package PathTree;



import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.ArrayList;
import java.util.HashMap;

public class PathTreeGraph {
    ArrayList<PathTreeNode> vl = new ArrayList<PathTreeNode>();
    ArrayList<InOutList> graph = new ArrayList<InOutList>();
    int vsize;
    public PathTreeGraph(){
        vsize=0;

    }

    public PathTreeGraph(int size){
        vsize=size;
        for(int i=0;i<size;i++) {
            vl.add(new PathTreeNode());
            graph.add(new InOutList());
        }

    }

    public PathTreeGraph(GraphDatabaseService db){

        for(Node n:db.getAllNodes()){
            vl.add(new PathTreeNode());
            graph.add(new InOutList());
        }

        for(Relationship rl:db.getAllRelationships()){
            addEdge((int)rl.getStartNodeId(),(int)rl.getEndNodeId());
        }
        vsize = vl.size();
    }

    int getNumVertices(){
        return vl.size();
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

    ArrayList<Integer> getRoots(){
        ArrayList<Integer> roots = new ArrayList<>();
        for(int i=0;i<graph.size();i++){
            if(graph.get(i).inList.size()==0)
                roots.add(i);
        }
        return roots;
    }

    void addVertex(int vid){
        if(vid>=vl.size()){
            int size = vl.size();
            for(int i=0;i<(vid-size+1);i++){
                graph.add(new InOutList());
                vl.add(new PathTreeNode());
            }

            vsize=vl.size();
        }

        PathTreeNode v = new PathTreeNode();
        v.visited=false;
        vl.set(vid, v);
        InOutList oil = new InOutList();
        graph.set(vid,oil);
    }

    void addEdge(int sid,int tid){
        if(sid >=vl.size())
            addVertex(sid);
        if(tid>=vl.size())
            addVertex(tid);
        graph.get(tid).inList.add(sid);
        graph.get(sid).outList.add(tid);
    }

    public int num_vertices(){
        return vl.size();
    }



}
