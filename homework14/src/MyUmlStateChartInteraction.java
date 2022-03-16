import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.format.UmlStateChartInteraction;

import java.util.HashMap;
import java.util.List;

public class MyUmlStateChartInteraction implements UmlStateChartInteraction {
    private HashMap<String,MyStateMachine> name2StateMachine = new HashMap<>();
    private HashMap<String,Boolean> stateMachineName2Valid = new HashMap<>();
    
    public MyUmlStateChartInteraction(HashMap<String,MyStateMachine> name2StateMachine,
                                      HashMap<String,Boolean> stateMachineName2Valid) {
        this.name2StateMachine = name2StateMachine;
        this.stateMachineName2Valid = stateMachineName2Valid;
    }
    
    public void check(String stateMachineName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!name2StateMachine.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        } else if (!stateMachineName2Valid.get(stateMachineName)) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        return;
    }
    
    @Override
    public int getStateCount(String stateMachineName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        check(stateMachineName);
        return name2StateMachine.get(stateMachineName).getStateNum();
    }
    
    @Override
    public int getSubsequentStateCount(String stateMachineName, String stateName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        check(stateMachineName);
        MyStateMachine myStateMachine = name2StateMachine.get(stateMachineName);
        return myStateMachine.getSubsequentCount(stateName);
    }
    
    @Override
    public List<String> getTransitionTrigger(String stateMachineName,
                                             String sourceStateName, String targetStateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        check(stateMachineName);
        MyStateMachine myStateMachine = name2StateMachine.get(stateMachineName);
        return myStateMachine.getTransitionTrigger(sourceStateName,targetStateName);
    }
}
