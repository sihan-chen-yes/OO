package exceptions;

import com.oocourse.spec3.exceptions.EqualMessageIdException;

import java.util.HashMap;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static int time = 0;
    private static HashMap<Integer,Integer> idErrorMap = new HashMap<>();
    private int id;
    
    public MyEqualMessageIdException(int id) {
        time += 1;
        if (idErrorMap.containsKey(id)) {
            int idErrorTime = idErrorMap.get(id) + 1;
            idErrorMap.put(id, idErrorTime);
        } else {
            idErrorMap.put(id,1);
        }
        this.id = id;
    }
    
    @Override
    public void print() {
        System.out.println(String.format("emi-%d, %d-%d",time,id,idErrorMap.get(id)));
    }
}
