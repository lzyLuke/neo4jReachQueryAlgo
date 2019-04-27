package PathTree;

import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DWGraphUtil {
    static int branch_depth = 0;

    static void findMaxBranching(DWGraph g, DWGraph maxBranch) {
        HashMap<Integer, DWVertex> vl = g.vertices();
        ArrayList<Integer> el;
        branch_depth++;

        int maxw = Integer.MIN_VALUE;
        DWVertexProp maxe = new DWVertexProp();

        for (Integer vlit : vl.keySet()) {
            maxw = Integer.MIN_VALUE;
            el = g.inEdges(vlit);

            for (Integer eit : el) {
                if (g.edgeOpMap.get(eit).weight > maxw) {
                    maxw = g.edgeOpMap.get(eit).weight;
                    maxe.id = g.edgeOpMap.get(eit).src;
                    maxe.edgeid = eit;
                    maxe.weight = maxw;
                }
            }
            if (maxw != Integer.MIN_VALUE) {
                maxBranch.addEdge(maxe.id, vlit, maxw, maxe.edgeid);
            } else
                maxBranch.addVertex(vlit);

        }


        ArrayList<Integer> list_s = new ArrayList<>();
        MutableInt index = new MutableInt(0);
        MutableInt scc_num = new MutableInt(0);
        ArrayList<Integer> sn = new ArrayList<>();
        HashMap<Integer, Pair> order = new HashMap<Integer, Pair>();
        HashMap<Integer, ArrayList<Integer>> sccmap = new HashMap<>();
        int vid;
        vl = maxBranch.vertices();
        for (Integer vlit : vl.keySet()){
            vid = vlit;
            if(maxBranch.vl.get(vid).visited) continue;
            tarjan(maxBranch,vid,index,order,sn,sccmap,scc_num);
        }

        if(scc_num.intValue()==maxBranch.numVertices())
            return;

        int num_comp;
        for(Integer mit:sccmap.keySet()){
            num_comp=mit;

            if(sccmap.get(mit).size()==1)
                continue;

            ArrayList<Integer> sccvec = sccmap.get(mit);
            int minw = Integer.MAX_VALUE;
            int weight,edgeid,src,trg;
            for(int rvit=sccvec.size()-1;rvit>=0;rvit--){
                if(rvit-1>=0){
                    weight = maxBranch.weight(sccvec.get(rvit),sccvec.get(rvit-1));
                    edgeid = maxBranch.edgeId(sccvec.get(rvit),sccvec.get(rvit-1));
                    src = sccvec.get(rvit);
                    trg = sccvec.get(rvit-1);
                }
                else{
                    weight = maxBranch.weight(sccvec.get(rvit),sccvec.get(sccvec.size()-1));
                    edgeid = maxBranch.edgeId(sccvec.get(rvit),sccvec.get(sccvec.size()-1));
                    src = sccvec.get(rvit);
                    trg = sccvec.get(sccvec.size()-1);
                }
                if(weight<minw){
                    minw=weight;
                    maxBranch.removeEdgeWithID(src,trg,edgeid);
                }
            }
        }
    }

    static void tarjan(DWGraph g, int vid, MutableInt index,
                HashMap<Integer, Pair> order,
                ArrayList<Integer> sn,
                HashMap<Integer,ArrayList<Integer>> sccmap,
                MutableInt scc
                ){
        Pair p = new Pair();
        p.first=index.intValue();p.second=index.intValue();
        order.put(vid,p);

        index.increment();

        sn.add(vid);
        g.vl.get(vid).visited=true;
        ArrayList<Integer> el = g.outEdges(vid);
        for(Integer eit:el){
            if(!g.vl.get(g.target(eit)).visited){
                tarjan(g,g.target(eit),index,order,sn,sccmap,scc);
                order.get(vid).second = Math.min(order.get(g.target(eit)).second, order.get(vid).second);
            }else if(sn.contains(g.target(eit))){
                order.get(vid).second=Math.min(order.get(g.target(eit)).first,order.get(vid).second);
            }
        }
        Collections.reverse(sn);
        ArrayList<Integer> reverse_sn = new ArrayList<Integer>(sn);
        Collections.reverse(sn);
        if(order.get(vid).first==order.get(vid).second){
            ArrayList<Integer> vec = new ArrayList<>();
            for(Integer rit:reverse_sn){
                if(rit!=vid){
                    vec.add(rit);
                    sn.remove(sn.size()-1);
                }else{
                    vec.add(rit);
                    sn.remove(sn.size()-1);
                    break;
                }
            }

            sccmap.put(scc.intValue(),vec);
            scc.increment();
        }
    }

}
