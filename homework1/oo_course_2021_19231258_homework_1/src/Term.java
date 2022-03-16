import java.math.BigInteger;

public class Term {
    private BigInteger coeff;
    private BigInteger degree;
    
    public Term(BigInteger coeff,BigInteger degree) {
        this.coeff = coeff;
        this.degree = degree;
    }
    
    public BigInteger getCoeff() {
        return coeff;
    }
    
    public BigInteger getDegree() {
        return degree;
    }
    
    public void mergeCoeff(BigInteger coeff) {
        this.coeff = this.coeff.add(coeff);
    }
    
    public void mergeTerm(Term term) {
        mergeCoeff(term.getCoeff());
    }
}
