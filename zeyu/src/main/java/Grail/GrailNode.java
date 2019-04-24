package Grail;

import org.neo4j.graphdb.Node;

public class GrailNode  {
    Node neo4jNode;
    int id;
    int topLevel;
    boolean visited;
    int minParentLevel;


    GrailNode(Node n){
        topLevel=-1;
        visited=false;
        neo4jNode = n;

    }


}
