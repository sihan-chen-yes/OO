public class Adder extends Function {
    private Function leftFunc;
    private Function rightFunc;
    
    public Adder(Function leftFunc,Function rightFunc) {
        this.leftFunc = leftFunc;
        this.rightFunc = rightFunc;
    }
    
    public void add(Function o) {
        leftFunc = new Adder(leftFunc,rightFunc);
        rightFunc = o;
    }
    
    public Function getDerivation() {
        Adder adder = new Adder(leftFunc.getDerivation(),rightFunc.getDerivation());
        if (getSign() == '-') {
            adder.setSign('-');
        }
        return adder;
    }
    
    @Override
    public String toString() {
        String ans = "";
        String left = leftFunc.toString();
        String right = rightFunc.toString();
        if (left.equals("0")) {
            ans = right;
        } else if (right.equals("0")) {
            ans = left;
        } else {
            ans = "(" + left + "+" + right + ")";
        }
        if (getSign() == '-') {
            return "-1*" + ans;
        }
        return ans;
    }
}
