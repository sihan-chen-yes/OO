package exceptions;

import com.oocourse.spec3.exceptions.EqualEmojiIdException;

import java.util.HashMap;

public class MyEqualEmojiIdException extends EqualEmojiIdException {
    private static int time = 0;
    private static HashMap<Integer,Integer> idErrorMap = new HashMap<>();
    private int id;
    
    public MyEqualEmojiIdException(int id) {
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
        System.out.println(String.format("eei-%d, %d-%d",time,id,idErrorMap.get(id)));
    }
}
