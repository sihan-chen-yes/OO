import java.util.ArrayList;

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
    
    public boolean hasValidReq() {
        for (Person person:queue) {
            if (person.isValid()) {
                return true;
            }
        }
        return false;
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
        Person person = null;
        Person priPerson = null;
        for (Person p:queue) {
            if (p.isValid() && p.isPriority()) {
                priPerson = p;
                break;
            }
            if (person == null && p.isValid()) {
                person = p;
            }
        }
        if (priPerson != null) {
            queue.remove(priPerson);
            num--;
            return priPerson;
        }
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
}
