import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.format.UmlCollaborationInteraction;
import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.elements.UmlAttribute;

import java.util.HashMap;

public class MyUmlCollaborationInteraction implements UmlCollaborationInteraction {
    private HashMap<String,MyInteraction> name2Interaction;
    private HashMap<String,Boolean> interactionName2Valid;
    private HashMap<String, UmlAttribute> id2Attribute;
    private HashMap<String,MyParticipant> id2Participant;
    private HashMap<String,MyInteraction> id2Interaction;
    
    public MyUmlCollaborationInteraction(HashMap<String,MyInteraction> name2Interaction,
                                         HashMap<String,Boolean> interactionName2Valid,
                                         HashMap<String,UmlAttribute> id2Attribute,
                                         HashMap<String,MyParticipant> id2Participant,
                                         HashMap<String,MyInteraction> id2Interaction) {
        this.name2Interaction = name2Interaction;
        this.interactionName2Valid = interactionName2Valid;
        this.id2Attribute = id2Attribute;
        this.id2Participant = id2Participant;
        this.id2Interaction = id2Interaction;
    }
    
    public void check(String interactionName) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        if (!name2Interaction.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (!interactionName2Valid.get(interactionName)) {
            throw new InteractionDuplicatedException(interactionName);
        }
        return;
    }
    
    @Override
    public int getParticipantCount(String interactionName) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        check(interactionName);
        return name2Interaction.get(interactionName).getParticipantNum();
    }
    
    @Override
    public int getIncomingMessageCount(String interactionName, String lifelineName) throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        check(interactionName);
        MyInteraction myInteraction = name2Interaction.get(interactionName);
        return myInteraction.getIncomingMessageNum(lifelineName);
    }
    
    @Override
    public int getSentMessageCount(String interactionName, String lifelineName, MessageSort sort)
            throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        check(interactionName);
        MyInteraction myInteraction = name2Interaction.get(interactionName);
        return myInteraction.getSentMessageSortNum(lifelineName,
                sort.getOriginalString().toLowerCase());
    }
    
    public HashMap<String, MyParticipant> getId2Participant() {
        return id2Participant;
    }
    
    public HashMap<String, UmlAttribute> getId2Attribute() {
        return id2Attribute;
    }
    
    public HashMap<String, MyInteraction> getId2Interaction() {
        return id2Interaction;
    }
}
