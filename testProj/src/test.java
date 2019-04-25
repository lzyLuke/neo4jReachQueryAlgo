public class test {
    public static void main(String[]args){
        String[] tokens;
        String line = "123 123 123";
        tokens=line.split("\\s+");
        System.out.println(tokens.length);
    }
}