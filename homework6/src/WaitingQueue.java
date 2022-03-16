import java.util.ArrayList;
import java.util.HashMap;

public class WaitingQueue {
    private HashMap<Integer,ArrayList<Person>> queue;
    private int loadedNum;
    private boolean isEnd;
    
    public void setEnd(boolean end) {
        isEnd = end;
    }
    
    public boolean isEnd() {
        return isEnd;
    }
    
    public WaitingQueue() {
        queue = new HashMap<>();
        for (int i = 1;i <= 20; i++) {
            queue.put(new Integer(i),new ArrayList<>());
        }
        this.loadedNum = 0;
    }

    public boolean isEmpty() {
        return loadedNum == 0;
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
    
    public int getLoadedNum() {
        return loadedNum;
    }
    
    public ArrayList<Person> getInQueue(int locFloor, int direction, int need) {
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
