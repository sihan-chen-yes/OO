package component;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.Map;

public class MyGroup implements Group {
    private int id;
    private HashMap<Integer, Person> people;
    
    private int valueSum;
    private int ageSum;
    private int ageMean;
    private int ageVar;
    private int ageSquareSum;
    private boolean needUpdateMean;
    private boolean needUpdateVar;
    
    public MyGroup(int id) {
        this.id = id;
        people = new HashMap<>();
        valueSum = 0;
        ageSum = 0;
        ageMean = 0;
        ageVar = 0;
        ageSquareSum = 0;
        needUpdateMean = false;
        needUpdateVar = false;
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Group) {
            return ((Group) obj).getId() == id;
        } else {
            return false;
        }
    }
    
    @Override
    public void addPerson(Person person) {
        people.put(person.getId(),person);
        updateAgeSum(person,1);
        updateAgeSquareSum(person,1);
        updateValueSum(person,1);
        needUpdateMean = true;
        needUpdateVar = true;
    }
    
    //update ageMean when add person
    public void updateAgeSum(Person person, int choice) {
        if (choice == 1) {
            ageSum += person.getAge();
        } else {
            ageSum -= person.getAge();
        }
    }
    
    //update ageSquareSum when add person
    public void updateAgeSquareSum(Person person,int choice) {
        int age = person.getAge();
        if (choice == 1) {
            ageSquareSum += age * age;
        } else {
            ageSquareSum -= age * age;
        }
    }
    
    //update valueSum when add person
    public void updateValueSum(Person person,int choice) {
        if (choice == 1) {
            for (Map.Entry<Integer,Person> entry:people.entrySet()) {
                if (entry.getValue().isLinked(person)) {
                    valueSum += 2 * entry.getValue().queryValue(person);
                }
            }
        } else {
            for (Map.Entry<Integer,Person> entry:people.entrySet()) {
                if (entry.getValue().isLinked(person)) {
                    valueSum -= 2 * entry.getValue().queryValue(person);
                }
            }
        }
    }
    
    //update valueSum when add relation
    public void addValueSum(int num) {
        valueSum += num;
    }
    
    @Override
    public boolean hasPerson(Person person) {
        if (people.containsValue(person)) {
            return true;
        }
        return false;
    }
    
    @Override
    public int getValueSum() {
        return valueSum;
    }
    
    @Override
    public int getAgeMean() {
        if (needUpdateMean) {
            if (getSize() == 0) {
                ageMean = 0;
                needUpdateMean = false;
            } else {
                ageMean = ageSum / people.size();
                needUpdateMean = false;
            }
        }
        return ageMean;
    }
    
    @Override
    public int getAgeVar() {
        if (needUpdateVar) {
            if (getSize() == 0) {
                ageVar = 0;
                needUpdateVar = false;
            } else {
                int mean = getAgeMean();
                int size = getSize();
                ageVar = (ageSquareSum - 2 * mean * ageSum + size * mean * mean) / size;
                needUpdateVar = false;
            }
        }
        return ageVar;
    }
    
    @Override
    public void delPerson(Person person) {
        people.remove(person.getId());
        updateAgeSum(person,-1);
        updateAgeSquareSum(person,-1);
        updateValueSum(person,-1);
        needUpdateMean = true;
        needUpdateVar = true;
    }
    
    @Override
    public int getSize() {
        return people.size();
    }
    
    public HashMap<Integer, Person> getPeople() {
        return people;
    }
}
