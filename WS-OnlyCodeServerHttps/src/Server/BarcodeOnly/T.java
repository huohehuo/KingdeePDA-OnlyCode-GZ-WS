package Server.BarcodeOnly;

public class T {
    public static void main(String[] args) {
        String s = "171 1100 107917112301477";
        s = s.replaceAll(" ","");
        System.out.println(s);
        System.out.println(s.substring(1,11));
        System.out.println(s.substring(11));
    }
}
