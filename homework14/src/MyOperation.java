import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyOperation {
    private UmlOperation operation;
    
    private HashMap<String,Integer> type2Cnt = new HashMap<>();
    private ArrayList<String> paraList = new ArrayList<>();
    private String returnType = null;
    
    public MyOperation(UmlOperation operation) {
        this.operation = operation;
    }
    
    public boolean isDuplicate(MyOperation o) {
        assert o instanceof MyOperation;
        HashMap<String,Integer> otype2Cnt = o.getType2Cnt();
        String oreturnType = o.getReturnType();
        if (!(type2Cnt.size() == otype2Cnt.size())
                || (returnType == null && oreturnType != null)
                || (returnType != null && oreturnType == null)) {
            return false;
        } else if (returnType != null && oreturnType != null && !returnType.equals(oreturnType)) {
            return false;
        } else {
            Iterator iterator = type2Cnt.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                if (!otype2Cnt.containsKey(entry.getKey())
                        || entry.getValue() != otype2Cnt.get(entry.getKey())) {
                    return false;
                }
            }
            return true;
        }
    }
    
    public String getName() {
        return operation.getName();
    }
    
    public HashMap getType2Cnt() {
        return type2Cnt;
    }
    
    public String getReturnType() {
        return returnType;
    }
    
    public String getParentId() {
        return operation.getParentId();
    }
    
    public void loadPara(String type,String direction) {
        if (direction.equals("IN")) {
            if (type2Cnt.containsKey(type)) {
                int cnt = type2Cnt.get(type);
                type2Cnt.put(type,cnt + 1);
                paraList.add(type);
            } else {
                type2Cnt.put(type,1);
                paraList.add(type);
            }
        } else {
            returnType = type;
        }
    }
    
    public ArrayList<String> getParaList() {
        return paraList;
    }
    
    public Visibility getVisibility() {
        return operation.getVisibility();
    }
}
