package exceptions;

import com.oocourse.spec3.exceptions.EqualRelationException;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private static int time = 0;
    private static HashMap<Integer,Integer> idErrorMap = new HashMap<>();
    private int id1;
    private int id2;
    
    public void updateErrorMap(int id) {
        if (idErrorMap.containsKey(id)) {
            int idErrorTime = idErrorMap.get(id) + 1;
            idErrorMap.put(id, idErrorTime);
        } else {
            idErrorMap.put(id,1);
        }
    }
    
    public MyEqualRelationException(int id1,int id2) {
        time += 1;
        if (id1 == id2) {
            updateErrorMap(id1);
            this.id1 = id1;
            this.id2 = id2;
        } else {
            updateErrorMap(id1);
            updateErrorMap(id2);
            if (id1 < id2) {
                this.id1 = id1;
                this.id2 = id2;
            } else {
                this.id1 = id2;
                this.id2 = id1;
            }
        }
    }
    
    @Override
    public void print() {
        System.out.println(String.format("er-%d, %d-%d, %d-%d",time,
                id1,idErrorMap.get(id1),id2,idErrorMap.get(id2)));
    }
}
