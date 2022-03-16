import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyNetwork implements Network {
    private HashMap<Integer,Person> people;
    
    public MyNetwork() {
        people = new HashMap<>();
    }
    
    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
    }
    
    @Override
    public Person getPerson(int id) {
        if (people.containsKey(id)) {
            return people.get(id);
        }
        return null;
    }
    
    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        int personId = person.getId();
        if (people.containsKey(personId)) {
            throw new MyEqualPersonIdException(personId);
        } else {
            people.put(personId,person);
        }
    }
    
    @Override
    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!people.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1,id2);
        } else {
            MyPerson person1 = (MyPerson) people.get(id1);
            MyPerson person2 = (MyPerson) people.get(id2);
            person1.addAcquaintance(person2);
            person1.addValue(id2,value);
            person2.addAcquaintance(person1);
            person2.addValue(id1,value);
            merge(id1,id2);
        }
    }
    
    public int find(int id) {
        MyPerson person = (MyPerson) getPerson(id);
        if (person.getFatherId() == id) {
            return id;
        }
        int fatherId = find(person.getFatherId());
        person.setFatherId(fatherId);
        return fatherId;
    }
    
    public void merge(int id1,int id2) {
        int fatherId1 = find(id1);
        int fatherId2 = find(id2);
        if (id1 != id2) {
            MyPerson person = (MyPerson) getPerson(fatherId1);
            person.setFatherId(fatherId2);
        }
    }
    
    @Override
    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!people.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1,id2);
        } else {
            return getPerson(id1).queryValue(getPerson(id2));
        }
    }
    
    @Override
    public int compareName(int id1, int id2) throws PersonIdNotFoundException {
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!people.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            return getPerson(id1).getName().compareTo(getPerson(id2).getName());
        }
    }
    
    @Override
    public int queryPeopleSum() {
        return people.size();
    }
    
    @Override
    public int queryNameRank(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            int sum = 1;
            for (Map.Entry<Integer,Person> entry:people.entrySet()) {
                if (getPerson(id).getName().compareTo(entry.getValue().getName()) > 0) {
                    sum += 1;
                }
            }
            return sum;
        }
    }
    
    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!people.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            if (find(id1) == find(id2)) {
                return true;
            }
            return false;
        }
    }
    
    @Override
    public int queryBlockSum() {
        ArrayList<Integer> fatherIdList = new ArrayList<>();
        for (Map.Entry<Integer,Person> entry:people.entrySet()) {
            int fatherId = find(entry.getKey());
            if (!fatherIdList.contains(fatherId)) {
                fatherIdList.add(fatherId);
            }
        }
        return fatherIdList.size();
    }
}
