public abstract class Function {
    public abstract Function getDerivation();
    
    private char sign = '+';
    
    public void setSign(char sign) {
        if (this.sign == sign) {
            this.sign = '+';
        } else {
            this.sign = '-';
        }
    }
    
    public char getSign() {
        return sign;
    }
}
