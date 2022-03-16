import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class MyParticipant {
    private UmlLifeline umlLifeline;
    private String name;
    private String id;
    private ArrayList<UmlMessage> incomingMessages = new ArrayList<>();
    private HashMap<String,ArrayList<UmlMessage>> sentMessages = new HashMap<>();
    private String parentId;
    private String represent;
    
    public MyParticipant(UmlLifeline umlLifeline) {
        this.umlLifeline = umlLifeline;
        name = umlLifeline.getName();
        id = umlLifeline.getId();
        parentId = umlLifeline.getParentId();
        represent = umlLifeline.getRepresent();
    }
    
    public void addIncomingMessages(UmlMessage umlMessage) {
        incomingMessages.add(umlMessage);
    }
    
    public void addSentMessages(UmlMessage umlMessage) {
        String sort = umlMessage.getMessageSort().getOriginalString().toLowerCase();
        if (!sentMessages.containsKey(sort)) {
            sentMessages.put(sort,new ArrayList<UmlMessage>());
        }
        sentMessages.get(sort).add(umlMessage);
    }
    
    public String getParentId() {
        return parentId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }
    
    public int getIncomingNum() {
        return incomingMessages.size();
    }
    
    public int getSentSortNum(String sort) {
        ArrayList<UmlMessage> sortMessage = sentMessages.get(sort);
        if (sortMessage == null) {
            return 0;
        } else {
            return sortMessage.size();
        }
    }
    
    public String getRepresent() {
        return represent;
    }
}
