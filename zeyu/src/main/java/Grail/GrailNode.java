package Grail;

import org.neo4j.graphdb.Node;

public class GrailNode  {
    Node neo4jNode;
    int id;

    GrailNode(Node n){
        neo4jNode = n;
    }


}
