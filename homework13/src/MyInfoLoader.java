import com.oocourse.uml1.models.common.NamedType;
import com.oocourse.uml1.models.common.ReferenceType;
import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyInfoLoader {
    private HashMap<String,MyOperation> id2Operation;
    private HashMap<String,MyInterface> id2Interface;
    private HashMap<String, UmlAssociationEnd> id2AssociationEnd;
    
    private HashMap<String,MyClass> id2Class;
    private HashMap<String,MyClass> name2Class;
    private HashMap<String,Boolean> name2Valid;
    private HashMap<String,MyInterface> name2Interface;
    private ArrayList<UmlAssociation> associations;
    private ArrayList<UmlAttribute> attributes;
    private ArrayList<UmlGeneralization> generalizations;
    private ArrayList<UmlInterfaceRealization> interfaceRealizations;
    private ArrayList<UmlParameter> parameters;
    
    private ArrayList<String> typeList;
    
    public MyInfoLoader(HashMap<String,MyOperation> id2Operation,
                        HashMap<String,MyInterface> id2Interface,
                        HashMap<String,UmlAssociationEnd> id2AssociationEnd,
                        HashMap<String,MyClass> id2Class,
                        HashMap<String,MyClass> name2Class,
                        HashMap<String,Boolean> name2Valid,
                        HashMap<String,MyInterface> name2Interface,
                        ArrayList<UmlAssociation> associations,
                        ArrayList<UmlAttribute> attributes,
                        ArrayList<UmlGeneralization> generalizations,
                        ArrayList<UmlInterfaceRealization> interfaceRealizations,
                        ArrayList<UmlParameter> parameters,
                        ArrayList<String> typeList) {
        this.id2Operation = id2Operation;
        this.id2Interface = id2Interface;
        this.id2AssociationEnd = id2AssociationEnd;
        this.id2Class = id2Class;
        this.name2Class = name2Class;
        this.name2Valid = name2Valid;
        this.name2Interface = name2Interface;
        this.associations = associations;
        this.attributes = attributes;
        this.generalizations = generalizations;
        this.interfaceRealizations = interfaceRealizations;
        this.parameters = parameters;
        this.typeList = typeList;
    }
    
    public void loadElement(UmlElement element) {
        if (element instanceof UmlClass) {
            String classId = element.getId();
            String className = element.getName();
            MyClass myClass = new MyClass((UmlClass) element);
            id2Class.put(classId,myClass);
            if (name2Valid.containsKey(className)) {
                name2Valid.put(className,false);
            } else {
                name2Valid.put(className,true);
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
//            if (source.equals(target)) {
//                continue;
//            }
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
}
