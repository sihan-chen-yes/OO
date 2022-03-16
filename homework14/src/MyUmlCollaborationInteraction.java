import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.format.UmlCollaborationInteraction;
import com.oocourse.uml2.models.common.MessageSort;

import java.util.HashMap;

public class MyUmlCollaborationInteraction implements UmlCollaborationInteraction {
    private HashMap<String,MyInteraction> name2Interaction = new HashMap<>();
    private HashMap<String,Boolean> interactionName2Valid = new HashMap<>();
    
    public MyUmlCollaborationInteraction(HashMap<String,MyInteraction> name2Interaction,
                                         HashMap<String,Boolean> interactionName2Valid) {
        this.name2Interaction = name2Interaction;
        this.interactionName2Valid = interactionName2Valid;
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
}
