import java.math.BigInteger;

public class Cos extends Function {
    private BigInteger one = new BigInteger("1");
    private BigInteger mone = new BigInteger("-1");
    private BigInteger zero = new BigInteger("0");
    private BigInteger exp;
    
    public Cos(BigInteger exp) {
        this.exp = exp;
    }
    
    public Function getDerivation() {
        Multer multer = new Multer(
                            new Constant(exp.multiply(mone)),new Multer(
                                new Cos(exp.subtract(one)),new Sin(one)));
        return multer;
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
