import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Nest extends Function {
    private Function inner;
    private Function outer;
    private BigInteger one = new BigInteger("1");
    private BigInteger zero = new BigInteger("0");
    
    public Nest(Function inner, Function outer) {
        this.inner = inner;
        this.outer = outer;
    }
    
    public Term getDerivation() {
        Term term = new Term();
        if (outer instanceof Sin) {
            Constant con = new Constant(((Sin) outer).getExp());
            Nest nestSin = new Nest(inner, new Sin(((Sin) outer).getExp().subtract(one)));
            Nest nestCos = new Nest(inner, new Cos(one));
            Function innerDeri = inner.getDerivation();
            term.multiply(con);
            term.multiply(nestSin);
            term.multiply(nestCos);
            term.multiply(innerDeri);
        } else {
            Constant con = new Constant(((Cos) outer).getExp());
            Nest nestSin = new Nest(inner, new Sin(one));
            Nest nestCos = new Nest(inner, new Cos(((Cos) outer).getExp().subtract(one)));
            Function innerDeri = inner.getDerivation();
            term.multiply(con);
            term.multiply(nestSin);
            term.multiply(nestCos);
            term.multiply(innerDeri);
            term.setSign('-');
        }
        return term;
    }
    
    @Override
    public String toString() {
        String ans = "";
        String innerString = inner.toString();
        Pattern p = Pattern.compile("\\(*0\\)*");
        if (outer instanceof Sin || outer instanceof Cos) {
            Matcher m = p.matcher(innerString);
            if (outer instanceof Sin) {
                BigInteger exp = ((Sin) outer).getExp();
                if (m.matches()) {
                    if (exp.compareTo(zero) == 0) {
                        return "1";
                    } else {
                        return "0";
                    }
                } else {
                    if (exp.compareTo(zero) == 0) {
                        return "1";
                    } else if (exp.compareTo(one) == 0) {
                        ans = "sin(" + innerString + ")";
                    } else {
                        ans = "sin(" + innerString + ")**" + exp.toString();
                    }
                }
            } else {
                BigInteger exp = ((Cos) outer).getExp();
                if (m.matches()) {
                    return "1";
                } else {
                    if (exp.compareTo(zero) == 0) {
                        return "1";
                    } else if (exp.compareTo(one) == 0) {
                        ans = "cos(" + innerString + ")";
                    } else {
                        ans = "cos(" + innerString + ")**" + exp.toString();
                    }
                }
            }
        }
        return ans;
    }
}