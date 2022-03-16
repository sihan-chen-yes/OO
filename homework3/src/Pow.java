import java.math.BigInteger;

public class Pow extends Function {
    private BigInteger exp;
    private BigInteger zero = new BigInteger("0");
    private BigInteger one = new BigInteger("1");
    private BigInteger two = new BigInteger("2");
    
    public Pow(BigInteger exp) {
        this.exp = exp;
    }
    
    public BigInteger getExp() {
        return exp;
    }
    
    public void mergePow(Pow pow) {
        this.exp = exp.add(pow.getExp());
    }
    
    public Term getDerivation() {
        Term term = new Term();
        Constant con = new Constant(exp);
        Pow pow = new Pow(exp.subtract(one));
        term.multiply(con);
        term.multiply(pow);
        return term;
    }
    
    @Override
    public String toString() {
        if (exp.compareTo(zero) == 0) {
            return "1";
        }
        String ans = "x";
        if (exp.compareTo(one) == 0) {
            return ans;
        }
        if (exp.compareTo(two) == 0) {
            return ans + "*" + "x";
        }
        return ans + "**" + exp.toString();
    }
}
