import java.math.BigInteger;

public class Sin extends Function {
    private BigInteger one = new BigInteger("1");
    private BigInteger zero = new BigInteger("0");
    private BigInteger exp;
    
    public Sin(BigInteger exp) {
        this.exp = exp;
    }
    
    public Function getDerivation() {
        Multer multer = new Multer(
                            new Constant(exp),new Multer(
                                new Sin(exp.subtract(one)),new Cos(one)));
        return multer;
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
