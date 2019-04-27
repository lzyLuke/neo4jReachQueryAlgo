import java.util.*;

public class test {
    public static void main(String[] args) {
        HashSet<Integer> hs = new HashSet<>();

        hs.add(1);
        hs.add(2);
        hs.add(3);
        hs.add(4);
        hs.add(5);

        for(int sit:hs){
            if(sit==3)
                hs.remove(3);
            System.out.println(sit);
        }
    }

}