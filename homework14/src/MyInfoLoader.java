import com.oocourse.uml2.models.common.NamedType;
import com.oocourse.uml2.models.common.ReferenceType;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyInfoLoader {
    private HashMap<String,MyOperation> id2Operation = new HashMap<>();
    private HashMap<String,MyInterface> id2Interface = new HashMap<>();
    private HashMap<String, UmlAssociationEnd> id2AssociationEnd = new HashMap<>();
    
    private HashMap<String,MyClass> id2Class = new HashMap<>();
    private HashMap<String,MyClass> name2Class = new HashMap<>();
    private HashMap<String,Boolean> className2Valid = new HashMap<>();
    private HashMap<String,MyInterface> name2Interface = new HashMap<>();
    private ArrayList<UmlAssociation> associations = new ArrayList<>();
    private ArrayList<UmlAttribute> attributes = new ArrayList<>();
    private ArrayList<UmlGeneralization> generalizations = new ArrayList<>();
    private ArrayList<UmlInterfaceRealization> interfaceRealizations = new ArrayList<>();
    private ArrayList<UmlParameter> parameters = new ArrayList<>();
    
    private ArrayList<String> typeList = new ArrayList<>();
    
    private MyUmlClassModelInteraction myUmlClassModelInteraction = null;
    
    private HashMap<String,MyStateMachine> id2StateMachine = new HashMap<>();
    private HashMap<String,MyStateMachine> name2StateMachine = new HashMap<>();
    private HashMap<String,Boolean> stateMachineName2Valid = new HashMap<>();
    
    private HashMap<String,MyState> id2State = new HashMap<>();
    
    private HashMap<String,MyTransition> id2Transition = new HashMap<>();
    
    private HashMap<String, UmlRegion> id2Region = new HashMap<>();
    
    private ArrayList<UmlEvent> events = new ArrayList<>();
    private ArrayList<UmlPseudostate> pseudostates = new ArrayList<>();
    private ArrayList<UmlFinalState> finalStates = new ArrayList<>();
    
    private MyUmlStateChartInteraction myUmlStateChartInteraction = null;
    
    private HashMap<String,MyInteraction> id2Interaction = new HashMap<>();
    private HashMap<String,MyInteraction> name2Interaction = new HashMap<>();
    private HashMap<String,Boolean> interactionName2Valid = new HashMap<>();
    
    private HashMap<String,MyParticipant> id2Participant = new HashMap<>();
    
    private ArrayList<UmlMessage> messages = new ArrayList<>();
    
    private MyUmlCollaborationInteraction myUmlCollaborationInteraction = null;
    
    public void loadElement(UmlElement element) {
        if (element instanceof UmlClass) {
            String classId = element.getId();
            String className = element.getName();
            MyClass myClass = new MyClass((UmlClass) element);
            id2Class.put(classId,myClass);
            if (className2Valid.containsKey(className)) {
                className2Valid.put(className,false);
            } else {
                className2Valid.put(className,true);
                name2Class.put(className,myClass);
            }
        } else if (element instanceof UmlAttribute) {
            attributes.add((UmlAttribute) element);
        } else if (element instanceof UmlOperation) {
            id2Operation.put(element.getId(),new MyOperation((UmlOperation) element));
        } else if (element instanceof UmlParameter) {
            parameters.add((UmlParameter) element);
        } else if (element instanceof UmlAssociation) {
            associations.add((UmlAssociation) element);
        } else if (element instanceof UmlAssociationEnd) {
            id2AssociationEnd.put(element.getId(),(UmlAssociationEnd) element);
        } else if (element instanceof UmlGeneralization) {
            generalizations.add((UmlGeneralization) element);
        } else if (element instanceof UmlInterfaceRealization) {
            interfaceRealizations.add((UmlInterfaceRealization) element);
        } else if (element instanceof UmlInterface) {
            MyInterface myInterface = new MyInterface((UmlInterface) element);
            name2Interface.put(element.getName(),myInterface);
            id2Interface.put(element.getId(),myInterface);
        } else if (element instanceof UmlInteraction
                  || element instanceof UmlMessage
                  || element instanceof UmlLifeline) {
            loadCollaboration(element);
        } else {
            loadStateChart(element);
        }
    }
    
    public void loadCollaboration(UmlElement element) {
        if (element instanceof UmlInteraction) {
            String id = element.getId();
            String interactionName = element.getName();
            MyInteraction myInteraction = new MyInteraction((UmlInteraction) element);
            id2Interaction.put(id,myInteraction);
            if (interactionName2Valid.containsKey(interactionName)) {
                interactionName2Valid.put(interactionName,false);
            } else {
                interactionName2Valid.put(interactionName,true);
                name2Interaction.put(interactionName,myInteraction);
            }
        } else if (element instanceof UmlMessage) {
            messages.add((UmlMessage) element);
        } else if (element instanceof UmlLifeline) {
            String id = element.getId();
            String participantName = element.getName();
            MyParticipant myParticipant = new MyParticipant((UmlLifeline) element);
            id2Participant.put(id, myParticipant);
        }
    }
    
    public void loadStateChart(UmlElement element) {
        if (element instanceof UmlStateMachine) {
            String id = element.getId();
            String stateMachineName = element.getName();
            MyStateMachine myStateMachine = new MyStateMachine((UmlStateMachine) element);
            id2StateMachine.put(id,myStateMachine);
            if (stateMachineName2Valid.containsKey(stateMachineName)) {
                stateMachineName2Valid.put(stateMachineName,false);
            } else {
                stateMachineName2Valid.put(stateMachineName,true);
                name2StateMachine.put(stateMachineName,myStateMachine);
            }
        } else if (element instanceof UmlRegion) {
            id2Region.put(element.getId(),(UmlRegion) element);
        } else if (element instanceof UmlPseudostate) {
            pseudostates.add((UmlPseudostate) element);
        } else if (element instanceof UmlFinalState) {
            finalStates.add((UmlFinalState) element);
        } else if (element instanceof UmlState) {
            String id = element.getId();
            String name = element.getName();
            MyState myState = new MyState((UmlState) element);
            id2State.put(id,myState);
        } else if (element instanceof UmlTransition) {
            id2Transition.put(element.getId(),new MyTransition((UmlTransition) element));
        } else if (element instanceof UmlEvent) {
            events.add((UmlEvent) element);
        }
    }
    
    public void init() {
        typeList.add("byte");
        typeList.add("short");
        typeList.add("int");
        typeList.add("long");
        typeList.add("float");
        typeList.add("double");
        typeList.add("char");
        typeList.add("boolean");
        typeList.add("String");
    }
    
    public void loadAttribute() {
        for (UmlAttribute attribute : attributes) {
            MyClass myClass = id2Class.get(attribute.getParentId());
            if (myClass != null) {
                myClass.addAttributes(attribute);
            }
        }
        attributes.clear();
    }
    
    public void loadOperation() {
        Iterator iterator = id2Operation.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            MyOperation operation = (MyOperation) entry.getValue();
            String parentId = operation.getParentId();
            if (id2Class.containsKey(parentId)) {
                MyClass myClass = id2Class.get(parentId);
                myClass.addOperation(operation);
            } else if (id2Interface.containsKey(parentId)) {
                MyInterface myInterface = id2Interface.get(parentId);
                myInterface.addOperation(operation);
            }
        }
        id2Operation.clear();
    }
    
    public void loadParameter() {
        for (UmlParameter parameter : parameters) {
            String name = null;
            if (parameter.getType() instanceof NamedType) {
                name = ((NamedType) parameter.getType()).getName();
            } else if (parameter.getType() instanceof ReferenceType) {
                String typeId = ((ReferenceType) parameter.getType()).getReferenceId();
                if (id2Class.containsKey(typeId)) {
                    name = id2Class.get(typeId).getName();
                } else {
                    name = id2Interface.get(typeId).getName();
                }
            }
            String operationId = parameter.getParentId();
            MyOperation operation = id2Operation.get(operationId);
            if (operation != null) {
                operation.loadPara(name,
                        parameter.getDirection().toUpperCaseString());
            }
        }
        parameters.clear();
    }
    
    public void loadAssociation() {
        for (UmlAssociation association : associations) {
            String end1Id = association.getEnd1();
            String end2Id = association.getEnd2();
            UmlAssociationEnd end1 = id2AssociationEnd.get(end1Id);
            UmlAssociationEnd end2 = id2AssociationEnd.get(end2Id);
            String reference1 = end1.getReference();
            String reference2 = end2.getReference();
            MyClass myClass1 = id2Class.get(reference1);
            MyClass myClass2 = id2Class.get(reference2);
            if (myClass1 != null && myClass2 != null) {
                myClass1.addAssociation(myClass2);
                if (!reference1.equals(reference2)) {
                    myClass2.addAssociation(myClass1);
                }
            }
        }
        associations.clear();
    }
    
    public void loadGeneralization() {
        for (UmlGeneralization generalization : generalizations) {
            String source = generalization.getSource();
            String target = generalization.getTarget();
            if (source.equals(target)) {
                continue;
            }
            if (id2Class.containsKey(source)) {
                MyClass myClass = id2Class.get(source);
                MyClass father = id2Class.get(target);
                myClass.setFather(father);
            } else if (id2Interface.containsKey(source)) {
                MyInterface myInterface = id2Interface.get(source);
                if (id2Interface.containsKey(target)) {
                    myInterface.addFatherInterfaces(id2Interface.get(target));
                }
            }
        }
        generalizations.clear();
    }
    
    public void loadInterfaceRealization() {
        for (UmlInterfaceRealization interfaceRealization : interfaceRealizations) {
            String source = interfaceRealization.getSource();
            String target = interfaceRealization.getTarget();
            if (id2Class.containsKey(source)) {
                MyClass myClass = id2Class.get(source);
                if (id2Interface.containsKey(target)) {
                    MyInterface myInterface = id2Interface.get(target);
                    myClass.addInterface(myInterface);
                }
            }
        }
        interfaceRealizations.clear();
    }
    
    public void loadMessage() {
        for (UmlMessage message : messages) {
            String source = message.getSource();
            String target = message.getTarget();
            if (id2Participant.containsKey(source)) {
                id2Participant.get(source).addSentMessages(message);
            }
            if (id2Participant.containsKey(target)) {
                id2Participant.get(target).addIncomingMessages(message);
            }
        }
        messages.clear();
    }
    
    public void loadParticipant() {
        Iterator iterator = id2Participant.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            MyParticipant myParticipant = (MyParticipant) entry.getValue();
            String parentId = myParticipant.getParentId();
            if (id2Interaction.containsKey(parentId)) {
                id2Interaction.get(parentId).addParticipant(myParticipant);
            }
        }
        id2Participant.clear();
    }
    
    public void loadEvent() {
        for (UmlEvent event : events) {
            String name = event.getName();
            String parentId = event.getParentId();
            if (id2Transition.containsKey(parentId)) {
                MyTransition myTransition = id2Transition.get(parentId);
                myTransition.addEvent(name);
            }
        }
        events.clear();
    }
    
    public void loadState() {
        Iterator iterator = id2State.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            MyState myState = (MyState) entry.getValue();
            if (!id2Region.containsKey(myState.getParentId())) {
                continue;
            }
            String stateMachineId = id2Region.get(myState.getParentId()).getParentId();
            if (!id2StateMachine.containsKey(stateMachineId)) {
                continue;
            }
            MyStateMachine myStateMachine = id2StateMachine.get(stateMachineId);
            myStateMachine.addState(myState);
        }
        for (UmlPseudostate pseudostate : pseudostates) {
            if (!id2Region.containsKey(pseudostate.getParentId())) {
                continue;
            }
            String stateMachineId = id2Region.get(pseudostate.getParentId()).getParentId();
            if (!id2StateMachine.containsKey(stateMachineId)) {
                continue;
            }
            MyStateMachine myStateMachine = id2StateMachine.get(stateMachineId);
            MyState myState = new MyState(pseudostate);
            myStateMachine.setPseudostate(myState);
            id2State.put(myState.getId(),myState);
        }
        pseudostates.clear();
        for (UmlFinalState finalState : finalStates) {
            if (!id2Region.containsKey(finalState.getParentId())) {
                continue;
            }
            String stateMachineId = id2Region.get(finalState.getParentId()).getParentId();
            if (!id2StateMachine.containsKey(stateMachineId)) {
                continue;
            }
            MyStateMachine myStateMachine = id2StateMachine.get(stateMachineId);
            MyState myState = new MyState(finalState);
            myStateMachine.setFinalState(myState);
            id2State.put(myState.getId(),myState);
        }
        finalStates.clear();
        id2Region.clear();
    }
    
    public void loadTransition() {
        Iterator iterator = id2Transition.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            MyTransition myTransition = (MyTransition) entry.getValue();
            String source = myTransition.getSource();
            String target = myTransition.getTarget();
            MyState sourceState = id2State.get(source);
            MyState targetState = id2State.get(target);
            if (sourceState == null || targetState == null) {
                continue;
            }
            sourceState.addSubsequentState(targetState);
            sourceState.addTransition(myTransition,targetState);
        }
        id2Transition.clear();
        id2State.clear();
    }
    
    public MyUmlClassModelInteraction getMyUmlClassModelInteraction() {
        if (myUmlClassModelInteraction == null) {
            loadAttribute();
            loadParameter();
            loadOperation();
            loadGeneralization();
            loadInterfaceRealization();
            loadAssociation();
            init();
            myUmlClassModelInteraction = new MyUmlClassModelInteraction(
                    id2Operation,id2Interface,id2AssociationEnd,
                    id2Class,name2Class, className2Valid,name2Interface,
                    associations,attributes,generalizations,
                    interfaceRealizations,parameters,typeList);
        }
        return myUmlClassModelInteraction;
    }
    
    public MyUmlStateChartInteraction getMyUmlStateChartInteraction() {
        if (myUmlStateChartInteraction == null) {
            loadEvent();
            loadState();
            loadTransition();
            myUmlStateChartInteraction = new MyUmlStateChartInteraction(
                    name2StateMachine,
                    stateMachineName2Valid);
        }
        return myUmlStateChartInteraction;
    }
    
    public MyUmlCollaborationInteraction getMyUmlCollaborationInteraction() {
        if (myUmlCollaborationInteraction == null) {
            loadMessage();
            loadParticipant();
            myUmlCollaborationInteraction = new MyUmlCollaborationInteraction(
                    name2Interaction,
                    interactionName2Valid);
        }
        return myUmlCollaborationInteraction;
    }
}
