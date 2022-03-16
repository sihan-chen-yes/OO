import java.util.ArrayList;
import java.util.HashMap;

public class PersonQueue {
    private HashMap<Integer,ArrayList<Person>> queue;
    private boolean isEnd;
    private int num;
    
    public PersonQueue() {
        queue = new HashMap<>();
        for (int i = 1;i <= 20; i++) {
            queue.put(new Integer(i),new ArrayList<Person>());
        }
        isEnd = false;
    }
    
    public  void addPerson(Person person) {
        Integer startFloor = new Integer(person.getFrom());
        queue.get(startFloor).add(person);
        num++;
    }
    
    public void setEnd(boolean end) {
        isEnd = end;
    }
    
    public boolean isEnd() {
        return isEnd;
    }
    
    public boolean isEmpty() {
        if (num == 0) {
            return true;
        }
        return false;
    }
    
    public boolean floorQueueEmpty(int from) {
        return queue.get(new Integer(from)).isEmpty();
    }
    
    public  Person getPerson(int from,int direction) {
        Integer startFloor = new Integer(from);
        ArrayList<Person> floorQueue = queue.get(startFloor);
        if (floorQueue.isEmpty()) {
            return null;
        }
        for (int i = 0; i < floorQueue.size(); i++) {
            Person person = floorQueue.get(i);
            if (person.getDirection() == direction) {
                floorQueue.remove(person);
                num--;
                return person;
            }
        }
        return null;
    }
}
