package PathTree;

import java.util.ArrayList;
import java.util.HashMap;

public class DWGraph {
    HashMap<Integer,InOutList> graph;
    HashMap<Integer,DWVertex> vl;
    HashMap<Integer,DWEdgeProp> edgeOpMap;
    int maxEdgeId;
    int sidcount=0;
    int tidcount=0;
    DWGraph() {
        graph = new HashMap<Integer, InOutList>();
        vl = new HashMap<Integer, DWVertex>();
        edgeOpMap = new HashMap<Integer, DWEdgeProp>();
        maxEdgeId = 0;
    }

    void addVertex(int vid){
        DWVertex v = new DWVertex();
        v.id=vid;
        v.visited=false;

        vl.put(vid,v);
        if(!graph.containsKey(vid))
        graph.put(vid,new InOutList());
    }

    void addEdge(int sid,int tid,int weight,int edgeid){
        if(!vl.containsKey(sid))
            addVertex(sid);
        if(!vl.containsKey(tid))
            addVertex(tid);

        int osid=-1;
        int otid=-1;

        if(edgeOpMap.containsKey(edgeid)){
            osid=edgeOpMap.get(edgeid).src;
            otid=edgeOpMap.get(edgeid).trg;
        }else{
            edgeOpMap.put(edgeid,new DWEdgeProp());
        }

        edgeOpMap.get(edgeid).src=sid;
        edgeOpMap.get(edgeid).trg=tid;
        edgeOpMap.get(edgeid).weight=weight;


        if(sid!=osid) {
            graph.get(sid).outList.add(edgeid);
            sidcount++;
        }

        if(tid!=otid) {
            graph.get(tid).inList.add(edgeid);
            tidcount++;
        }
        if(sid!=osid && tid!=otid)
            maxEdgeId++;
    }


    HashMap<Integer,DWVertex>  vertices(){
        return vl;
    }
    int numVertices(){
        return vl.size();
    }

    ArrayList<Integer> inEdges(int trg){
        return graph.get(trg).inList;
    }
    ArrayList<Integer> outEdges(int src){
        return graph.get(src).outList;
    }

    int target(int edgeid){
        int trg = edgeOpMap.get(edgeid).trg;
        return trg;
    }

    int weight(int src,int trg){
        int w = 0;
        ArrayList<Integer> el = graph.get(src).outList;
        for(int eit:el){
            if(edgeOpMap.get(eit).trg==trg){
                w = edgeOpMap.get(eit).weight;
                break;
            }
        }
        return w;
    }

    int edgeId(int src,int trg){
        for(int emit:edgeOpMap.keySet()){
            if(edgeOpMap.get(emit).src == src && edgeOpMap.get(emit).trg==trg)
                return emit;
        }
        return -1;
    }
    void removeEdgeWithID(int sid, int tid, int edgeid){
        if(!vl.containsKey(sid)) {
            System.out.println(String.valueOf(sid)+" not existed");
            return;
        }
        if(!vl.containsKey(tid)){
            System.out.println(String.valueOf(tid)+" not existed");
            return;
        }

        edgeOpMap.remove(edgeid);

        graph.get(sid).outList.remove(new Integer(edgeid));
        graph.get(sid).inList.remove(new Integer(edgeid));
    }

}
