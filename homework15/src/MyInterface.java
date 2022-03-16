import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlInterface;

import java.util.ArrayList;

public class MyInterface {
    private UmlInterface umlInterface;
    private ArrayList<MyInterface> fatherInterfaces = new ArrayList<>();
    private ArrayList<MyOperation> operations = new ArrayList<>();
    private boolean updatedFatherInterface = false;
    private String name;
    private Boolean dup = false;
    private ArrayList<UmlAttribute> attributes = new ArrayList<>();
    
    public MyInterface(UmlInterface umlInterface) {
        this.umlInterface = umlInterface;
        name = umlInterface.getName();
    }
    
    public void addFatherInterfaces(MyInterface fatherInterface) {
        fatherInterfaces.add(fatherInterface);
    }
    
    public ArrayList<MyInterface> getFatherInterfaces() {
        return fatherInterfaces;
    }
    
    public void addOperation(MyOperation operation) {
        operations.add(operation);
    }
    
    public String getName() {
        return name;
    }
    
    public void setUpdatedFatherInterface(boolean updatedFatherInterface) {
        this.updatedFatherInterface = updatedFatherInterface;
    }
    
    public boolean hasUpdatedFatherInterface() {
        return updatedFatherInterface;
    }
    
    public void updateFatherInterface(ArrayList<MyInterface> fatherInterfaces) {
        this.fatherInterfaces = fatherInterfaces;
    }
    
    public Boolean getDup() {
        return dup;
    }
    
    public void setDup(Boolean dup) {
        this.dup = dup;
    }
    
    public UmlInterface getUmlInterface() {
        return umlInterface;
    }
    
    public void addAttr(UmlAttribute umlAttribute) {
        attributes.add(umlAttribute);
    }
    
    public ArrayList<UmlAttribute> getAttributes() {
        return attributes;
    }
}
