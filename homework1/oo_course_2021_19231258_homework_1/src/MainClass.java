public class MainClass {
    public static void main(String[] args) {
        Poly poly = Reader.read();
        poly.getDerivation();
        System.out.println(poly.show());
    }
}
