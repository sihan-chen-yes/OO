import java.math.BigInteger;

public class Constant extends Function {
    private BigInteger con;
    
    public Constant(BigInteger con) {
        this.con = con;
    }
    
    public BigInteger getCon() {
        return con;
    }
    
    public void mergeCon(Constant con) {
        this.con = this.con.multiply(con.getCon());
    }
    
    public Term getDerivation() {
        Term term = new Term();
        term.multiply(new Constant(new BigInteger("0")));
        return term;
    }
    
    @Override
    public String toString() {
        String ans = con.toString();
        return ans;
    }
}
