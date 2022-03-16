import java.math.BigInteger;

public class Sin extends Function {
    private BigInteger one = new BigInteger("1");
    private BigInteger zero = new BigInteger("0");
    private BigInteger exp;
    
    public Sin(BigInteger exp) {
        this.exp = exp;
    }
    
    public BigInteger getExp() {
        return exp;
    }
    
    public void mergeSin(Sin sin) {
        exp = exp.add(sin.getExp());
    }
    
    public Term getDerivation() {
        Term term = new Term();
        Sin sin = new Sin(exp.subtract(one));
        Cos cos = new Cos(one);
        Constant con = new Constant(exp);
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
        String ans = "sin(x)";
        if (exp.compareTo(one) == 0) {
            return ans;
        }
        return ans + "**" + exp.toString();
    }
}
