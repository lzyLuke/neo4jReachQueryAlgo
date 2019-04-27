package PathTree;

import javafx.util.Pair;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.*;

public class PathTree {
    PathTreeGraph g;
    DWGraph pg;
    PathTreeGraph ng;//equivalent weight graph
    DWGraph branch;
    PathTreeGraph newbranch;

    int[] nextVertex;
    Set[] out_uncover;
    ArrayList<ArrayList<Integer>> pathMap;
    ArrayList<Integer> grts;
    int[][] labels;




    PathTree(PathTreeGraph graph, ArrayList<Integer> ts){
        grts = ts;
        int maxid = g.getNumVertices();
        labels = new int[maxid][3];

        nextVertex = new int[maxid];
        out_uncover = new Set[maxid];
        for(int i=0;i<maxid;i++){
            nextVertex[i]=-1;
            out_uncover[i] = new HashSet<Integer>();
        }
        pathMap = new ArrayList<>();
        g=new PathTreeGraph();
        pg = new DWGraph();
        ng = new PathTreeGraph(maxid);
        branch = new DWGraph();
        newbranch = new PathTreeGraph();


    }

    void buildWeightPathGraph(){
        PathTreeGraphUtil.pathDecomposition(g, pathMap, grts);
        HashMap<Integer,Integer> fastMap = new HashMap<>();
        ArrayList<Integer> path = new ArrayList<>();
        ArrayList<Integer> el ;
        int edgeid =0;
        int depth;
        int gsize = pathMap.size()+10;
        ArrayList<Integer> mit;
        for(int k=0;k<pathMap.size();k++){
            mit = pathMap.get(k);
            pg.addVertex(k);
            depth=1;
            path = mit;
            for(Integer lit :path){
                el = g.outEdges(lit);
                for(Integer eit:el){
                    if(g.getNode(eit).path_id!=k){
                        if(fastMap.containsKey(k*gsize+g.getNode(eit).path_id)){

                            pg.addEdge(k,g.getNode(eit).path_id,depth,
                                    fastMap.get(k*gsize+g.getNode(eit).path_id)
                                    );
                        }else{
                            fastMap.put(k*gsize+g.getNode(eit).path_id,edgeid);
                            pg.addEdge(k,g.getNode(eit).path_id,depth,edgeid);
                            edgeid++;
                        }

                    }
                }
                depth++;
            }
        }
    }

    void createLabels(){

        //building weighted path graph
        buildWeightPathGraph();

        //find maximum branching

        DWGraphUtil.findMaxBranching(pg,branch);


        newbranch = new PathTreeGraph(branch.numVertices());

        transform(branch,newbranch);

        buildEquGraph();


    }

    void transform(DWGraph branch,PathTreeGraph graph){
        HashMap<Integer,DWVertex> dwvl = branch.vertices();
        ArrayList<Integer> dwel;

        for(int i=0;i<branch.numVertices();i++)
            graph.addVertex(i);

        for(Integer dwvit:dwvl.keySet()){
            dwel = branch.outEdges(dwvit);
            for(Integer dweit:dwel){
                graph.addEdge(dwvit,branch.target(dweit));
            }
        }
    }

    void buildEquGraph(){
        ArrayList<Integer> path = new ArrayList<>();
        int p,q;

        for(ArrayList<Integer> mit: pathMap){
            path = mit;
            if(path.size()==1)continue;

            for(int lit=0;lit<path.size();){
                p = path.get(lit);
                lit++;
                if(lit==path.size()) break;
                q=path.get(lit);
                nextVertex[p]=q;
                ng.addEdge(p,q);
            }
        }

        ArrayList<Integer> el ;
        ArrayList<Integer> el1 ;
        int source_path_maxtopo=0;
        int max_id;
        int max_topo_id=Integer.MIN_VALUE;
        int depth;
        int gsize = newbranch.num_vertices();
        for (int i=0 ; i<gsize;i++){
            el = newbranch.outEdges(i);
            for( int eit:el){
                path = pathMap.get(eit);
                source_path_maxtopo = Integer.MIN_VALUE;
                for(int lit:path){
                    max_id = -1;
                    max_topo_id = Integer.MIN_VALUE;
                    el1 = g.inEdges(lit);
                    for (int eit1:el1){
                        if(g.getNode(eit1).path_id==i&&g.getNode(eit1).topo_id>max_topo_id){
                            max_id=eit1;
                            max_topo_id=g.getNode(eit1).topo_id;
                        }
                    }
                    if(max_id==-1||max_topo_id<=source_path_maxtopo)
                        continue;
                    source_path_maxtopo = max_topo_id;
                    ng.addEdge(max_id,lit);
                }
            }
        }


    }

    void pathDFS(int vid, MutableInt order,MutableInt first_order, boolean[] visited){
        visited[vid] =true;
        g.getNode(vid).first_visit=first_order.intValue();
        first_order.increment();
        if(nextVertex[vid]!=-1){
            if(!visited[nextVertex[vid]])
                pathDFS(nextVertex[vid],order,first_order,visited);
        }

        ArrayList<Integer> el = ng.outEdges(vid);
        for(int eit:el){
            if(!visited[eit])
                pathDFS(eit,order,first_order,visited);
        }
        g.getNode(vid).dfs_order=order.intValue();
        order.decrement();
    }

    void insertSet(HashSet<Integer> s1,HashSet<Integer> s2){
        boolean insert;
        int pre1,post1,pre2,post2;
        for(int sit2:s2){
            insert=true;
            pre2=labels[sit2][0];
            post2=labels[sit2][1];
            for(int sit1:s1){
                pre1 = labels[sit1][0];
                post1 = labels[sit1][1];
                if(pre2 >= pre1 && post2 <= post1 && labels[sit1][2]<labels[sit2][2]){
                    insert=false;
                    break;
                }
                else if(pre2<=pre1&&post2>=post1&&labels[sit1][2]>labels[sit2][2]){
                    s1.remove(sit1);
                    sit1++;
                }
                else
                    sit1++;

            }

        }
    }
}
