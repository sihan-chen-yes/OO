import java.math.BigInteger;

public class Multer extends Function {
    private Function leftFunc;
    private Function rightFunc;
    
    public Multer(Function leftFunc,Function rightFunc) {
        this.leftFunc = leftFunc;
        this.rightFunc = rightFunc;
    }
    
    public void multiply(Function o) {
        this.leftFunc = new Multer(leftFunc,rightFunc);
        this.rightFunc = o;
    }
    
    public Function getDerivation() {
        Multer left = new Multer(leftFunc.getDerivation(),rightFunc);
        Multer right = new Multer(leftFunc,rightFunc.getDerivation());
        Adder adder = new Adder(left,right);
        if (getSign() == '-') {
            return new Multer(new Constant(new BigInteger("-1")),adder);
        }
        return adder;
    }
    
    @Override
    public String toString() {
        String ans = "";
        String left = leftFunc.toString();
        String right = rightFunc.toString();
        if (left.equals("0")) {
            ans = "0";
        } else if (right.equals("0")) {
            ans = "0";
        } else if (left.equals("1")) {
            ans = right;
        } else if (right.equals("1")) {
            ans = left;
        } else {
            ans = left + "*" + right;
        }
        if (getSign() == '-') {
            return "-1*" + ans;
        }
        return ans;
    }
}
