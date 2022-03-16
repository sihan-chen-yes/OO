import java.util.ArrayList;
import java.util.HashMap;

public class SimpleQueue {
    private ArrayList<Person> queue;
    private int num;
    private boolean isEnd;
    private boolean newElevator;
    
    public SimpleQueue() {
        queue = new ArrayList<>();
        num = 0;
        isEnd = false;
        newElevator = false;
    }
    
    public boolean hasNewElevator() {
        return newElevator;
    }
    
    public void setNewElevator(boolean newElevator) {
        this.newElevator = newElevator;
    }
    
    public Person getPerson() {
        if (queue.isEmpty()) {
            return null;
        }
        Person person = queue.get(0);
        queue.remove(person);
        num--;
        return person;
    }
    
    public void addPerson(Person person) {
        queue.add(person);
        num++;
    }
    
    public boolean isEnd() {
        return isEnd;
    }
    
    public void setEnd(boolean end) {
        this.isEnd = end;
    }
    
    public int getNum() {
        return num;
    }
    
    public void sort() {
        HashMap<Integer,ArrayList<Person>> sortMap = new HashMap<>();
        for (int i = 1;i <= 20;i++) {
            sortMap.put(i,new ArrayList<Person>());
        }
        for (Person person:queue) {
            sortMap.get(person.getFrom()).add(person);
        }
        ArrayList<Person> sortedQueue = new ArrayList<>();
        ArrayList<Person> temp;
        for (int i = 20;i > 0;i--) {
            temp = sortMap.get(new Integer(i));
            if (temp.size() != 0) {
                for (Person person:temp) {
                    sortedQueue.add(person);
                }
            }
        }
        queue = sortedQueue;
    }

}
