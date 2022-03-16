package component;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import exceptions.MyEmojiIdNotFoundException;
import exceptions.MyEqualEmojiIdException;
import exceptions.MyEqualGroupIdException;
import exceptions.MyEqualMessageIdException;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyGroupIdNotFoundException;
import exceptions.MyMessageIdNotFoundException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class MyNetwork implements Network {
    private HashMap<Integer,Person> people;
    private HashMap<Integer, Group> groups;
    private HashMap<Integer, Message> messages;
    private HashMap<Integer,Integer> emojiIdToemojiHeat;
    private int block;
    
    public MyNetwork() {
        people = new HashMap<>();
        groups = new HashMap<>();
        messages = new HashMap<>();
        emojiIdToemojiHeat = new HashMap<>();
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
    public void addMessage(Message message)
            throws EqualMessageIdException, EqualPersonIdException, EmojiIdNotFoundException {
        if (messages.containsValue(message)) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message instanceof EmojiMessage
                && !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
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
                if (message instanceof RedEnvelopeMessage) {
                    int money = ((RedEnvelopeMessage) message).getMoney();
                    person1.addMoney(-1 * money);
                    person2.addMoney(money);
                } else if (message instanceof EmojiMessage) {
                    int emojiId = ((EmojiMessage) message).getEmojiId();
                    int heat = emojiIdToemojiHeat.get(emojiId);
                    emojiIdToemojiHeat.put(emojiId,heat + 1);
                }
                messages.remove(id);
                person2.getMessages().add(message);
            } else {
                MyMessage message = (MyMessage) getMessage(id);
                MyGroup myGroup = (MyGroup) message.getGroup();
                int socialValue = message.getSocialValue();
                if (message instanceof RedEnvelopeMessage) {
                    int money = ((RedEnvelopeMessage) message).getMoney();
                    MyPerson person = (MyPerson) message.getPerson1();
                    int size = message.getGroup().getSize();
                    int perMoney = money / size;
                    for (Map.Entry<Integer,Person> entry:myGroup.getPeople().entrySet()) {
                        entry.getValue().addSocialValue(socialValue);
                        if (entry.getValue().equals(person)) {
                            person.addMoney(-1 * perMoney * (size - 1));
                        } else {
                            entry.getValue().addMoney(perMoney);
                        }
                    }
                } else {
                    for (Map.Entry<Integer,Person> entry:myGroup.getPeople().entrySet()) {
                        entry.getValue().addSocialValue(socialValue);
                    }
                }
                if (message instanceof EmojiMessage) {
                    int emojiId = ((EmojiMessage) message).getEmojiId();
                    int heat = emojiIdToemojiHeat.get(emojiId);
                    emojiIdToemojiHeat.put(emojiId,heat + 1);
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
    
    @Override
    public boolean containsEmojiId(int id) {
        return emojiIdToemojiHeat.containsKey(id);
    }
    
    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (containsEmojiId(id)) {
            throw new MyEqualEmojiIdException(id);
        } else {
            emojiIdToemojiHeat.put(id,0);
        }
    }
    
    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getMoney();
        }
    }
    
    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!containsEmojiId(id)) {
            throw new MyEmojiIdNotFoundException(id);
        } else {
            return emojiIdToemojiHeat.get(id);
        }
    }
    
    @Override
    public int deleteColdEmoji(int limit) {
        Iterator iterator = emojiIdToemojiHeat.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if ((Integer)entry.getValue() < limit) {
                iterator.remove();
            }
        }
        iterator = messages.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (entry.getValue() instanceof EmojiMessage
                    && !containsEmojiId(((EmojiMessage) entry.getValue()).getEmojiId())) {
                iterator.remove();
            }
        }
        return emojiIdToemojiHeat.size();
    }
    
    @Override
    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        if (!containsMessage(id) || (containsMessage(id) && getMessage(id).getType() == 1)) {
            throw new MyMessageIdNotFoundException(id);
        } else {
            MyMessage message = (MyMessage) getMessage(id);
            MyPerson person1 = (MyPerson) message.getPerson1();
            MyPerson person2 = (MyPerson) message.getPerson2();
            if (find(person1.getId()) != find(person2.getId())) {
                return -1;
            }
            else {
                int socialValue = message.getSocialValue();
                person1.addSocialValue(socialValue);
                person2.addSocialValue(socialValue);
                if (message instanceof RedEnvelopeMessage) {
                    int money = ((RedEnvelopeMessage) message).getMoney();
                    person1.addMoney(-1 * money);
                    person2.addMoney(money);
                } else if (message instanceof EmojiMessage) {
                    int emojiId = ((EmojiMessage) message).getEmojiId();
                    int heat = emojiIdToemojiHeat.get(emojiId);
                    emojiIdToemojiHeat.put(emojiId,heat + 1);
                }
                messages.remove(id);
                person2.getMessages().add(message);
                return findMinPath(person1.getId(),person2.getId());
            }
        }
    }
    
    public int findMinPath(int id1,int id2) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        HashMap<Integer,Integer> id2Dis = new HashMap<>();
        queue.add(new Node(id1,0));
        id2Dis.put(id1,0);
        while (!queue.isEmpty()) {
            Node node = queue.remove();
            int dis = node.getDis();
            int id = node.getId();
            if (id == id2) {
                return dis;
            } else if (id2Dis.containsKey(id) && id2Dis.get(id) < dis) {
                continue;
            } else {
                MyPerson person = (MyPerson) getPerson(id);
                for (Map.Entry<Integer,Person> entry:person.getAcquaintance().entrySet()) {
                    int newId = entry.getValue().getId();
                    int newValue = entry.getValue().queryValue(person);
                    if (!id2Dis.containsKey(newId) || dis + newValue < id2Dis.get(newId)) {
                        id2Dis.put(newId, dis + newValue);
                        queue.add(new Node(newId, dis + newValue));
                    }
                }
            }
        }
        return -1;
    }
}
