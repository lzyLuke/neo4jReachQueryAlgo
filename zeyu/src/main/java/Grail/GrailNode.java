package Grail;

import org.neo4j.graphdb.Node;

import java.util.ArrayList;

public class GrailNode  {
    Node neo4jNode;
    int id;
    int topoLevel;
    int topoId;
    boolean visited;
    int minParentLevel;
    ArrayList<Integer> pre;
    ArrayList<Integer> post;
    ArrayList<Integer> middle;


    GrailNode(Node n){
        topoLevel=-1;
        visited=false;
        neo4jNode = n;

    }


}
