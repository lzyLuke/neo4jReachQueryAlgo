package PathTree;

import org.neo4j.graphdb.Node;

public class PathTreeNode {
    boolean visited;
    boolean fat;
    int topo_id;
    int path_id;
    int dfs_order;
    int pre_order;
    int post_order;
    int first_visit;
    public PathTreeNode(Node n){
    }

    public  PathTreeNode(){

    }
}
