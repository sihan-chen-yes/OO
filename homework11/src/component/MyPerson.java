package component;

import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private int fatherId;
    //PersonId to Person
    private HashMap<Integer,Person> acquaintance;
    //PersonId to Value
    private HashMap<Integer,Integer> value;
    private int money;
    private int socialValue;
    private ArrayList<Message> messages;
    
    public MyPerson(int id,String name,int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        fatherId = id;
        acquaintance = new HashMap<>();
        value = new HashMap<>();
        money = 0;
        socialValue = 0;
        messages = new ArrayList<>();
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
    
    @Override
    public void addSocialValue(int num) {
        socialValue += num;
    }
    
    @Override
    public int getSocialValue() {
        return socialValue;
    }
    
    @Override
    public ArrayList<Message> getMessages() {
        return messages;
    }
    
    @Override
    public ArrayList<Message> getReceivedMessages() {
        ArrayList<Message> receivedMessages = new ArrayList<>();
        int num = 0;
        for (int i = messages.size() - 1; i >= 0 && num < 4;--i,num++) {
            receivedMessages.add(messages.get(i));
        }
        return receivedMessages;
    }
    
    @Override
    public void addMoney(int num) {
        money += num;
    }
    
    @Override
    public int getMoney() {
        return money;
    }
    
    public HashMap<Integer,Person> getAcquaintance() {
        return acquaintance;
    }
}
