public class Regex {
    private final String inBlank = "\\d^[ |\t]+\\d$";
    private final String signBlank = "([\\+-][ |\t]*){2}[\\+-][ |\t]+\\d+";
    private final String mblank = "\\*[ |\t]+\\*";
    private final String sinBlank = "(s[ |\t]+in)|(si[ |\t]+n)|(s[ |\t]+i[ |\t]+n)";
    private final String cosBlank = "(c[ |\t]+os)|(co[ |\t]+s)|(c[ |\t]+o[ |\t]+s)";
    private final String conBlank = "\\([ |\t]*[+-][ |\t]+\\d+[ |\t]*\\)|\\*[ |\t]*[+-][ |\t]+\\d+";
    private final String triBlank = sinBlank + "|" + cosBlank;
    private final String wf = inBlank + "|" + signBlank + "|" + mblank + "|" + triBlank;
    private final String sign = "(\\+|-)";
    private final String space = "( |\t)";
    private final String spaces =  "(" + space + "{0,70}" + ")";
    private final String integer = "(\\d+)";
    private final String sinteger = "(" + sign + "?" + integer + ")";
    private final String exp = "(\\*\\*" + spaces + sinteger + ")";
    private final String sin = "(sin" + spaces +
            "E" + "(" + spaces + exp + ")?" + ")";
    private final String cos = "(cos" + spaces +
            "E" + "(" + spaces + exp + ")?" + ")";
    private final String tri = "(" + sin + "|" + cos + ")";
    private final String pow = "(x" + "(" + spaces + exp + ")?" + ")";
    private final String con = sinteger;
    private final String var = "(" + pow + "|" + tri + ")";
    private final String fac = "(" + var + "|" + con + "|" + "E" + ")";
    private final String plusTerm = "("
            + "(" + "(" + sign + spaces + ")?" + fac + ")"
            + "(" + spaces + "\\*" + spaces + fac + "){0,70}"
            + ")";
    private final String term = "(" + "(" + "?<=[\\+-]" + ")" + "("
            + "(" + "(" + sign   + spaces + ")?" + fac + ")"
            + "(" + spaces + "\\*" + spaces + fac + "){0,70}"
            + ")" + ")";
    private final String expression = "("
            + "(" + spaces + "(" + sign + spaces + ")?" + term + spaces + ")"
            + "(" + sign + spaces + term + spaces + "){0,70}"
            + ")";
    private final String newterm = "("
            + "(" + "(" + sign + spaces + ")?" + "F" + ")"
            + "(" + spaces + "\\*" + spaces + "F" + "){0,70}"
            + ")";
    private final String newexp = "("
            + "(" + spaces + "(" + sign + spaces + ")?" + "T" + spaces + ")"
            + "(" + sign + spaces + "T" + spaces + "){0,70}"
            + ")";
    
    public String getTermPattern() {
        return term;
    }
    
    public String getPlusTermPattern() {
        return plusTerm;
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
    
    public String getSpaces() {
        return spaces;
    }
    
    public String getNewterm() {
        return newterm;
    }
    
    public String getNewexp() {
        return newexp;
    }
    
    public String getWF() {
        return wf;
    }
}

