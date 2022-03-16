import java.util.ArrayList;

public class TransferManager {
    private final ArrayList<Integer> lowFloor;
    private final ArrayList<Integer> highFloor;
    private final ArrayList<Integer> lowCritical;
    private final ArrayList<Integer> highCritical;
    
    public TransferManager() {
        lowFloor = new ArrayList<>();
        for (int i = 1;i <= 3;i++) {
            lowFloor.add(i);
        }
        lowCritical = new ArrayList<>();
        for (int i = 4;i <= 6;i++) {
            lowCritical.add(i);
        }
        highCritical = new ArrayList<>();
        for (int i = 15;i <= 17;i++) {
            highCritical.add(i);
        }
        highFloor = new ArrayList<>();
        for (int i = 18;i <= 20;i++) {
            highFloor.add(i);
        }
    }
    
    public ArrayList<Person> genManagedPersons(Person person) {
        ArrayList<Person> managedPersons = new ArrayList<>();
        int id = person.getId();
        int from = person.getFrom();
        int to = person.getTo();
        if (Math.abs((to - from)) <= 4) {
            managedPersons = noTransfer(person);
        } else {
            if ((lowFloor.contains(from) && highFloor.contains(to))
                    || (lowFloor.contains(to) && highFloor.contains(from))) {
                person.setElevatorType("C");
                person.setValid(true);
                managedPersons.add(person);
            } else if (lowCritical.contains(from) && highFloor.contains(to)) {
                Person firstRoute = new Person(id,from,3);
                firstRoute.setElevatorType("A");
                firstRoute.setValid(true);
                Person nextRoute = new Person(id,3,to);
                nextRoute.setElevatorType("C");
                nextRoute.setPriority(true);
                firstRoute.setNextRoute(nextRoute);
                managedPersons.add(firstRoute);
                managedPersons.add(nextRoute);
            } else if (highFloor.contains(from) && lowCritical.contains(to)) {
                Person firstRoute = new Person(id,from,3);
                firstRoute.setElevatorType("C");
                firstRoute.setValid(true);
                Person nextRoute = new Person(id,3,to);
                nextRoute.setElevatorType("A");
                nextRoute.setPriority(true);
                firstRoute.setNextRoute(nextRoute);
                managedPersons.add(firstRoute);
                managedPersons.add(nextRoute);
            } else if (lowFloor.contains(from) && highCritical.contains(to)) {
                Person firstRoute = new Person(id,from,18);
                firstRoute.setElevatorType("C");
                firstRoute.setValid(true);
                Person nextRoute = new Person(id,18,to);
                nextRoute.setElevatorType("A");
                nextRoute.setPriority(true);
                firstRoute.setNextRoute(nextRoute);
                managedPersons.add(firstRoute);
                managedPersons.add(nextRoute);
            } else if (highCritical.contains(from) && lowFloor.contains(to)) {
                Person firstRoute = new Person(id,from,18);
                firstRoute.setElevatorType("A");
                firstRoute.setValid(true);
                Person nextRoute = new Person(id,18,to);
                nextRoute.setElevatorType("C");
                nextRoute.setPriority(true);
                firstRoute.setNextRoute(nextRoute);
                managedPersons.add(firstRoute);
                managedPersons.add(nextRoute);
            } else {
                managedPersons = genNormal(id,from,to);
            }
        }
        return managedPersons;
    }
    
    public ArrayList<Person> noTransfer(Person person) {
        ArrayList<Person> managedPersons = new ArrayList<>();
        int from = person.getFrom();
        int to = person.getTo();
        if (oddToOdd(from,to)) {
            person.setElevatorType("B");
        } else {
            person.setElevatorType("A");
        }
        person.setValid(true);
        managedPersons.add(person);
        return managedPersons;
    }
    
    public ArrayList<Person> genNormal(int id,int from,int to) {
        ArrayList<Person> managedPersons = new ArrayList<>();
        int direction;
        if (from > to) {
            direction = -1;
        } else {
            direction = 1;
        }
        if (oddToOdd(from,to)) {
            Person person = new Person(id,from,to);
            person.setElevatorType("B");
            person.setValid(true);
            managedPersons.add(person);
        } else if (oddToEven(from,to)) {
            Person firstRoute;
            Person nextRoute;
            if (direction == 1) {
                firstRoute = new Person(id,from,to - 1);
                nextRoute = new Person(id,to - 1,to);
            } else {
                firstRoute = new Person(id,from,to + 1);
                nextRoute = new Person(id,to + 1,to);
            }
            firstRoute.setElevatorType("B");
            firstRoute.setValid(true);
            nextRoute.setElevatorType("A");
            nextRoute.setPriority(true);
            firstRoute.setNextRoute(nextRoute);
            managedPersons.add(firstRoute);
            managedPersons.add(nextRoute);
        } else if (evenToOdd(from,to)) {
            Person firstRoute;
            Person nextRoute;
            if (direction == 1) {
                firstRoute = new Person(id,from,from + 1);
                nextRoute = new Person(id,from + 1,to);
            } else {
                firstRoute = new Person(id,from,from - 1);
                nextRoute = new Person(id,from - 1,to);
            }
            firstRoute.setElevatorType("A");
            firstRoute.setValid(true);
            nextRoute.setElevatorType("B");
            nextRoute.setPriority(true);
            firstRoute.setNextRoute(nextRoute);
            managedPersons.add(firstRoute);
            managedPersons.add(nextRoute);
        } else if (evenToEven(from,to)) {
            Person person = new Person(id,from,to);
            person.setElevatorType("A");
            person.setValid(true);
            managedPersons.add(person);
        } else {
            System.out.println("Wrong Never Happen!");
        }
        return managedPersons;
    }
    
    public boolean oddToOdd(int from,int to) {
        return (from % 2 == 1) && (to % 2 == 1);
    }
    
    public boolean oddToEven(int from,int to) {
        return (from % 2 == 1) && (to % 2 == 0);
    }
    
    public boolean evenToOdd(int from,int to) {
        return (from % 2 == 0) && (to % 2 == 1);
    }
    
    public boolean evenToEven(int from,int to) {
        return (from % 2 == 0) && (to % 2 == 0);
    }
}
