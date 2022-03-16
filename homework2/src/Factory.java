import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Factory {
    private Regex reg;
    
    public Factory(Regex reg) {
        this.reg = reg;
    }
    
    public Function generate(StringBuffer expression) {
        ArrayList<Function> expList = new ArrayList<>();
        while (hasExpFac(expression)) {
            StringBuffer expFactor = getExpressionFactor(expression);
            expList.add(generate(expFactor));
        }
        return parseAdder(expList, expression.toString());
    }
    
    public Boolean hasExpFac(StringBuffer expression) {
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                if (i == 0) {
                    return true;
                } else if (i > 0) {
                    char pre = expression.charAt(i - 1);
                    if (pre != 'n' && pre != 's') {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public StringBuffer getExpressionFactor(StringBuffer expression) {
        Boolean flag = false;
        int leftNum = 0;
        int leftIndex = -1;
        int rightNum = 0;
        int rightIndex = -1;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                leftNum++;
                char pre = '@';
                if (i > 0) {
                    pre = expression.charAt(i - 1);
                }
                if (i == 0 || (i > 0 && (pre != 'n' && pre != 's') && flag == false)) {
                    leftIndex = i;
                    flag = true;
                }
            }
            if (expression.charAt(i) == ')') {
                rightNum++;
                if (flag == true && leftNum == rightNum) {
                    rightIndex = i;
                    break;
                }
            }
        }
        StringBuffer exp = new StringBuffer(expression.substring(leftIndex + 1, rightIndex));
        expression.replace(leftIndex, rightIndex + 1, "E");
        return exp;
    }
    
    public Function parseAdder(ArrayList<Function> expList, String exp) {
        String expression = exp;
        String term = reg.getTermPattern();
        Pattern p = Pattern.compile(term);
        Matcher m = p.matcher(expression);
        ArrayList<Function> termList = new ArrayList<>();
        while (m.find()) {
            termList.add(parseMulter(expList, m.group()));
            expression = expression.replaceFirst(term, "T");
        }
        while (expression.contains("TT")) {
            expression = expression.replaceAll("TT", "T\\+T");
        }
        expression = expression.replaceAll(" |\t", "");
        if (termList.size() == 1) {
            if (expression.charAt(0) == '-') {
                termList.get(0).setSign('-');
            }
            return termList.get(0);
        }
        int i = 1;
        for (int j = 1; j < expression.length(); j++) {
            if (expression.charAt(j) == '-' || expression.charAt(j) == '+') {
                termList.get(i).setSign(expression.charAt(j));
                i++;
            }
        }
        if (expression.charAt(0) == '-') {
            termList.get(0).setSign('-');
        }
        Adder adder = new Adder(termList.get(0), termList.get(1));
        termList.remove(0);
        termList.remove(0);
        while (!termList.isEmpty()) {
            adder.add(termList.get(0));
            termList.remove(0);
        }
        return adder;
    }
    
    public Function parseMulter(ArrayList<Function> expList, String t) {
        String term = t;
        String factor = reg.getFacPattern();
        Pattern p = Pattern.compile(factor);
        Matcher m = p.matcher(term);
        ArrayList<Function> factorList = new ArrayList<>();
        while (m.find()) {
            factorList.add(parseFactor(expList, m.group()));
            term = term.replaceFirst(factor, "F");
        }
        term = term.replaceAll(" |\t", "");
        if (factorList.size() == 1) {
            if (term.charAt(0) == '-') {
                return new Multer(factorList.get(0), new Constant(new BigInteger("-1")));
            }
            return new Multer(factorList.get(0), new Constant(new BigInteger("1")));
        }
        Multer multer = new Multer(factorList.get(0), factorList.get(1));
        factorList.remove(0);
        factorList.remove(0);
        while (!factorList.isEmpty()) {
            multer.multiply(factorList.get(0));
            factorList.remove(0);
        }
        if (term.charAt(0) == '-') {
            multer.setSign('-');
        }
        return multer;
    }
    
    public Function parseFactor(ArrayList<Function> expList, String factor) {
        Pattern conPattern = Pattern.compile(reg.getConPattern());
        Pattern powPattern = Pattern.compile(reg.getPowPattern());
        Pattern sinPattern = Pattern.compile(reg.getSinPattern());
        Pattern cosPattern = Pattern.compile(reg.getCosPattern());
        Matcher conMatcher = conPattern.matcher(factor);
        Matcher powMatcher = powPattern.matcher(factor);
        Matcher sinMatcher = sinPattern.matcher(factor);
        Matcher cosMatcher = cosPattern.matcher(factor);
        if (conMatcher.matches()) {
            return new Constant(new BigInteger(conMatcher.group()));
        } else if (powMatcher.matches()) {
            Pattern p = Pattern.compile("x(\\*\\*(?<exp>[-\\+]?\\d+))?");
            Matcher m = p.matcher(factor);
            m.find();
            if (m.group("exp") != null) {
                return new Pow(new BigInteger(m.group("exp")));
            } else {
                return new Pow(new BigInteger("1"));
            }
        } else if (sinMatcher.matches()) {
            Pattern p = Pattern.compile("sin\\(x\\)(\\*\\*(?<exp>[-\\+]?\\d+))?");
            Matcher m = p.matcher(factor);
            m.find();
            if (m.group("exp") != null) {
                return new Sin(new BigInteger(m.group("exp")));
            } else {
                return new Sin(new BigInteger("1"));
            }
        } else if (cosMatcher.matches()) {
            Pattern p = Pattern.compile("cos\\(x\\)(\\*\\*(?<exp>[-\\+]?\\d+))?");
            Matcher m = p.matcher(factor);
            m.find();
            if (m.group("exp") != null) {
                return new Cos(new BigInteger(m.group("exp")));
            } else {
                return new Cos(new BigInteger("1"));
            }
        } else {
            Function tmp = expList.get(0);
            expList.remove(0);
            return tmp;
        }
    }
}
