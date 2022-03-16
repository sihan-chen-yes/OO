import java.util.Objects;

public class Course {
    private final String id;
    private final String name;
    
    public Course(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Course)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Course that = (Course) obj;
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
