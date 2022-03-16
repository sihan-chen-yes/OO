import java.util.Objects;

public class Student {
    private final String id;
    private final String name;
    
    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Student)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Student that = (Student) obj;
        if (this.id.equals(that.id) && this.name.equals(that.name)) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return name;
    }
}
