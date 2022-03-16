import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public final class Expression {
    private final Map<Integer, Term> expToTerm = new TreeMap<>();

    public Expression(String str) {
        String[] coeAndExp = str.split(" ");
        for (int i = 0; i < coeAndExp.length; ) {
            // 解析出系数 coe 和指数 exp
            int coe = Integer.parseInt(coeAndExp[i++]);
            int exp = Integer.parseInt(coeAndExp[i++]);
            // 添加到 TreeMap 中，将指数相同的项进行合并
            Term toMerge = expToTerm.get(exp);
            Term toPut;
            if (toMerge == null) {
                toPut = new Term(coe, exp);
            } else {
                toPut = new Term(coe + toMerge.getCoe(), exp);
            }
            expToTerm.put(exp, toPut);
        }
        // 去除系数为0的项
        expToTerm.entrySet().removeIf(entry -> entry.getValue().getCoe() == 0);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Expression)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        Expression that = (Expression) o;
        return Objects.equals(expToTerm, that.expToTerm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expToTerm);
    }

    @Override
    public String toString() {
        Set set = expToTerm.keySet();
        Object[] arr = set.toArray();
        Arrays.sort(arr);
        if (arr.length == 0) {
            return "0";
        }
        String ans = "";
        ans += expToTerm.get(arr[0]).toString();
        for (int i = 1;i < arr.length;i++) {
            String tmp = expToTerm.get(arr[i]).toString();
            if (!tmp.equals("")) {
                if (tmp.charAt(0) == '-') {
                    ans += tmp;
                } else {
                    ans += "+";
                    ans += tmp;
                }
            }
        }
        return ans;
        // TODO
    }
}
