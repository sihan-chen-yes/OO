import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression extends Function {
    private ArrayList<Function> addedList = new ArrayList<>();
    
    public void add(Function term) {
        if (term instanceof Expression) {
            for (Function t : ((Expression) term).getAddedList()) {
                this.addedList.add(t);
            }
        } else {
            this.addedList.add(term);
        }
    }
    
    public ArrayList<Function> getAddedList() {
        return addedList;
    }
    
    public Function getDerivation() {
        Expression derivation = new Expression();
        for (Function term : addedList) {
            Function termDeri = term.getDerivation();
            if (termDeri instanceof Expression) {
                for (Function t : ((Expression) termDeri).getAddedList()) {
                    derivation.add(t);
                }
            } else {
                derivation.add(termDeri);
            }
        }
        if (getSign() == '-') {
            derivation.setSign('-');
        }
        return derivation;
    }
    
    @Override
    public String toString() {
        String ans = "";
        if (addedList.isEmpty()) {
            return "0";
        }
        ans += addedList.get(0).toString();
        Pattern p = Pattern.compile("\\(*0\\)*");
        for (int i = 1;i < addedList.size();i++) {
            String tmp = addedList.get(i).toString();
            Matcher m = p.matcher(tmp);
            if (m.matches()) {
                continue;
            } else if (tmp.charAt(0) != '-') {
                ans = ans + "+" + tmp;
            } else {
                ans += tmp;
            }
        }
        ans = "(" + ans + ")";
        if (getSign() == '-') {
            return "-1*" + ans;
        }
        return ans;
    }
    
}
