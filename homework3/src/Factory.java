import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Factory{
    private Regex reg;
    
    public Factory(Regex reg) {
        this.reg = reg;
    }
    
    public Boolean checkBlankWF(String expression) {
        Pattern p = Pattern.compile(reg.getWF());
        Matcher m = p.matcher(expression);
        if (m.find()) {
            return true;
        }
        return false;
    }
    
    public Function generate(StringBuffer exp) throws Exception {
        if (checkBlankWF(exp.toString())) {
            throw new Exception();
        }
        StringBuffer expression = new StringBuffer(exp.toString().replaceAll(" |\t",""));
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
                return true;
            }
        }
        return false;
    }
    
    public StringBuffer getExpressionFactor(StringBuffer expression) throws Exception {
        Boolean flag = false;
        int leftNum = 0;
        int leftIndex = -1;
        int rightNum = 0;
        int rightIndex = -1;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                leftNum++;
                if (flag == false) {
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
            if (i == expression.length() - 1 && rightIndex == -1) {
                throw new Exception();
            }
        }
        StringBuffer exp = new StringBuffer(expression.substring(leftIndex + 1, rightIndex));
        expression.replace(leftIndex, rightIndex + 1, "E");
        return exp;
    }
    
    public Function parseAdder(ArrayList<Function> expList, String exp) throws Exception {
        ArrayList<Function> termList = new ArrayList<>();
        String expression = exp;
        String plusTerm = reg.getPlusTermPattern();
        Pattern pfirst = Pattern.compile(plusTerm);
        Matcher mfirst = pfirst.matcher(expression);
        mfirst.find();
        termList.add(parseMulter(expList, mfirst.group()));
        expression = expression.replaceFirst(plusTerm, "T");
        String term = reg.getTermPattern();
        Pattern p = Pattern.compile(term);
        Matcher m = p.matcher(expression);
        while (m.find()) {
            termList.add(parseMulter(expList, m.group()));
            expression = expression.replaceFirst(term, "T");
        }
        String newexp = reg.getNewexp();
        Pattern pjudge = Pattern.compile(newexp);
        Matcher mjudge = pjudge.matcher(expression);
        if (!mjudge.matches()) {
            throw new Exception();
        }
        Expression expre = new Expression();
        if (termList.size() == 1) {
            if (expression.charAt(0) == '-') {
                termList.get(0).setSign('-');
            }
            expre.add(termList.get(0));
            return expre;
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
        expre.add(termList.get(0));
        expre.add(termList.get(1));
        termList.remove(0);
        termList.remove(0);
        while (!termList.isEmpty()) {
            expre.add(termList.get(0));
            termList.remove(0);
        }
        return expre;
    }
    
    public Function parseMulter(ArrayList<Function> expList, String t) throws Exception {
        String termString = t;
        String factor = reg.getFacPattern();
        Pattern p = Pattern.compile(factor);
        Matcher m = p.matcher(termString);
        ArrayList<Function> factorList = new ArrayList<>();
        while (m.find()) {
            factorList.add(parseFactor(expList, m.group()));
            termString = termString.replaceFirst(factor, "F");
        }
        String newterm = reg.getNewterm();
        Pattern pjudge = Pattern.compile(newterm);
        Matcher mjudge = pjudge.matcher(termString);
        if (!mjudge.matches()) {
            throw new Exception();
        }
        Term term = new Term();
        if (factorList.size() == 1) {
            term.multiply(factorList.get(0));
            if (termString.charAt(0) == '-') {
                term.setSign('-');
            }
            return term;
        }
        term.multiply(factorList.get(0));
        term.multiply(factorList.get(1));
        factorList.remove(0);
        factorList.remove(0);
        while (!factorList.isEmpty()) {
            term.multiply(factorList.get(0));
            factorList.remove(0);
        }
        if (termString.charAt(0) == '-') {
            term.setSign('-');
        }
        return term;
    }
    
    public Function powFactor(String factor) throws Exception {
        Pattern p = Pattern.compile("x(\\*\\*(?<exp>[-\\+]?\\d+))?");
        Matcher m = p.matcher(factor);
        m.find();
        String tmp = m.group("exp");
        if (tmp != null) {
            if (Math.abs(Integer.valueOf(tmp)) > 50) {
                throw new Exception();
            }
            return new Pow(new BigInteger(tmp));
        } else {
            return new Pow(new BigInteger("1"));
        }
    }
    
    public Function sinFactor(ArrayList<Function> expList,String factor) throws Exception {
        Pattern p = Pattern.compile("sinE(\\*\\*(?<exp>[-\\+]?\\d+))?");
        Matcher m = p.matcher(factor);
        m.find();
        String tmp = m.group("exp");
        Function expression = expList.get(0);
        expList.remove(0);
        if (tmp != null) {
            if (Math.abs(Integer.valueOf(tmp)) > 50) {
                throw new Exception();
            }
            if (expression.toString().equals("x")) {
                return new Sin(new BigInteger(tmp));
            } else {
                return new Nest(expression,new Sin(new BigInteger(tmp)));
            }
        } else {
            if (expression.toString().equals("x")) {
                return new Sin(new BigInteger("1"));
            } else {
                return new Nest(expression,new Sin(new BigInteger("1")));
            }
        }
    }
    
    public Function cosFactor(ArrayList<Function> expList,String factor) throws Exception {
        Pattern p = Pattern.compile("cosE(\\*\\*(?<exp>[-\\+]?\\d+))?");
        Matcher m = p.matcher(factor);
        m.find();
        String tmp = m.group("exp");
        Function expression = expList.get(0);
        expList.remove(0);
        if (tmp != null) {
            if (Math.abs(Integer.valueOf(tmp)) > 50) {
                throw new Exception();
            }
            if (expression.toString().equals("x")) {
                return new Cos(new BigInteger(tmp));
            } else {
                return new Nest(expression,new Cos(new BigInteger(tmp)));
            }
        } else {
            if (expression.toString().equals("x")) {
                return new Cos(new BigInteger("1"));
            } else {
                return new Nest(expression,new Cos(new BigInteger("1")));
            }
        }
    }
    
    public Function parseFactor(ArrayList<Function> expList, String factor) throws Exception {
        Matcher conMatcher = Pattern.compile(reg.getConPattern()).matcher(factor);
        Matcher powMatcher = Pattern.compile(reg.getPowPattern()).matcher(factor);
        Matcher sinMatcher = Pattern.compile(reg.getSinPattern()).matcher(factor);
        Matcher cosMatcher = Pattern.compile(reg.getCosPattern()).matcher(factor);
        Matcher ematcher = Pattern.compile("E").matcher(factor);
        if (conMatcher.matches()) {
            return new Constant(new BigInteger(conMatcher.group()));
        } else if (powMatcher.matches()) {
            return powFactor(factor);
        } else if (sinMatcher.matches()) {
            return sinFactor(expList,factor);
        } else if (cosMatcher.matches()) {
            return cosFactor(expList,factor);
        } else if (ematcher.matches()) {
            Function expressionFac = expList.get(0);
            expList.remove(0);
            return expressionFac;
        } else {
            throw new Exception();
        }
    }
}
