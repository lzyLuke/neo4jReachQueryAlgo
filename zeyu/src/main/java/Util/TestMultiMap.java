package Util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Map;

public class TestMultiMap {
    public static void main(String[] args){
        Multimap<Integer,Integer> map = ArrayListMultimap.create();

        map.put(2,4);
        map.put(2,3);
        map.put(1,2);
        map.put(1,3);

        for(Map.Entry<Integer,Integer> en:map.entries()){
            System.out.println(en);
        }
    }
}
