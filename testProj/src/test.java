import java.util.*;

public class test {
    public static void main(String[] args) {
        HashSet<Integer> hs = new HashSet<>();

        hs.add(1);
        hs.add(2);
        hs.add(3);
        hs.add(4);
        hs.add(5);

        for(Iterator<Integer> sit1=hs.iterator();sit1.hasNext();){
            int sit =sit1.next();
            if(sit==3)
                sit1.remove();
        }

        System.out.println(hs);
    }

}