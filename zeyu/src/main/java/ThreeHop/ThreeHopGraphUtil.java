package ThreeHop;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class ThreeHopGraphUtil {

    static void chainDecomposition(ThreeHopGraph g,ArrayList<ArrayList<Integer>> chainMap,
                            ArrayList<Integer> rts, int[][] chains){
        int N = g.getNumVertices();
        ArrayList<TreeSet<Integer>> pred = new ArrayList(N);
        ArrayList<TreeSet<Integer>> succ = new ArrayList(N);

        for(int i=0;i<N;i++){
            pred.add(new TreeSet<Integer>());
            succ.add(new TreeSet<Integer>());
        }

        ThreeHopGraph tc = new ThreeHopGraph(g.getNumVertices());


    }

    static void transitive_closure(ThreeHopGraph g, ThreeHopGraph tc){
        ArrayList<Integer> ts = new ArrayList<>();
        topological_sort(g,ts);
        ArrayList<Integer> el =new ArrayList<>();
        TreeMap<Integer,TreeSet<Integer>> ms = new TreeMap<>();

        for(int vit:ts){
            TreeSet<Integer> s = new TreeSet<>();
            s.add(vit);
            ms.put(vit,s);
            el = g.outEdges(vit);
            for(int eit:el){
                ms.get(vit).addAll(ms.get(eit));
            }
        }


        for(int mit:ms.keySet()){
            TreeSet<Integer> s = ms.get(mit);
            int src = mit;

            for (int sit: s){
                tc.addEdge(src,sit);
            }
        }
    }

    static void topological_sort(ThreeHopGraph g, ArrayList<Integer> ts){
        boolean[] visited = new boolean[g.getNumVertices()];
        ArrayList<Integer> preorder = new ArrayList<>();
        ArrayList<Integer> postorder = new ArrayList<>();
        ArrayList<Integer> roots = g.getRoots();

        for(int sit:roots){
            if(!visited[sit])
                dfs(g,sit,preorder,postorder,visited);
        }

        ts.addAll(postorder);
    }


    static void dfs(ThreeHopGraph g,int vid, ArrayList<Integer> preorder,
             ArrayList<Integer> postorder, boolean[] visited){
        visited[vid] = true;
        preorder.add(vid);
        ArrayList<Integer> el = g.outEdges(vid);
        int nextid=-1;

        for(int eit:el){
            if(!visited[eit]){
                nextid=eit;
                dfs(g,nextid,preorder,postorder,visited);
            }
        }
        postorder.add(vid);
    }
}
