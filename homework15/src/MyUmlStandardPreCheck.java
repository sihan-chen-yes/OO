import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.format.UmlStandardPreCheck;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class MyUmlStandardPreCheck implements UmlStandardPreCheck {
    private MyUmlClassModelInteraction myUmlClassModelInteraction;
    private MyUmlStateChartInteraction myUmlStateChartInteraction;
    private MyUmlCollaborationInteraction myUmlCollaborationInteraction;
    
    public MyUmlStandardPreCheck(MyUmlClassModelInteraction myUmlClassModelInteraction,
                                 MyUmlStateChartInteraction myUmlStateChartInteraction,
                                 MyUmlCollaborationInteraction myUmlCollaborationInteraction) {
        this.myUmlClassModelInteraction = myUmlClassModelInteraction;
        this.myUmlStateChartInteraction = myUmlStateChartInteraction;
        this.myUmlCollaborationInteraction = myUmlCollaborationInteraction;
    }
    
    @Override
    public void checkForUml001() throws UmlRule001Exception {
        HashMap<String,MyClass> id2Class = myUmlClassModelInteraction.getId2Class();
        Iterator iterator = id2Class.entrySet().iterator();
        HashSet<AttributeClassInformation> ans = new HashSet<>();
        String name;
        while (iterator.hasNext()) {
            HashSet<String> nameSet = new HashSet();
            Map.Entry<String,MyClass> entry = (Map.Entry)iterator.next();
            MyClass myClass = entry.getValue();
            ArrayList<String> names = myClass.getAttrName();
            names.addAll(myClass.getOppositeEnds());
            for (int i = 0; i < names.size();i++) {
                name = names.get(i);
                if (name == null || nameSet.contains(name)) {
                    continue;
                } else {
                    for (int j = 0;j < names.size();j++) {
                        if (i != j && name.equals(names.get(j))) {
                            nameSet.add(name);
                            break;
                        }
                    }
                }
            }
            for (String dup : nameSet) {
                ans.add(new AttributeClassInformation(dup,myClass.getName()));
            }
        }
        if (!ans.isEmpty()) {
            throw new UmlRule001Exception(ans);
        }
    }
    
    @Override
    public void checkForUml002() throws UmlRule002Exception {
        HashMap<String,MyClass> id2Class = myUmlClassModelInteraction.getId2Class();
        Iterator iterator = id2Class.entrySet().iterator();
        HashSet ans = new HashSet<>();
        while (iterator.hasNext()) {
            Map.Entry<String,MyClass> entry = (Map.Entry)iterator.next();
            MyClass myClass = entry.getValue();
            if (ans.contains(myClass)) {
                continue;
            }
            if (classIsGeneralizationLoop(myClass)) {
                addLoop(myClass,ans);
            }
        }
        HashMap<String,MyInterface> id2Interface = myUmlClassModelInteraction.getId2Interface();
        iterator = id2Interface.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,MyInterface> entry = (Map.Entry)iterator.next();
            MyInterface myInterface = entry.getValue();
            if (interfaceIsGeneralizationLoop(myInterface)) {
                ans.add(myInterface.getUmlInterface());
            }
        }
        if (!ans.isEmpty()) {
            throw new UmlRule002Exception(ans);
        }
    }
    
    public void addLoop(MyClass myClass,HashSet set) {
        MyClass loop = myClass;
        set.add(myClass.getUmlClass());
        loop = loop.getFather();
        while (!loop.equals(myClass)) {
            set.add(loop.getUmlClass());
            loop = loop.getFather();
        }
        return;
    }
    
    public Boolean classIsGeneralizationLoop(MyClass myClass) {
        MyClass loop = myClass;
        ArrayList<MyClass> trace = new ArrayList<>();
        while (loop.getFather() != null && !trace.contains(loop.getFather())) {
            loop = loop.getFather();
            trace.add(loop);
            if (myClass.equals(loop)) {
                return true;
            }
        }
        return false;
    }
    
    public Boolean interfaceIsGeneralizationLoop(MyInterface myInterface) {
        ArrayList<MyInterface> work = new ArrayList<>();
        work.addAll(myInterface.getFatherInterfaces());
        ArrayList<MyInterface> trace = new ArrayList<>();
        while (!work.isEmpty()) {
            MyInterface now = work.get(0);
            work.remove(0);
            trace.add(now);
            if (now.equals(myInterface)) {
                return true;
            } else {
                for (MyInterface myInterface1 : now.getFatherInterfaces()) {
                    if (!trace.contains(myInterface1) && !work.contains(myInterface1)) {
                        work.add(myInterface1);
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void checkForUml003() throws UmlRule003Exception {
        HashMap<String,MyInterface> id2Interface = myUmlClassModelInteraction.getId2Interface();
        Iterator iterator = id2Interface.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,MyInterface> entry = (Map.Entry)iterator.next();
            MyInterface myInterface = entry.getValue();
            myUmlClassModelInteraction.updateFatherInterface(myInterface);
        }
        iterator = id2Interface.entrySet().iterator();
        HashSet<UmlInterface> ans = new HashSet<>();
        while (iterator.hasNext()) {
            Map.Entry<String,MyInterface> entry = (Map.Entry)iterator.next();
            MyInterface myInterface = entry.getValue();
            if (myInterface.getDup() == true) {
                ans.add(myInterface.getUmlInterface());
            }
        }
        if (!ans.isEmpty()) {
            throw new UmlRule003Exception(ans);
        }
    }
    
    @Override
    public void checkForUml004() throws UmlRule004Exception {
        HashMap<String,MyClass> id2Class = myUmlClassModelInteraction.getId2Class();
        Iterator iterator = id2Class.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,MyClass> entry = (Map.Entry)iterator.next();
            MyClass myClass = entry.getValue();
            myUmlClassModelInteraction.updateClassInterface(myClass);
        }
        iterator = id2Class.entrySet().iterator();
        HashSet<UmlClass> ans = new HashSet<>();
        while (iterator.hasNext()) {
            Map.Entry<String,MyClass> entry = (Map.Entry)iterator.next();
            MyClass myClass = entry.getValue();
            if (myClass.getDup() == true) {
                ans.add(myClass.getUmlClass());
            }
        }
        if (!ans.isEmpty()) {
            throw new UmlRule004Exception(ans);
        }
    }
    
    @Override
    public void checkForUml005() throws UmlRule005Exception {
        HashMap<String,MyInterface> interfaces = myUmlClassModelInteraction.getId2Interface();
        Iterator iterator = interfaces.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,MyInterface> entry = (Map.Entry)iterator.next();
            MyInterface myInterface = entry.getValue();
            if (myInterface.getName() == null) {
                throw new UmlRule005Exception();
            }
        }
        HashMap<String,MyClass> classes = myUmlClassModelInteraction.getId2Class();
        iterator = classes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,MyClass> entry = (Map.Entry)iterator.next();
            MyClass myClass = entry.getValue();
            if (myClass.getName() == null) {
                throw new UmlRule005Exception();
            }
        }
        HashMap<String,MyOperation> operations = myUmlClassModelInteraction.getId2Operation();
        iterator = operations.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,MyOperation> entry = (Map.Entry)iterator.next();
            MyOperation myOperation = entry.getValue();
            if (myOperation.getName() == null) {
                throw new UmlRule005Exception();
            }
        }
        ArrayList<UmlAttribute> attributes = myUmlClassModelInteraction.getAttributes();
        for (int i = 0;i < attributes.size();i++) {
            if (attributes.get(i).getName() == null) {
                throw new UmlRule005Exception();
            }
        }
        ArrayList<UmlParameter> parameters = myUmlClassModelInteraction.getParameters();
        for (int i = 0; i < parameters.size();i++) {
            if (parameters.get(i).getDirection().toUpperCaseString().equals("RETURN")) {
                continue;
            } else if (parameters.get(i).getName() == null) {
                throw new UmlRule005Exception();
            }
        }
    }
    
    @Override
    public void checkForUml006() throws UmlRule006Exception {
        HashMap<String,MyInterface> interfaces = myUmlClassModelInteraction.getId2Interface();
        Iterator iterator = interfaces.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,MyInterface> entry = (Map.Entry)iterator.next();
            MyInterface myInterface = entry.getValue();
            ArrayList<UmlAttribute> attributes = myInterface.getAttributes();
            for (UmlAttribute umlAttribute : attributes) {
                if (!umlAttribute.getVisibility().toUpperCaseString().equals("PUBLIC")) {
                    throw new UmlRule006Exception();
                }
            }
        }
    }
    
    @Override
    public void checkForUml007() throws UmlRule007Exception {
        HashMap<String,MyParticipant> id2Participant = myUmlCollaborationInteraction.
                getId2Participant();
        HashMap<String,UmlAttribute> id2Attr = myUmlCollaborationInteraction.
                getId2Attribute();
        HashMap<String,MyInteraction> id2Interaction = myUmlCollaborationInteraction.
                getId2Interaction();
        Iterator iterator = id2Participant.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,MyParticipant> entry = (Map.Entry)iterator.next();
            MyParticipant myParticipant = entry.getValue();
            String represent = myParticipant.getRepresent();
            if (represent == null || !id2Attr.containsKey(represent)) {
                throw new UmlRule007Exception();
            } else {
                UmlAttribute umlAttribute = id2Attr.get(represent);
                MyInteraction myInteraction = id2Interaction.get(myParticipant.getParentId());
                if (!umlAttribute.getParentId().equals(myInteraction.getParentId())) {
                    throw new UmlRule007Exception();
                }
            }
        }
    }
    
    @Override
    public void checkForUml008() throws UmlRule008Exception {
        HashMap<String,MyStateMachine> id2StateMachine = myUmlStateChartInteraction.
                getId2StateMachine();
        Iterator iterator = id2StateMachine.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,MyStateMachine> entry = (Map.Entry)iterator.next();
            MyStateMachine myStateMachine = entry.getValue();
            if (myStateMachine.getPseudostate() == null) {
                continue;
            } else {
                MyState myState = myStateMachine.getPseudostate();
                HashMap<MyState,ArrayList<MyTransition>> stateName2Transition =
                        myState.getStateName2Transition();
                if (stateName2Transition.size() > 1) {
                    throw new UmlRule008Exception();
                } else if (stateName2Transition.size() == 1) {
                    Iterator iterator1 = stateName2Transition.entrySet().iterator();
                    while (iterator1.hasNext()) {
                        Map.Entry<String,ArrayList<MyTransition>> entry1 =
                                (Map.Entry)iterator1.next();
                        ArrayList<MyTransition> transitions = entry1.getValue();
                        if (transitions.size() > 1) {
                            throw new UmlRule008Exception();
                        } else {
                            for (int i = 0; i < transitions.size();i++) {
                                MyTransition myTransition = transitions.get(i);
                                if (myTransition.getGuard() != null
                                        || !myTransition.getEventList().isEmpty()) {
                                    throw new UmlRule008Exception();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
