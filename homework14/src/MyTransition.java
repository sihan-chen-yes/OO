import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;

public class MyTransition {
    private UmlTransition umlTransition;
    private String name;
    private String parentId;
    private ArrayList<String> eventList = new ArrayList<>();
    
    public MyTransition(UmlTransition umlTransition) {
        this.umlTransition = umlTransition;
        name = umlTransition.getName();
        parentId = umlTransition.getParentId();
    }
    
    public String getName() {
        return getName();
    }
    
    public void addEvent(String event) {
        eventList.add(event);
    }
    
    public ArrayList<String> getEventList() {
        return eventList;
    }
    
    public String getParentId() {
        return parentId;
    }
    
    public String getSource() {
        return umlTransition.getSource();
    }
    
    public String getTarget() {
        return umlTransition.getTarget();
    }
}
