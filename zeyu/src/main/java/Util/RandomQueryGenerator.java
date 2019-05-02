package Util;

import javafx.util.Pair;
import sun.awt.image.ImageWatched;

import java.util.LinkedList;
import java.util.Random;

public class RandomQueryGenerator {

    public static LinkedList<Pair> gernatorRandomQuery(int num,int maxID){
        Random r = new Random(1);

        LinkedList<Pair> result = new LinkedList<>();
        for(int i=0;i<num;i++){
            result.add(new Pair(r.nextInt(maxID), r.nextInt(maxID)));
        }

        return result;
    }
}
