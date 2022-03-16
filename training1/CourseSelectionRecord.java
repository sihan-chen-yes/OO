import java.util.Objects;

public class CourseSelectionRecord {
    private final Student stu;
    private final Course course;
    
    public CourseSelectionRecord(Student stu, Course course) {
        this.stu = stu;
        this.course = course;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(stu, course);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CourseSelectionRecord)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        CourseSelectionRecord that = (CourseSelectionRecord) obj;
        if (this.stu.equals(that.stu) && this.course.equals(that.course)) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return stu.toString() + " selects " + course.toString();
    }
}
