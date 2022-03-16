import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.models.elements.UmlStateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MyStateMachine {
    private UmlStateMachine umlStateMachine;
    private String name;
    private MyState pseudostate = null;
    private MyState finalState = null;
    private HashMap<String, MyState> id2State = new HashMap<>();
    private HashMap<String, MyState> name2State = new HashMap<>();
    private HashMap<String, Boolean> stateName2Valid = new HashMap<>();
    
    public MyStateMachine(UmlStateMachine umlStateMachine) {
        this.umlStateMachine = umlStateMachine;
        name = umlStateMachine.getName();
    }
    
    public void addState(MyState myState) {
        String id = myState.getId();
        String name = myState.getName();
        id2State.put(id, myState);
        if (name2State.containsKey(name)) {
            stateName2Valid.put(name,false);
        } else if (!name2State.containsKey(name)) {
            name2State.put(name,myState);
            stateName2Valid.put(name,true);
        }
    }
    
    public void setPseudostate(MyState pseudostate) {
        this.pseudostate = pseudostate;
    }
    
    public void setFinalState(MyState finalState) {
        this.finalState = finalState;
    }
    
    public int getStateNum() {
        int count = id2State.size();
        if (pseudostate != null) {
            count++;
        }
        if (finalState != null) {
            count++;
        }
        return count;
    }
    
    public void check(String stateName) throws
            StateNotFoundException, StateDuplicatedException {
        if (!name2State.containsKey(stateName)) {
            throw new StateNotFoundException(name, stateName);
        } else if (!stateName2Valid.get(stateName)) {
            throw new StateDuplicatedException(name, stateName);
        }
        return;
    }
    
    public ArrayList<String> getTransitionTrigger(String sourceStateName,
                                                  String targetStateName) throws
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        check(sourceStateName);
        check(targetStateName);
        MyState sourceState = name2State.get(sourceStateName);
        MyState targetState = name2State.get(targetStateName);
        if (!sourceState.hasTransition(targetState)) {
            throw new TransitionNotFoundException(name, sourceStateName, targetStateName);
        } else {
            return sourceState.getTransitionTrigger(targetStateName);
        }
    }
    
    public int getSubsequentCount(String stateName) throws
            StateNotFoundException, StateDuplicatedException {
        check(stateName);
        MyState state = name2State.get(stateName);
        ArrayList<MyState> work = new ArrayList<>();
        HashSet<MyState> ans = new HashSet<>();
        HashSet<MyState> walked = new HashSet<>();
        work.add(state);
        while (!work.isEmpty()) {
            MyState now = work.get(0);
            work.remove(0);
            if (walked.contains(now)) {
                continue;
            } else {
                ans.addAll(now.getSubsequentState());
                work.addAll(now.getSubsequentState());
                walked.add(now);
            }
        }
        return ans.size();
    }
    
    public MyState getPseudostate() {
        return pseudostate;
    }
}

