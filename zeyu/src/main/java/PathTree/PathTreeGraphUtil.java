package PathTree;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import javafx.util.Pair;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.*;

public class PathTreeGraphUtil {

    static void mergeSCC(PathTreeGraph g, int[] on , ArrayList<Integer> reverse_topo_sort){
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
            //tarjan(g,vid,ind,order,sn,sccmap,scc);
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
        TreeMap<Integer,Integer> tp = new TreeMap<>();

    }

    static void topological_sort(PathTreeGraph g,ArrayList<Integer> reverse_topo_sort){
        boolean[] visited = new boolean[g.getNumVertices()];
        ArrayList<Integer> preorder = new ArrayList<>();
        ArrayList<Integer> postorder = new ArrayList<>();
        ArrayList<Integer> roots = g.getRoots();

        for(Integer sit:roots){
            if(!visited[sit])
                dfs(g,sit,preorder,postorder,visited);
        }

        reverse_topo_sort.addAll(postorder);
    }


    static void dfs(PathTreeGraph g, int vid, ArrayList<Integer> preorder, ArrayList<Integer> postorder,boolean[] visited){
        visited[vid]=true;
        preorder.add(vid);
        ArrayList<Integer> el = g.outEdges(vid);
        int nextid = -1;

        for(Integer eit:el){
            if(!visited[eit]){
                nextid = eit;
                dfs(g,nextid,preorder,postorder,visited);
            }
        }
        postorder.add(vid);
    }

    static void pathDecomposition(PathTreeGraph g, ArrayList<ArrayList<Integer>> pathMap,ArrayList<Integer> reverse_topo_sort){

        Collections.reverse(reverse_topo_sort);
        ArrayList<Integer> topo_sort = new ArrayList(reverse_topo_sort);
        Collections.reverse(reverse_topo_sort);
        ArrayList<Integer> el;
        int i=0,k;
        int min;
        int min_id;
        boolean[] visited = new boolean[g.num_vertices()];
        ArrayList<Integer> path = new ArrayList<>();

        for(Integer rit:topo_sort){
            k=rit;
            if(visited[k]) continue;
            visited[k]=true;
            path.add(k);
            g.getNode(k).path_id=i;
            do {
                min = Integer.MAX_VALUE;
                min_id=-1;
                el=g.outEdges(k);
                for(Integer eit:el){
                    if(!visited[eit]&&g.getNode(eit).topo_id<min){
                        min = g.getNode(eit).topo_id;
                        min_id = eit;
                    }
                }
                if(min_id!=-1){
                    visited[min_id]=true;
                    g.getNode(min_id).path_id=i;
                    path.add(min_id);
                    k=min_id;
                }

            }while(min_id!=-1);

            pathMap.add(path);
            path = new ArrayList<>();
            i++;
        }
    }

    void traverse(PathTreeGraph tree, int vid ,MutableInt pre_post, boolean[] visited){
        visited[vid]=true;
        ArrayList<Integer> el =tree.outEdges(vid);
        int pre_order;
        for(int eit:el){
            pre_order=pre_post.intValue();
            pre_post.increment();
            if(!visited[eit])
                traverse(tree,eit,pre_post,visited);
            tree.getNode(eit).pre_order = pre_order;
            tree.getNode(eit).post_order = pre_post.intValue();
            pre_post.increment();
        }
    }
    void pre_post_labeling(PathTreeGraph tree){
        ArrayList<Integer> roots = tree.getRoots();
        MutableInt pre_post = new MutableInt(0);
        int pre_order = 0;
        boolean[] visited = new boolean[tree.num_vertices()];

        for(int sit:roots){
            pre_order = pre_post.intValue();
            pre_post.increment();
            traverse(tree,sit,pre_post,visited);
            tree.getNode(sit).pre_order=pre_order;
            tree.getNode(sit).post_order=pre_post.intValue();
            pre_post.increment();
        }


    }



}
