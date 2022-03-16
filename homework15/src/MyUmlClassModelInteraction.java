import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.OperationParamInformation;
import com.oocourse.uml3.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.format.UmlClassModelInteraction;
import com.oocourse.uml3.models.common.NamedType;
import com.oocourse.uml3.models.common.ReferenceType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlParameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyUmlClassModelInteraction implements UmlClassModelInteraction {

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

    public MyUmlClassModelInteraction(HashMap<String,MyOperation> id2Operation,
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

    public void check(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Class.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!name2Valid.get(className)) {
            throw new ClassDuplicatedException(className);
        }
        return;
    }
    
    @Override
    public int getClassCount() {
        return id2Class.size();
    }

    @Override
    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        check(className);
        MyClass myClass = name2Class.get(className);
        return myClass.getOperations().size();
    }

    @Override
    public int getClassAttributeCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        check(className);
        MyClass myClass = name2Class.get(className);
        return getNumOfAttr(myClass);
    }

    public int getNumOfAttr(MyClass myClass) {
        if (myClass.hasUpdatedNumOfAttr()) {
            return myClass.getNumOfAttr();
        } else {
            MyClass fatherClass = myClass.getFather();
            int numOfAttr;
            if (fatherClass != null) {
                numOfAttr = getNumOfAttr(fatherClass) + myClass.getNumOfAttr();
            } else {
                numOfAttr = myClass.getNumOfAttr();
            }
            myClass.setNumOfAttribute(numOfAttr);
            myClass.setUpdatedNumOfAttr(true);
            return numOfAttr;
        }
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String className,
                                                                String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        check(className);
        MyClass myClass = name2Class.get(className);
        ArrayList<MyOperation> operations = myClass.getOperations();
        HashMap<Visibility,Integer> visibility2Cnt = new HashMap<>();
        for (MyOperation operation : operations) {
            if (operation.getName().equals(operationName)) {
                Visibility visibility = operation.getVisibility();
                if (visibility2Cnt.containsKey(visibility)) {
                    int cnt = visibility2Cnt.get(visibility);
                    visibility2Cnt.put(visibility,cnt + 1);
                } else {
                    visibility2Cnt.put(visibility,1);
                }
            }
        }
        return visibility2Cnt;
    }

    @Override
    public Visibility getClassAttributeVisibility(String className,
                                                  String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        check(className);
        MyClass myClass = name2Class.get(className);
        boolean find = false;
        Visibility visibility = null;
        while (myClass != null) {
            ArrayList<UmlAttribute> attributes = myClass.getAttributes();
            for (UmlAttribute attribute : attributes) {
                if (attribute.getName().equals(attributeName)) {
                    if (find == false) {
                        find = true;
                    } else {
                        throw new AttributeDuplicatedException(className,attributeName);
                    }
                    visibility = attribute.getVisibility();
                }
            }
            myClass = myClass.getFather();
        }
        if (find == false) {
            throw new AttributeNotFoundException(className,attributeName);
        } else {
            return visibility;
        }
    }

    public boolean checkAttrType(UmlAttribute attribute) {
        if (attribute.getType() instanceof NamedType) {
            String name = ((NamedType) attribute.getType()).getName();
            if (typeList.contains(name)) {
                return true;
            }
            return false;
        } else {
            assert attribute.getType() instanceof ReferenceType;
            String typeId = ((ReferenceType) attribute.getType()).getReferenceId();
            if (id2Class.containsKey(typeId) || id2Interface.containsKey(typeId)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean checkParaType(MyOperation operation) {
        HashMap<String,Integer> type2Cnt = operation.getType2Cnt();
        String returnType = operation.getReturnType();
        if ((!name2Class.containsKey(returnType)
                && !name2Interface.containsKey(returnType))
                && !typeList.contains(returnType)
                && (returnType != null && !returnType.equals("void"))) {
            return false;
        }
        Iterator iterator = type2Cnt.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String type = (String) entry.getKey();
            if ((!name2Class.containsKey(type) && !name2Interface.containsKey(type))
                    && !typeList.contains(type)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getClassAttributeType(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException, AttributeNotFoundException,
            AttributeDuplicatedException, AttributeWrongTypeException {
        check(className);
        MyClass myClass = name2Class.get(className);
        boolean find = false;
        boolean error = false;
        String type = null;
        while (myClass != null) {
            ArrayList<UmlAttribute> attributes = myClass.getAttributes();
            for (UmlAttribute attribute : attributes) {
                if (attribute.getName().equals(attributeName)) {
                    if (find == false) {
                        find = true;
                    } else {
                        throw new AttributeDuplicatedException(className,attributeName);
                    }
                    if (error == false && !checkAttrType(attribute)) {
                        error = true;
                    }
                    if (error == false) {
                        if (attribute.getType() instanceof NamedType) {
                            type = ((NamedType) attribute.getType()).getName();
                        } else if (attribute.getType() instanceof ReferenceType) {
                            String typeId = ((ReferenceType) attribute.getType())
                                    .getReferenceId();
                            if (id2Class.containsKey(typeId)) {
                                type = id2Class.get(typeId).getName();
                            } else {
                                type = id2Interface.get(typeId).getName();
                            }
                        }
                    }
                }
            }
            myClass = myClass.getFather();
        }
        if (find == false) {
            throw new AttributeNotFoundException(className,attributeName);
        } else if (error == true) {
            throw new AttributeWrongTypeException(className,attributeName);
        } else {
            return type;
        }
    }

    @Override
    public List<OperationParamInformation> getClassOperationParamType(String className,
                                                                      String operationName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        check(className);
        MyClass myClass = name2Class.get(className);
        ArrayList<OperationParamInformation> ans = new ArrayList<>();
        ArrayList<MyOperation> operations = myClass.getOperations();
        ArrayList<MyOperation> cmp = new ArrayList<>();
        for (MyOperation operation : operations) {
            if (operation.getName().equals(operationName)) {
                if (!checkParaType(operation)) {
                    throw new MethodWrongTypeException(className,operationName);
                }
                String returnType = operation.getReturnType();
                OperationParamInformation opInfo;
                opInfo = new OperationParamInformation(
                        operation.getParaList(), returnType);
                ans.add(opInfo);
                cmp.add(operation);
            }
        }
        for (int i = 0; i < cmp.size();i++) {
            for (int j = 0; j < cmp.size();j++) {
                if (i == j) {
                    continue;
                } else {
                    if (cmp.get(i).isDuplicate(cmp.get(j))) {
                        throw new MethodDuplicatedException(className,operationName);
                    }
                }
            }
        }
        return ans;
    }

    @Override
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        check(className);
        MyClass myClass = name2Class.get(className);
        updateAsso(myClass);
        ArrayList<MyClass> assoList = myClass.getAssociations();
        ArrayList<String> ans = new ArrayList<>();
        for (MyClass asso : assoList) {
            ans.add(asso.getName());
        }
        return ans;
    }

    public void updateAsso(MyClass myClass) {
        if (myClass.hasUpdatedAsso()) {
            return;
        } else {
            MyClass fatherClass = myClass.getFather();
            if (fatherClass == null) {
                myClass.setHasUpdatedAsso(true);
                return;
            } else {
                updateAsso(fatherClass);
                ArrayList<MyClass> fatherAssoList = fatherClass.getAssociations();
                ArrayList<MyClass> myClassAssoList = myClass.getAssociations();
                ArrayList updatedAsso = (ArrayList) Stream.of(fatherAssoList,myClassAssoList)
                        .flatMap(Collection::stream).distinct().collect(Collectors.toList());
                myClass.updateAssociation(updatedAsso);
                myClass.setHasUpdatedAsso(true);
                return;
            }
        }
    }

    @Override
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        check(className);
        MyClass myClass = name2Class.get(className);
        updateTopFather(myClass);
        return myClass.getTopFather().getName();
    }

    public void updateTopFather(MyClass myClass) {
        if (myClass.hasUpdatedTopFather()) {
            return;
        } else {
            MyClass fatherClass = myClass.getFather();
            if (fatherClass == null) {
                myClass.setTopFather(myClass);
                myClass.setUpdatedTopFather(true);
                return;
            } else {
                updateTopFather(fatherClass);
                myClass.setTopFather(fatherClass.getTopFather());
                myClass.setUpdatedTopFather(true);
                return;
            }
        }
    }

    @Override
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        check(className);
        MyClass myClass = name2Class.get(className);
        updateClassInterface(myClass);
        HashSet<MyInterface> interfaces = myClass.getInterfaces();
        ArrayList<String> ans = new ArrayList<>();
        for (MyInterface myInterface : interfaces) {
            ans.add(myInterface.getName());
        }
        return ans;
    }

    public void updateFatherInterface(MyInterface myInterface) {
        if (myInterface.hasUpdatedFatherInterface()) {
            return;
        } else {
            ArrayList<MyInterface> fatherInterfaces = myInterface.getFatherInterfaces();
            if (fatherInterfaces.isEmpty()) {
                myInterface.setUpdatedFatherInterface(true);
                return;
            } else {
                ArrayList<MyInterface> allInterface = new ArrayList<>();
                for (MyInterface fatherInterface : fatherInterfaces) {
                    updateFatherInterface(fatherInterface);
                    ArrayList<MyInterface> tmpFatherInterface = fatherInterface
                            .getFatherInterfaces();
                    allInterface.addAll(tmpFatherInterface);
                    allInterface.add(fatherInterface);
                    if (fatherInterface.getDup() == true) {
                        myInterface.setDup(true);
                    }
                }
                HashSet<MyInterface> ans = new HashSet<>(allInterface);
                if (ans.size() != allInterface.size()) {
                    myInterface.setDup(true);
                }
                myInterface.setUpdatedFatherInterface(true);
                myInterface.updateFatherInterface(new ArrayList<>(ans));
                return;
            }
        }
    }

    public void updateClassInterface(MyClass myClass) {
        if (myClass.hasUpdatedInterface()) {
            return;
        } else {
            MyClass fatherClass = myClass.getFather();
            if (fatherClass == null) {
                HashSet<MyInterface> interfaces = myClass.getInterfaces();
                ArrayList<MyInterface> allInterface = new ArrayList<>();
                for (MyInterface myInterface : interfaces) {
                    updateFatherInterface(myInterface);
                    ArrayList<MyInterface> fatherInterfaces = myInterface.getFatherInterfaces();
                    allInterface.addAll(fatherInterfaces);
                    allInterface.add(myInterface);
                    if (myInterface.getDup() == true) {
                        myClass.setDup(true);
                    }
                }
                HashSet<MyInterface> set = new HashSet<>(allInterface);
                if (set.size() != allInterface.size()) {
                    myClass.setDup(true);
                }
                myClass.setUpdatedInterface(true);
                myClass.updateInterface(set);
                return;
            } else {
                updateClassInterface(fatherClass);
                HashSet<MyInterface> fatherSet = fatherClass.getInterfaces();
                if (fatherClass.getDup() == true) {
                    myClass.setDup(true);
                }
                ArrayList<MyInterface> allInterface = new ArrayList<>(fatherSet);
                HashSet<MyInterface> interfaces = myClass.getInterfaces();
                for (MyInterface myInterface : interfaces) {
                    updateFatherInterface(myInterface);
                    ArrayList<MyInterface> fatherInterfaces = myInterface.getFatherInterfaces();
                    allInterface.addAll(fatherInterfaces);
                    allInterface.add(myInterface);
                    if (myInterface.getDup() == true) {
                        myClass.setDup(true);
                    }
                }
                HashSet<MyInterface> ans = new HashSet<>(allInterface);
                if (ans.size() != allInterface.size()) {
                    myClass.setDup(true);
                }
                myClass.setUpdatedInterface(true);
                myClass.updateInterface(ans);
                return;
            }
        }
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        check(className);
        MyClass myClass = name2Class.get(className);
        ArrayList<AttributeClassInformation> ans = new ArrayList<>();
        while (myClass != null) {
            ArrayList<UmlAttribute> attributes = myClass.getAttributes();
            for (UmlAttribute attribute : attributes) {
                if (!attribute.getVisibility().toLowerCaseString().equals("private")) {
                    ans.add(new AttributeClassInformation(attribute.getName(),
                            myClass.getName()));
                }
            }
            myClass = myClass.getFather();
        }
        return ans;
    }
    
    public HashMap<String,MyInterface> getId2Interface() {
        return id2Interface;
    }
    
    public HashMap<String,MyClass> getId2Class() {
        return id2Class;
    }
    
    public ArrayList<UmlAttribute> getAttributes() {
        return attributes;
    }
    
    public HashMap<String,MyOperation> getId2Operation() {
        return id2Operation;
    }
    
    public ArrayList<UmlParameter> getParameters() {
        return parameters;
    }
}
