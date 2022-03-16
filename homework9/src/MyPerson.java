import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private int fatherId;
    private HashMap<Integer,Person> acquaintance;
    private HashMap<Integer,Integer> value;
    
    public MyPerson(int id,String name,int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        fatherId = id;
        acquaintance = new HashMap<>();
        value = new HashMap<>();
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    public int getFatherId() {
        return fatherId;
    }
    
    public void setFatherId(int fatherId) {
        this.fatherId = fatherId;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public int getAge() {
        return age;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Person) {
            return  ((Person) obj).getId() == id;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean isLinked(Person person) {
        if (person.getId() == id || acquaintance.containsKey(person.getId())) {
            return true;
        }
        return false;
    }
    
    @Override
    public int queryValue(Person person) {
        int personId = person.getId();
        if (acquaintance.containsKey(personId)) {
            return value.get(personId);
        }
        return 0;
    }
    
    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }
    
    public void addAcquaintance(Person person) {
        acquaintance.put(person.getId(),person);
    }
    
    public void addValue(int id,int weight) {
        value.put(id,weight);
    }
}
