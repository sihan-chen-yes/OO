import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        input = input.replaceAll(" |\t","");
        StringBuffer expression = new StringBuffer(input);
        Regex reg = new Regex();
        Factory factory = new Factory(reg);
        Function function = factory.generate(expression);
        Function derivation = function.getDerivation();
        System.out.println(derivation.toString());
    }
}
