package Grail;

import org.apache.commons.lang3.mutable.MutableInt;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Collections;

public class Grail {
    GrailGraph g;
    int dim;
    int[] visited;
    int QueryCnt=0;
    int PositiveCut=0,NegativeCut=0,TotalCall=0,TotalDepth=0,CurrentDepth=0;
    Grail(GrailGraph graph,int Dim){
        g=graph;
        dim=Dim;
        int maxid=g.getNumVertices();
        visited=new int[maxid];
        for(int i=0;i<maxid;i++){
            graph.getNode(i).pre=new ArrayList<Integer>();
            graph.getNode(i).post=new ArrayList<Integer>();
            graph.getNode(i).middle=new ArrayList<Integer>();
            visited[i]=-1;
        }

        for(int i=0;i<dim;i++){
            randomlabeling(graph);
        }

        PositiveCut=NegativeCut=TotalCall=TotalDepth=CurrentDepth=0;
    }

    void randomlabeling(GrailGraph tree){
        ArrayList<Integer> roots = tree.getRoots();

        MutableInt prePost = new MutableInt(0);
        boolean[] visited=new boolean[tree.getNumVertices()];
        Collections.shuffle(roots);
        for(Integer sit:roots){
            prePost.increment();
            visit(tree,sit,prePost,visited);
        }
    }

    int visit(GrailGraph tree,int vid,MutableInt prePost,boolean[] visited){
        visited[vid]=true;
        ArrayList<Integer> el = tree.outEdges(vid);
        Collections.shuffle(el);

        int  preOrder = tree.getNumVertices()+1;
        tree.getNode(vid).middle.add(prePost.intValue());
        for(Integer eit:el){
            if(!visited[eit]){
                preOrder=Math.min(preOrder,visit(tree,eit,prePost,visited));
            }else
                preOrder=Math.min(preOrder,tree.getNode(eit).pre.get(tree.getNode(eit).pre.size()-1));

        }

        preOrder=Math.min(preOrder,prePost.intValue());
        tree.getNode(vid).pre.add(preOrder);
        tree.getNode(vid).post.add(prePost.intValue());
        prePost.increment();
        return preOrder;
    }

    boolean reach(int src,int trg){
        if(src==trg) return true;
        if(!contains(src,trg)) return false;

        visited[src]=++QueryCnt;
        return goForReach(src,trg);

    }

    boolean contains(int src,int trg){

        for(int i=0;i<dim;i++){
            if(g.getNode(src).pre.get(i) > g.getNode(trg).pre.get(i))
                return false;
            if(g.getNode(src).post.get(i) < g.getNode(trg).post.get(i))
                return false;
        }
        return true;

    }

    boolean goForReach(int src,int trg){
        if(src==trg) return true;
        visited[src]=QueryCnt;
        ArrayList<Integer> el = g.outEdges(src);
        for(Integer eit:el){
            if(visited[eit]!=QueryCnt && contains(eit,trg)){
                if(goForReach(eit,trg)) return true;
            }
        }
        return false;
    }


}

