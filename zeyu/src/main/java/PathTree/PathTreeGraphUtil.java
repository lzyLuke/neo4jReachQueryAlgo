package PathTree;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PathTreeGraphUtil {

    void mergeSCC(PathTreeGraph g, int[] on , ArrayList<Integer> reverse_topo_sort){
        ArrayList<Integer> sn;

        HashMap<Integer, Pair<Integer,Integer>> order;
        int ind=0;
        Multimap<Integer,Integer> sccmap = ArrayListMultimap.create();

        int scc =0;
        int vid;
        int origsize=g.getNumVertices();

        for(int i=0;i<origsize;i++){
            vid=i;
            if(g.getNode(vid).visited)
                continue;
            tarjan(g,vid,ind,order,sn,sccmap,scc);
        }

        if(scc==origsize){
            for (int i=0;i<origsize;i++)
                on[i] = i;
            topological_sort(g,reverse_topo_sort);

            for(int i=0;i<reverse_topo_sort.size();i++)
                g.getNode(reverse_topo_sort.get(i)).topo_id=reverse_topo_sort.size()-i-1;
            return;
        }

        HashMap<Integer,ArrayList<Integer>> inlist = new HashMap<Integer, ArrayList<Integer>>();
        HashMap<Integer,ArrayList<Integer>> outlist = new HashMap<Integer, ArrayList<Integer>>();
        g.extract(inlist, outlist);

        int num_comp;
        int maxid=g.getNumVertices()-1;

        for(Map.Entry<Integer,Integer> mit:sccmap.entries()){


        }
    }
}
