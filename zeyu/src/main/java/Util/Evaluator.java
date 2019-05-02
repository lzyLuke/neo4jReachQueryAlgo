package Util;

import org.neo4j.graphalgo.CostEvaluator;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Relationship;

public class Evaluator implements CostEvaluator<Double>
{


    public Evaluator(  )
    {
        super();

    }

    @Override
    public Double getCost(Relationship relationship, Direction direction )
    {
        return 1.0;
    }
}
