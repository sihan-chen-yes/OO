import java.util.ArrayList;
import java.util.List;

public class Bus {
    private List<Person> personList;

    public Bus() {
        personList = new ArrayList<>();
    }

    public void addPerson(Person person) {
        personList.add(person);
    }

    public void removePerson(Person person) {
        personList.remove(person);
    }

    public List<Person> getPersonList() {
        List<Person> newList = new ArrayList<>();
        for (Person per:personList) {
            Person p = per.clone();
            newList.add(p);
        }
        return newList;
        // TODO: return the current list of persons in the bus by implementing the deep clone
    }
}
