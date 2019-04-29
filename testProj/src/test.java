import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.*;

public class test {
    public static void main(String[] args) {
        File cplus= new File("/Users/liuzeyu/Desktop/c++");
        File java = new File("/Users/liuzeyu/Desktop/java");
        try {
            BufferedReader crd = new BufferedReader(new FileReader(cplus));
            BufferedReader jrd = new BufferedReader(new FileReader(java));

            String a = crd.readLine();
            String b = jrd.readLine();

            if(!a.equals(b)){
                System.out.println("WTF");
            }
            System.out.println("Same");
        }catch(Exception e){
            e.printStackTrace();
        }


    }

}