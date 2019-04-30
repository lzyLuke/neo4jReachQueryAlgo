import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class testIT {
    public static void main(String[] args){
        HashSet<Integer> a = new HashSet<>();
        a.add(1);
        a.add(2);
        a.add(3);
        Iterator<Integer> it = a.iterator();

        for(it=a.iterator();it.hasNext();){
            Integer i = it.next();
            if(i==2)
            it.remove();
            System.out.println(i);
        }

        System.out.println(a);






    }
}
