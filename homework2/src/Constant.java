import java.math.BigInteger;

public class Constant extends Function {
    private BigInteger con;
    
    public Constant(BigInteger con) {
        this.con = con;
    }
    
    public Function getDerivation() {
        return new Constant(new BigInteger("0"));
    }
    
    @Override
    public String toString() {
        String ans = con.toString();
        if (getSign() == '-') {
            ans = "-1*" + ans;
        }
        return ans;
    }
}
