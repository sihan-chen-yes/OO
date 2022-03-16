public class Regex {
    private final String sign = "(\\+|-)";
    private final String space = "( |\t)";
    private final String spaces =  "(" + space + "{0,150}" + ")";
    private final String integer = "(\\d+)";
    private final String sinteger = "(" + sign + "?" + integer + ")";
    private final String exp = "(\\*\\*" + spaces + sinteger + ")";
    private final String sin = "(sin" + spaces +
            "\\(" + spaces + "x" + spaces + "\\)" + "(" + spaces + exp + ")?" + ")";
    private final String cos = "(cos" + spaces +
            "\\(" + spaces + "x" + spaces + "\\)" + "(" + spaces + exp + ")?" + ")";
    private final String tri = "(" + sin + "|" + cos + ")";
    private final String pow = "(x" + "(" + spaces + exp + ")?" + ")";
    private final String con = sinteger;
    private final String var = "(" + pow + "|" + tri + ")";
    private final String fac = "(" + var + "|" + con + "|" + "E" + ")";
    private final String term = "("
            + "(" + "(" + sign + spaces + ")?" + fac + ")"
            + "(" + spaces + "\\*" + spaces + fac + "){0,75}"
            + ")";
    private final String expression = "("
            + "(" + spaces + "(" + sign + spaces + ")?" + term + spaces + ")"
            + "(" + sign + spaces + term + spaces + "){0,75}"
            + ")";
    
    public String getTermPattern() {
        return term;
    }
    
    public String getFacPattern() {
        return fac;
    }
    
    public String getPowPattern() {
        return pow;
    }
    
    public String getConPattern() {
        return con;
    }
    
    public String getSignPattern() {
        return sign;
    }
    
    public String getSinPattern() {
        return sin;
    }
    
    public String getCosPattern() {
        return cos;
    }
    
    public String getExpression() {
        return expression;
    }
    
    public String getSpace() {
        return space;
    }
    
}

