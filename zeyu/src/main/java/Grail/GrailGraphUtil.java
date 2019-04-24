package Grail;

import java.util.ArrayList;

public class GrailGraphUtil {
    void topoLeveler(GrailGraph g){
        int N = g.getNumVertices();
        //depth first serach whole graph
        for(int i=0;i<N;i++)
            topoLevel(g,i);

    }



    int topoLevel(GrailGraph g, int vid){
        if(g.getNode(vid).topLevel!=-1)
            return g.getNode(vid).topLevel;
        int min = g.getNumVertices();
        int max = -1;
        g.getNode(vid).topLevel=0;
        ArrayList<Integer> el = g.inEdges(vid);
        for(Integer eit:el){
            max = max > topoLevel(g,eit)?max:g.getNode(eit).topLevel;
            min = min <g.getNode(eit).topLevel?min:g.getNode(eit).topLevel;
        }

        g.getNode(vid).topLevel=max+1;
        g.getNode(vid).minParentLevel = (min==g.getNumVertices()?-1:min);
        return g.getNode(vid).topLevel;
    }



}


