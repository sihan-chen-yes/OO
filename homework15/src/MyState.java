import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class MyState {
    private String name;
    private String id;
    private String parentId;
    private HashSet<MyState> subsequentState = new HashSet<>();
    private HashMap<MyState,ArrayList<MyTransition>> stateName2Transition = new HashMap<>();
    
    public MyState(UmlState umlState) {
        name = umlState.getName();
        id = umlState.getId();
        parentId = umlState.getParentId();
    }
    
    public MyState(UmlPseudostate umlPseudostate) {
        name = umlPseudostate.getName();
        id = umlPseudostate.getId();
        parentId = umlPseudostate.getParentId();
    }
    
    public MyState(UmlFinalState umlFinalState) {
        name = umlFinalState.getName();
        id = umlFinalState.getId();
        parentId = umlFinalState.getParentId();
    }
    
    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }
    
    public ArrayList<String> getTransitionTrigger(String stateName) {
        ArrayList<String> triggers = new ArrayList<>();
        Iterator iterator = stateName2Transition.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            MyState myState = (MyState) entry.getKey();
            String now = myState.getName();
            if (now != null && now.equals(stateName)) {
                ArrayList<MyTransition> transitions = (ArrayList<MyTransition>) entry.getValue();
                for (MyTransition myTransition : transitions) {
                    for (String event : myTransition.getEventList()) {
                        triggers.add(event);
                    }
                }
                break;
            }
        }
        return triggers;
    }
    
    public HashSet<MyState> getSubsequentState() {
        return subsequentState;
    }
    
    public void addSubsequentState(MyState myState) {
        subsequentState.add(myState);
    }
    
    public void addTransition(MyTransition myTransition,MyState state) {
        if (!stateName2Transition.containsKey(state)) {
            stateName2Transition.put(state,new ArrayList<>());
        }
        stateName2Transition.get(state).add(myTransition);
    }
    
    public String getParentId() {
        return parentId;
    }
    
    public Boolean hasTransition(MyState myState) {
        return stateName2Transition.containsKey(myState);
    }

    public HashMap<MyState,ArrayList<MyTransition>> getStateName2Transition() {
        return stateName2Transition;
    }
}
