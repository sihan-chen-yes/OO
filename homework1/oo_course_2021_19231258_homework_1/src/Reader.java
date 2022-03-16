import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reader {
    public static Poly read() {
        Scanner scanner = new Scanner(System.in);
        String in = scanner.nextLine();
        String blank = " |\\t";
        String doublePlus = "\\+\\+";
        String doubleMinus = "--";
        String plusAndMinus = "\\+-";
        String minusAndPlus = "-\\+";
        String singleMinus = "(?<=[^\\*])-";
        String mpm = "-\\+-";
        String pmp = "\\+-\\+";
        String noBlank = in.replaceAll(blank,"");
        String noDoublePlus = noBlank.replaceAll(doublePlus,"+");
        String noDoubleMinus = noDoublePlus.replaceAll(doubleMinus + "|" + mpm,"+");
        String noTri = noDoubleMinus.replaceAll(pmp,"-");
        String info = noTri.replaceAll(plusAndMinus
                + "|" + minusAndPlus
                + "|" + singleMinus,"+-");
        String[] termsTemp = info.split("(?<=[^\\*])\\+");
        ArrayList<String> terms = removeBlank(termsTemp);
        String coeffPattern = "(?<coeff>-?0*\\d+)\\*?";
        String degreePattern = "x(\\*{2}(?<degree>[+-]?\\d+))?\\*?";
        Pattern p = Pattern.compile(coeffPattern + "|" + degreePattern);
        Poly poly = new Poly();
        for (String termInfo : terms) {
            Matcher m = p.matcher(termInfo);
            BigInteger coeff = BigInteger.valueOf(1);
            BigInteger degree = BigInteger.valueOf(0);
            while (m.find()) {
                if (m.group().charAt(0) == 'x') {
                    if ((m.start() - 1) >= 0 && termInfo.charAt(m.start() - 1) == '-') {
                        coeff = coeff.multiply(BigInteger.valueOf(-1));
                    }
                    if (m.group("degree") != null) {
                        BigInteger tmpDegree = new BigInteger(m.group("degree"));
                        degree = degree.add(tmpDegree);
                    } else {
                        degree = degree.add(BigInteger.valueOf(1));
                    }
                } else {
                    BigInteger tmpCoeff = new BigInteger(m.group("coeff"));
                    coeff = coeff.multiply(tmpCoeff);
                }
            }
            Term term = new Term(coeff,degree);
            poly.addTerm(term);
        }
        return poly;
    }
    
    public static ArrayList<String> removeBlank(String[] strs) {
        ArrayList<String> strings = new ArrayList<>();
        for (String str : strs) {
            if (!str.equals("")) {
                strings.add(str);
            }
        }
        return strings;
    }
}
