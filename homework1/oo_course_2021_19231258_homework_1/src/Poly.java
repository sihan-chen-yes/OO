import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class Poly {
    private HashMap<BigInteger,Term> map = new HashMap<>();
    
    public void addTerm(Term term) {
        BigInteger degree = term.getDegree();
        if (!map.isEmpty() && map.containsKey(degree)) {
            map.get(degree).mergeTerm(term);
        } else {
            map.put(degree,term);
        }
    }
    
    public void getDerivation() {
        Set degreeSet = map.keySet();
        Object[] arr = degreeSet.toArray();
        Arrays.sort(arr);
        for (Object key : arr) {
            BigInteger degree = map.get(key).getDegree();
            BigInteger coeff = map.get(key).getCoeff();
            coeff = coeff.multiply(degree);
            if (key.equals(BigInteger.valueOf(0))) {
                if (arr.length == 1) {
                    Term term = new Term(coeff,degree);
                    map.put(degree,term);
                } else {
                    map.remove(key);
                }
            } else {
                degree = degree.add(BigInteger.valueOf(-1));
                map.remove(key);
                Term term = new Term(coeff,degree);
                map.put(degree,term);
            }
        }
    }
    
    public String show() {
        Set degreeSet = map.keySet();
        Object[] arr = degreeSet.toArray();
        int j = 0;
        BigInteger zero = new BigInteger("0");
        BigInteger one = new BigInteger("1");
        String ans = "";
        while (j < arr.length - 1 && map.get(arr[j]).getCoeff().compareTo(zero) == -1) {
            j++;
        }
        Object tmp = arr[j];
        arr[j] = arr[0];
        arr[0] = tmp;
        for (int i = 0;i < arr.length;i++) {
            BigInteger coeff = map.get(arr[i]).getCoeff();
            BigInteger degree = map.get(arr[i]).getDegree();
            if (i == 0) {
                ans += String.valueOf(coeff);
                if (degree.compareTo(zero) == 0) {
                    continue;
                } else if (degree.compareTo(one) == 0) {
                    ans += "*x";
                } else {
                    ans = ans + "*x**" + String.valueOf(degree);
                }
            } else {
                if (coeff.compareTo(zero) == 0) {
                    continue;
                } else {
                    if (coeff.compareTo(zero) == 1) {
                        ans = ans + "+" + String.valueOf(coeff);
                    } else {
                        ans += String.valueOf(coeff);
                    }
                    if (degree.compareTo(zero) == 0) {
                        continue;
                    } else if (degree.compareTo(one) == 0) {
                        ans += "*x";
                    } else {
                        ans = ans + "*x**" + String.valueOf(degree);
                    }
                }
            }
        }
        return ans;
    }
    
}
