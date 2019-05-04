package ThreeHop;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import org.apache.commons.lang3.mutable.MutableInt;
import sun.jvm.hotspot.utilities.IntervalTree;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;

public class ThreeHop {


    //typedef set<LabelPair, paircomp> PSET;
    //typedef set<LabelPair, strictPairComp> BSET;
    //typedef set<Interval, IntervalComp> SSET;
    //typedef hash_map<int, set<int> > HSET;
    //typedef priority_queue<BInfor, vector<BInfor>, BInforComp> PQUEUE;
    //typedef hash_map<int, VertexInfor> BVMAP;
    ThreeHopGraph g;

    ArrayList<ArrayList<TreeSet<LabelPair>>> pset;
    ArrayList<ArrayList<Integer>> chainMap;
    ArrayList<TreeSet<Integer>> l_in, l_out;
    ArrayList<ArrayList<Integer>> l_in_vec,l_out_vec;
    ArrayList<TreeSet<Interval>> interval_in,interval_out;
    IntervalTree tree_in,tree_out;

    int[] mark;
    int[] query_util;

    double ratio;
    int chain_size;
    int cp_size;
    ArrayList<Integer> grts;
    int[][] chains;


    ThreeHop(ThreeHopGraph graph, ArrayList<Integer> ts, double rate){
        grts = ts;
        int N = g.getNumVertices();
        chains = new int[N][2];
    }

    public void createLabels(){
        transitiveClosureContour();



    }

    void transitiveClosureContour() {
        ThreeHopGraphUtil.chainDecomposition(g, chainMap, grts, chains);
        chain_size = chainMap.size();

        boolean[] change = new boolean[chain_size];
        int[][] minoid = new int[chain_size][chain_size];
        for (int i = 0; i < chain_size; i++){
            for (int j = 0; j < chain_size; j++) {
                minoid[i][j] = Integer.MAX_VALUE;
            }
        }

        pset = new ArrayList<>();

        for(int i=0;i<chain_size;i++) {
            ArrayList<TreeSet<LabelPair>> temparray = new ArrayList<>();
            for(int j=0;j<chain_size;j++){
                temparray.add(new TreeSet<LabelPair>(new paircomp()));
            }
            pset.add(temparray);
        }


        int ucid,vcid;
        LabelPair pit;
        for(int rtit:grts){
            for(int i=0;i<chain_size;i++)
                change[i]=false;

            ucid = chains[rtit][0];

            ArrayList<Integer> el = g.outEdges(rtit);
            for(int eit:el){
                vcid = chains[eit][0];
                if(chains[eit][1]<minoid[ucid][vcid]&&ucid!=vcid){
                    minoid[ucid][vcid] = chains[eit][1];
                    change[vcid]=true;
                    for(int j=0;j<chain_size;j++){
                        if(j!=ucid && j!=vcid){

                            if(pset.get(vcid).get(j).contains(new LabelPair(chains[eit][1],Integer.MAX_VALUE)))
                                pit=new LabelPair(chains[eit][1],Integer.MAX_VALUE);
                            else{
                                pit = pset.get(vcid).get(j).lower(new LabelPair(chains[eit][1],Integer.MAX_VALUE));
                                if(pit==null) continue;
                                if(minoid[ucid][j] > pit.second){
                                    minoid[ucid][j] = pit.second;
                                    change[j]=true;
                                }
                            }
                        }
                    }
                }

                for( int i=0;i< chain_size;i++){
                    if(i!=ucid&&minoid[ucid][i]!=Integer.MAX_VALUE && change[i]){
                        pset.get(ucid).get(i).add(new LabelPair(chains[rtit][1],minoid[ucid][i]));
                    }
                }
            }

            for(int i=0;i<chain_size;i++){
                minoid[i]=null;
            }
            minoid = null;
            change = null;
        }
    }


    void threeHopContour(){
        ArrayList<Integer> order = new ArrayList<Integer>();
        int[] csize = new int[chain_size];

        cp_size = 0;

        int uncover_size = 0;

        TreeSet<Integer>[] ps = new TreeSet[g.getNumVertices()];
        for(int i=0;i<g.getNumVertices();i++){
            ps[i] = new TreeSet();
        }

        for(int i=0;i<chain_size;i++){
            for(int j=0;j<chain_size;j++){
                if (i!=j){
                    cp_size+=pset.get(i).get(j).size();
                    for(LabelPair psit :pset.get(i).get(j)){
                        ps[chainMap.get(i).get(psit.first)].add(chainMap.get(i).get(psit.second));
                    }
                    uncover_size +=pset.get(i).get(j).size();

                    csize[i] += pset.get(i).get(j).size();
                    csize[j] += pset.get(i).get(j).size();
                }

            }
            order.add(i);
        }


        TreeSet<Integer>[] uncovered = new TreeSet[g.getNumVertices()] ;
        for(int i=0;i<g.getNumVertices();i++){
            uncovered[i] = new TreeSet<Integer>(ps[i]);
        }

        Collections.sort(order, new index_comp(csize));

        l_in = new ArrayList<TreeSet<Integer>>();
        l_out = new ArrayList<TreeSet<Integer>>();

        for(int i=0;i<g.getNumVertices();i++){
            l_in.add(new TreeSet<Integer>());
            l_out.add(new TreeSet<Integer>());
        }

        int threshold = (int)(uncover_size*ratio);

        if(threshold==0) threshold=1;
        HashMap<Integer,TreeMap<Integer,Integer>> X = new HashMap<>();
        HashMap<Integer,TreeMap<Integer,Integer>> Y = new HashMap<>();
        HashMap<Integer,HashMap<Integer,TreeSet<Integer>>> edgeMap = new HashMap<>();
        MutableInt cno = new MutableInt(0);
        MutableInt uncoverSize = new MutableInt(uncover_size);
        while(cno.intValue()<chain_size && uncover_size>0){
            constructBipartite(X,Y,edgeMap,ps,order,cno,threshold);
            processBipartite(X,Y,edgeMap,ps,uncovered,uncoverSize);
        }

        l_in_vec = new ArrayList<>();
        l_out_vec = new ArrayList<>();

        for(int i=0;i<g.getNumVertices();i++){
            l_in_vec.add(new ArrayList<Integer>(l_in.get(i)));
            l_out_vec.add(new ArrayList<Integer>(l_out.get(i)));
        }

        mark = new int[chain_size];
        query_util = new int[chain_size];
        for (int i=0;i<chain_size;i++){
            mark[i] = -1;
            query_util[i] = -1;
        }


    }


    void constructBipartite(HashMap<Integer,TreeMap<Integer,Integer>> X,
                            HashMap<Integer,TreeMap<Integer,Integer>> Y,
                            HashMap<Integer,HashMap<Integer,TreeSet<Integer>>> edgeMap,
                            TreeSet<Integer>[] ps,
                            ArrayList<Integer> order,MutableInt cno, int threshold){
        X.clear();
        Y.clear();
        edgeMap.clear();



        int result;
        int u,v;

        ArrayList<Multimap<Integer,LabelPair>> vin = new ArrayList();
        ArrayList<Multimap<Integer,LabelPair>> vout = new ArrayList();
        for(int i=0;i<chain_size;i++){
            vin.add(ArrayListMultimap.create());
            vout.add(ArrayListMultimap.create());
        }

        for(int i=0;i<chain_size;i++){
            for(int j=0; j <chain_size; j++){
                if(i!=j){
                   for(LabelPair psit:pset.get(i).get(j)) {
                       u = chainMap.get(i).get(psit.first);
                       v = chainMap.get(i).get(psit.second);
                       vout.get(i).put(psit.first,new LabelPair(u,v));
                       vin.get(j).put(psit.second,new LabelPair(u,v));
                   }
                }
            }
        }

        int counter = 0;
        int index = -1;
        int begin = cno.intValue();

        for (int k= cno.intValue();k<chain_size;k++){
            int i = order.get(k);
            for(Map.Entry<Integer,LabelPair>bsit:vin.get(i).entries()){
                X.get(i).put(bsit.getValue().first,bsit.getValue().second);
                Y.get(i).put(bsit.getValue().second,bsit.getValue().first);
            }

            for(Map.Entry<Integer,LabelPair>bsit:vout.get(i).entries()){
                X.get(i).put(bsit.getValue().first,bsit.getValue().second);
                Y.get(i).put(bsit.getValue().second,bsit.getValue().first);
            }
            counter +=X.get(i).size();
            counter +=Y.get(i).size();
            if(counter > threshold){
                index = k+1;
                break;
            }
            else
                index=k+1;
        }

        for(int k=cno.intValue();k<index;k++){
            int i = order.get(k);
            for(Map.Entry<Integer,LabelPair> bsit:vin.get(i).entries()){
                for(Map.Entry<Integer,LabelPair> bsit1:vout.get(i).entries()){
                    u = bsit.getValue().first;
                    v = bsit1.getValue().second;
                    if(chains[bsit.getValue().second][1]<=chains[bsit1.getValue().first][1]){
                        if(ps[u].contains(v)){

                            addEdgeMap(edgeMap,i,u,v);
                            addEdgeMap(edgeMap,i,v,u);
                        }
                    }
                }

            }

            for( Map.Entry<Integer,LabelPair> bsit:vin.get(i).entries()){
                addEdgeMap(edgeMap, i, bsit.getValue().first,bsit.getValue().second );
                addEdgeMap(edgeMap,i,bsit.getValue().second,bsit.getValue().first);
            }

            for( Map.Entry<Integer,LabelPair> bsit:vout.get(i).entries()){
                addEdgeMap(edgeMap, i, bsit.getValue().first,bsit.getValue().second );
                addEdgeMap(edgeMap,i,bsit.getValue().second,bsit.getValue().first);
            }
            cno.setValue(index);
        }

    }

    void addEdgeMap(HashMap<Integer,HashMap<Integer,TreeSet<Integer>>> edgeMap,int i,int j,int value){
        if(!edgeMap.containsKey(i))
            edgeMap.put(i,new HashMap<Integer,TreeSet<Integer>>());
        HashMap<Integer,TreeSet<Integer>> secondmap = edgeMap.get(i);

        if(!secondmap.containsKey(j))
            secondmap.put(j,new TreeSet<Integer>());
        TreeSet<Integer> ts = secondmap.get(j);
        ts.add(value);
    }

    void processBipartite(HashMap<Integer,TreeMap<Integer,Integer>> X,
                            HashMap<Integer,TreeMap<Integer,Integer>> Y,
                            HashMap<Integer,HashMap<Integer,TreeSet<Integer>>> edgeMap,
                            TreeSet<Integer>[] ps,TreeSet<Integer>[] uncovered,
                          MutableInt uncover_size
                            ){

    }
}


