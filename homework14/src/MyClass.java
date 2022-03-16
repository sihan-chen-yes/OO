import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;

import java.util.ArrayList;
import java.util.HashSet;

public class MyClass {
    private UmlClass umlClass;
    
    private MyClass father = null;
    private MyClass topFather = null;
    private boolean updatedTopFather = false;
    
    private ArrayList<UmlAttribute> attributes = new ArrayList<>();
    private int numOfAttribute = 0;
    private boolean updatedNumOfAttr = false;
    private ArrayList<MyOperation> operations = new ArrayList<>();
    private HashSet<MyInterface> interfaces = new HashSet<>();
    private boolean updatedInterface = false;
    private ArrayList<MyClass> associations = new ArrayList<>();
    private boolean hasUpdatedAsso = false;
    
    private String name;
    
    private Boolean dup = false;
    
    public MyClass(UmlClass umlClass) {
        this.umlClass = umlClass;
        name = umlClass.getName();
    }
    
    public void setFather(MyClass father) {
        this.father = father;
    }
    
    public MyClass getFather() {
        return father;
    }
    
    public void setTopFather(MyClass topFather) {
        this.topFather = topFather;
    }
    
    public MyClass getTopFather() {
        return topFather;
    }
    
    public void setUpdatedTopFather(boolean updatedTopFather) {
        this.updatedTopFather = updatedTopFather;
    }
    
    public boolean hasUpdatedTopFather() {
        return updatedTopFather;
    }
    
    public int getNumOfAttr() {
        return numOfAttribute;
    }
    
    public void setNumOfAttribute(int numOfAttribute) {
        this.numOfAttribute = numOfAttribute;
    }
    
    public ArrayList<MyOperation> getOperations() {
        return operations;
    }
    
    public void setUpdatedNumOfAttr(boolean updatedNumOfAttr) {
        this.updatedNumOfAttr = updatedNumOfAttr;
    }
    
    public boolean hasUpdatedNumOfAttr() {
        return updatedNumOfAttr;
    }
    
    public void addOperation(MyOperation operation) {
        operations.add(operation);
    }
    
    public HashSet<MyInterface> getInterfaces() {
        return interfaces;
    }
    
    public void addInterface(MyInterface myInterface) {
        interfaces.add(myInterface);
    }
    
    public void setUpdatedInterface(boolean updatedInterface) {
        this.updatedInterface = updatedInterface;
    }
    
    public boolean hasUpdatedInterface() {
        return updatedInterface;
    }
    
    public ArrayList<MyClass> getAssociations() {
        return associations;
    }
    
    public void setHasUpdatedAsso(boolean hasUpdatedAsso) {
        this.hasUpdatedAsso = hasUpdatedAsso;
    }
    
    public boolean hasUpdatedAsso() {
        return hasUpdatedAsso;
    }
    
    public void addAttributes(UmlAttribute attribute) {
        attributes.add(attribute);
        numOfAttribute += 1;
    }
    
    public ArrayList<UmlAttribute> getAttributes() {
        return attributes;
    }
    
    public String getName() {
        return name;
    }
    
    public void addAssociation(MyClass myclass) {
        if (!associations.contains(myclass)) {
            associations.add(myclass);
        }
    }
    
    public void updateAssociation(ArrayList<MyClass> asso) {
        associations = asso;
    }
    
    public void updateInterface(HashSet<MyInterface> interfaces) {
        this.interfaces = interfaces;
    }
    
    public Boolean getDup() {
        return dup;
    }
    
    public void setDup(Boolean dup) {
        this.dup = dup;
    }
}
