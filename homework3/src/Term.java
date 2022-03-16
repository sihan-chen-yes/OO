import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Term extends Function {
    private HashMap<String,Function> facientMap = new HashMap<>();
    private ArrayList<Expression> expressionFac = new ArrayList<>();
    private ArrayList<Nest> nestFac = new ArrayList<>();
    
    public Term() {
        facientMap.put("Constant",new Constant(new BigInteger("1")));
    }
    
    public ArrayList<Expression> getExpressionFac() {
        return expressionFac;
    }
    
    public ArrayList<Nest> getNestFac() {
        return nestFac;
    }
    
    public HashMap<String, Function> getFacientMap() {
        return facientMap;
    }
    
    public void multiply(Function fac) {
        if (fac instanceof Expression) {
            expressionFac.add((Expression)fac);
        }  else if (fac instanceof Constant) {
            Constant con = (Constant)facientMap.get("Constant");
            con.mergeCon((Constant)fac);
        } else if (fac instanceof Pow) {
            if (facientMap.containsKey("Pow")) {
                Pow pow = (Pow) facientMap.get("Pow");
                pow.mergePow((Pow) fac);
            } else {
                facientMap.put("Pow",fac);
            }
        } else if (fac instanceof Sin) {
            if (facientMap.containsKey("Sin")) {
                Sin sin = (Sin)facientMap.get("Sin");
                sin.mergeSin((Sin)fac);
            } else {
                facientMap.put("Sin",fac);
            }
        } else if (fac instanceof Cos) {
            if (facientMap.containsKey("Cos")) {
                Cos cos = (Cos)facientMap.get("Cos");
                cos.mergeCos((Cos)fac);
            } else {
                facientMap.put("Cos",fac);
            }
        } else if (fac instanceof Nest) {
            nestFac.add((Nest)fac);
        } else {
            mergeTerm((Term)fac);
        }
    }
    
    public void mergeTerm(Term term) {
        HashMap map = term.getFacientMap();
        Set s = map.keySet();
        for (Object key:s) {
            if (map.containsKey(key)) {
                multiply((Function)map.get(key));
            }
        }
        for (Expression e:term.getExpressionFac()) {
            multiply(e);
        }
        for (Nest e:term.getNestFac()) {
            multiply(e);
        }
    }
    
    @Override
    public void setSign(char sign) {
        Constant con = (Constant)facientMap.get("Constant");
        if (sign == '-') {
            con.mergeCon(new Constant(new BigInteger("-1")));
        }
    }
    
    public Expression getDerivation() {
        Expression expression = new Expression();
        Set set = facientMap.keySet();
        for (Object key : set) {
            if (facientMap.get(key) instanceof Constant) {
                continue;
            }
            Term term = (Term)facientMap.get(key).getDerivation();
            for (Object k : set) {
                if (k.equals(key)) {
                    continue;
                }
                term.multiply(facientMap.get(k));
            }
            for (Expression exp : expressionFac) {
                term.multiply(exp);
            }
            for (Nest nest:nestFac) {
                term.multiply(nest);
            }
            expression.add(term);
        }
        for (int i = 0;i < expressionFac.size();i++) {
            Term term = new Term();
            term.multiply(expressionFac.get(i).getDerivation());
            for (int j = 0;j < expressionFac.size();j++) {
                if (j == i) {
                    continue;
                } else {
                    term.multiply(expressionFac.get(j));
                }
            }
            for (Object key : set) {
                term.multiply(facientMap.get(key));
            }
            for (Nest e : nestFac) {
                term.multiply(e);
            }
            expression.add(term);
        }
        for (int i = 0;i < nestFac.size();i++) {
            Term term = new Term();
            term.multiply(nestFac.get(i).getDerivation());
            for (int j = 0;j < nestFac.size();j++) {
                if (j == i) {
                    continue;
                } else {
                    term.multiply(nestFac.get(j));
                }
            }
            for (Object key : set) {
                term.multiply(facientMap.get(key));
            }
            for (Expression e : expressionFac) {
                term.multiply(e);
            }
            expression.add(term);
        }
        return expression;
    }
    
    public String facientMapToString() {
        String ans = "";
        if (facientMap.containsKey("Pow")) {
            Pow pow = (Pow)facientMap.get("Pow");
            String tmp = pow.toString();
            if (!tmp.equals("1")) {
                ans = tmp;
            }
        }
        if (facientMap.containsKey("Sin")) {
            Sin sin = (Sin)facientMap.get("Sin");
            String tmp = sin.toString();
            if (!tmp.equals("1")) {
                if (ans.isEmpty()) {
                    ans = tmp;
                } else {
                    ans = ans + "*" + tmp;
                }
            }
        }
        if (facientMap.containsKey("Cos")) {
            Cos cos = (Cos)facientMap.get("Cos");
            String tmp = cos.toString();
            if (!tmp.equals("1")) {
                if (ans.isEmpty()) {
                    ans = tmp;
                } else {
                    ans = ans + "*" + tmp;
                }
            }
        }
        return ans;
    }
    
    @Override
    public String toString() {
        Constant con = (Constant)facientMap.get("Constant");
        if (con.getCon().compareTo(new BigInteger("0")) == 0) {
            return "0";
        }
        String ans = facientMapToString();
        Pattern one = Pattern.compile("\\(*1\\)*");
        Pattern zero = Pattern.compile("\\(*0\\)*");
        for (Expression e : expressionFac) {
            String tmp = e.toString();
            Matcher mone = one.matcher(tmp);
            Matcher mzero = zero.matcher(tmp);
            if (mzero.matches()) {
                return "0";
            } else if (!mone.matches()) {
                if (ans.isEmpty()) {
                    ans = tmp;
                } else {
                    ans = ans + "*" + tmp;
                }
            }
        }
        for (Nest e : nestFac) {
            String tmp = e.toString();
            Matcher m = one.matcher(tmp);
            Matcher mzero = zero.matcher(tmp);
            if (mzero.matches()) {
                return "0";
            } else if (!m.matches()) {
                if (ans.isEmpty()) {
                    ans = tmp;
                } else {
                    ans = ans + "*" + tmp;
                }
            }
        }
        if (ans.isEmpty()) {
            ans = con.toString();
        } else if (con.getCon().compareTo(new BigInteger("1")) != 0) {
            ans = con.toString() + "*" + ans;
        }
        return ans;
    }
}
