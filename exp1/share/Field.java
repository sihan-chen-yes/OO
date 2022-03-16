public class Field implements Comparable<Field> {
    private String name;
    private int age;

    public Field() {
    }

    public Field(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    // code for comparable
    @Override
    public int compareTo(Field o) {
        // the first key : age 
        if (this.age > o.getAge()) {
            return 1;
        }
        if (this.age < o.getAge()) {
            return -1;
        }

        // the second key : name
        if (this.name.compareTo(o.getName()) > 0) {
            return 1;
        }
        if (this.name.compareTo(o.getName()) < 0) {
            return -1;
        }

        return 0;
    }
}  
