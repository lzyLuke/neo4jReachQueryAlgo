package DFS;

public class DFS {
    DFSGraph g;
    public DFS(DFSGraph g){
        this.g=g;
    }

    public boolean reach(int src,int trg){
        boolean[] visitedMap = new boolean[g.getSize()];
        return DFS(src,trg,visitedMap);
    }


    boolean DFS(int src,int trg,boolean[] visitedMap){
        if(src==trg) return true;
        visitedMap[src]=true;
        for(int i:g.getNode(src).outlist){
            if(visitedMap[i]==true)
                continue;
            if(i==trg)
                return true;

            if(DFS(i,trg,visitedMap)==true)
                return true;
        }
        return false;
    }

}
