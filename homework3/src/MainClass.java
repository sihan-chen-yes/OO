import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        StringBuffer expression = new StringBuffer(input);
        Regex reg = new Regex();
        Factory factory = new Factory(reg);
        try {
            Function function = factory.generate(expression);
            Function derivation = function.getDerivation();
            System.out.println(derivation.toString());
        } catch (Exception e) {
            System.out.println("WRONG FORMAT!");
        }
    }
}
