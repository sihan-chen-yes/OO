import java.util.ArrayList;
import java.util.HashMap;

public class WaitingQueue {
    private HashMap<Integer,ArrayList<Person>> queue;
    private int size;
    private int loadedNum;
    private boolean isEnd;
    
    public void setEnd(boolean end) {
        isEnd = end;
    }
    
    public boolean isEnd() {
        return isEnd;
    }
    
    public WaitingQueue(int size) {
        queue = new HashMap<>();
        for (int i = 1;i <= 20; i++) {
            queue.put(new Integer(i),new ArrayList<Person>());
        }
        this.size = size;
        this.loadedNum = 0;
    }

    public boolean isEmpty() {
        return loadedNum == 0;
    }
    
    public boolean isFull() {
        return loadedNum == size;
    }
    
    public void addPerson(Person person) {
        Integer startFloor = new Integer(person.getFrom());
        queue.get(startFloor).add(person);
        loadedNum++;
    }
    
    public boolean floorQueueEmpty(int floor) {
        if (queue.get(new Integer(floor)).size() == 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean getInReq(int locFloor,int direction) {
        ArrayList<Person> floorQueue = queue.get(new Integer(locFloor));
        for (Person person:floorQueue) {
            if (person.getDirection() == direction) {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Person> getInQueue(int locFloor,int direction,int need) {
        ArrayList<Person> floorQueue = queue.get(new Integer(locFloor));
        ArrayList<Person> getInQueue = new ArrayList<>();
        for (int i = 0;i < floorQueue.size(); i++) {
            Person person = floorQueue.get(i);
            if (person.getDirection() == direction && getInQueue.size() < need) {
                getInQueue.add(person);
            }
        }
        for (Person person:getInQueue) {
            floorQueue.remove(person);
            loadedNum--;
        }
        return getInQueue;
    }
}
