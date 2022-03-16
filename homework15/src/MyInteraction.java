import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.models.elements.UmlInteraction;

import java.util.HashMap;

public class MyInteraction {
    private UmlInteraction umlInteraction;
    private String name;
    private HashMap<String,MyParticipant> id2Participant = new HashMap<>();
    private HashMap<String,MyParticipant> name2Participant = new HashMap<>();
    private HashMap<String,Boolean> participantName2Valid = new HashMap<>();
    private String parentId;
    
    public MyInteraction(UmlInteraction umlInteraction) {
        this.umlInteraction = umlInteraction;
        name = umlInteraction.getName();
        parentId = umlInteraction.getParentId();
    }
    
    public void addParticipant(MyParticipant participant) {
        String id = participant.getId();
        String name = participant.getName();
        id2Participant.put(id,participant);
        if (name2Participant.containsKey(name)) {
            participantName2Valid.put(name,false);
        } else if (!name2Participant.containsKey(name)) {
            name2Participant.put(name,participant);
            participantName2Valid.put(name,true);
        }
    }
    
    public int getParticipantNum() {
        return id2Participant.size();
    }
    
    public void check(String lifelineName) throws
            LifelineNotFoundException, LifelineDuplicatedException {
        if (!name2Participant.containsKey(lifelineName)) {
            throw new LifelineNotFoundException(name,lifelineName);
        } else if (!participantName2Valid.get(lifelineName)) {
            throw new LifelineDuplicatedException(name,lifelineName);
        }
        return;
    }
    
    public int getIncomingMessageNum(String lifelineName) throws
            LifelineNotFoundException, LifelineDuplicatedException {
        check(lifelineName);
        MyParticipant myParticipant = name2Participant.get(lifelineName);
        return myParticipant.getIncomingNum();
    }
    
    public int getSentMessageSortNum(String lifelineName, String sort) throws
            LifelineNotFoundException, LifelineDuplicatedException {
        check(lifelineName);
        MyParticipant myParticipant = name2Participant.get(lifelineName);
        return myParticipant.getSentSortNum(sort);
    }
    
    public String getParentId() {
        return parentId;
    }
}
