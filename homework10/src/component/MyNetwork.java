package component;

import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import exceptions.MyEqualGroupIdException;
import exceptions.MyEqualMessageIdException;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyGroupIdNotFoundException;
import exceptions.MyMessageIdNotFoundException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyNetwork implements Network {
    private HashMap<Integer,Person> people;
    private HashMap<Integer, Group> groups;
    private HashMap<Integer, Message> messages;
    private int block;
    
    public MyNetwork() {
        people = new HashMap<>();
        groups = new HashMap<>();
        messages = new HashMap<>();
        block = 0;
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
            block++;
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
            for (Map.Entry<Integer, Group> entry:groups.entrySet()) {
                MyGroup myGroup = (MyGroup) entry.getValue();
                if (myGroup.hasPerson(person1) && myGroup.hasPerson(person2)) {
                    myGroup.addValueSum(2 * value);
                }
            }
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
    
    //if fatherId is not the same change one to the other
    //mind that the changed side's children's fatherId is not changed now
    // but when find is called it will change
    public void merge(int id1,int id2) {
        int fatherId1 = find(id1);
        int fatherId2 = find(id2);
        if (fatherId1 != fatherId2) {
            MyPerson person = (MyPerson) getPerson(fatherId1);
            person.setFatherId(fatherId2);
            block--;
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
        return block;
    }
    
    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsValue(group)) {
            throw new MyEqualGroupIdException(group.getId());
        } else {
            groups.put(group.getId(),group);
        }
    }
    
    @Override
    public Group getGroup(int id) {
        if (groups.containsKey(id)) {
            return groups.get(id);
        }
        return null;
    }
    
    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else {
            MyGroup group = (MyGroup) getGroup(id2);
            if (group.getSize() < 1111) {
                group.addPerson(getPerson(id1));
            }
        }
    }
    
    @Override
    public int queryGroupSum() {
        return groups.size();
    }
    
    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return getGroup(id).getSize();
        }
    }
    
    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return getGroup(id).getValueSum();
        }
    }
    
    @Override
    public int queryGroupAgeMean(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return getGroup(id).getAgeMean();
        }
    }
    
    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return getGroup(id).getAgeVar();
        }
    }
    
    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else {
            MyGroup group = (MyGroup) getGroup(id2);
            group.delPerson(getPerson(id1));
        }
    }
    
    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }
    
    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        if (messages.containsValue(message)) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        } else {
            messages.put(message.getId(),message);
        }
    }
    
    @Override
    public Message getMessage(int id) {
        if (containsMessage(id)) {
            return messages.get(id);
        }
        return null;
    }
    
    @Override
    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        } else if (getMessage(id).getType() == 0
                && !(getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()))) {
            throw new MyRelationNotFoundException(getMessage(id).getPerson1().getId(),
                    getMessage(id).getPerson2().getId());
        } else if (getMessage(id).getType() == 1
                && !(getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1()))) {
            throw new MyPersonIdNotFoundException(getMessage(id).getPerson1().getId());
        } else {
            if (getMessage(id).getType() == 0) {
                MyMessage message = (MyMessage) getMessage(id);
                MyPerson person1 = (MyPerson) message.getPerson1();
                MyPerson person2 = (MyPerson) message.getPerson2();
                int socialValue = message.getSocialValue();
                person1.addSocialValue(socialValue);
                person2.addSocialValue(socialValue);
                messages.remove(id);
                person2.getMessages().add(message);
            } else {
                MyMessage message = (MyMessage) getMessage(id);
                MyGroup myGroup = (MyGroup) message.getGroup();
                int socialValue = message.getSocialValue();
                for (Map.Entry<Integer,Person> entry:myGroup.getPeople().entrySet()) {
                    entry.getValue().addSocialValue(socialValue);
                }
                messages.remove(id);
            }
        }
    }
    
    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getSocialValue();
        }
    }
    
    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getReceivedMessages();
        }
    }
}
