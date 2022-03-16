import java.math.BigInteger;

public class Cos extends Function {
    private BigInteger one = new BigInteger("1");
    private BigInteger zero = new BigInteger("0");
    private BigInteger mone = new BigInteger("-1");
    
    private BigInteger exp;
    
    public Cos(BigInteger exp) {
        this.exp = exp;
    }
    
    public BigInteger getExp() {
        return exp;
    }
    
    public void mergeCos(Cos cos) {
        exp = exp.add(cos.getExp());
    }
    
    public Term getDerivation() {
        Term term = new Term();
        Sin sin = new Sin(one);
        Cos cos = new Cos(exp.subtract(one));
        Constant con = new Constant(exp.multiply(mone));
        term.multiply(sin);
        term.multiply(cos);
        term.multiply(con);
        return term;
    }
    
    @Override
    public String toString() {
        if (exp.compareTo(zero) == 0) {
            return "1";
        }
        String ans = "cos(x)";
        if (exp.compareTo(one) == 0) {
            return ans;
        }
        return ans + "**" + exp.toString();
    }
}
