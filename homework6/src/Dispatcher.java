import java.util.ArrayList;

public class Dispatcher extends Thread {
    private final SimpleQueue totalQueue;
    private final ArrayList<PersonQueue> personQueues;
    private final ArrayList<Elevator> elevators;
    private String mode;
    
    public Dispatcher(SimpleQueue totalQueue,ArrayList<PersonQueue> personQueues,
                      ArrayList<Elevator> elevators,
                      String mode) {
        this.totalQueue = totalQueue;
        this.personQueues = personQueues;
        this.elevators = elevators;
        this.mode = mode;
    }
    
    
    public PersonQueue getEmptyPersonQueue() {
        PersonQueue personQueue;
        for (int i = 0;i < personQueues.size();i++) {
            personQueue = personQueues.get(i);
            if (personQueue.isEmpty()) {
                return personQueue;
            }
        }
        return null;
    }
    
    public ArrayList<PersonQueue> getSuitablePersonQueue(Person person) {
        int direction = person.getDirection();
        int loc = person.getFrom();
        ArrayList<PersonQueue> suitablePersonQueue = new ArrayList<>();
        Elevator elevator;
        for (int i = 0;i < personQueues.size();i++) {
            elevator = elevators.get(i);
            if (direction == elevator.getDirection()) {
                if (direction == 1 && loc > elevator.getLocFloor()) {
                    suitablePersonQueue.add(personQueues.get(i));
                } else if (direction == -1 && loc < elevator.getLocFloor()) {
                    suitablePersonQueue.add(personQueues.get(i));
                }
            }
        }
        if (suitablePersonQueue.size() == 0) {
            return personQueues;
        }
        return suitablePersonQueue;
    }
    
    public PersonQueue getMinPersonQueue(ArrayList<PersonQueue> suitablePersonQueue) {
        int num = suitablePersonQueue.get(0).getNum();
        PersonQueue minQueue = suitablePersonQueue.get(0);
        PersonQueue personQueue;
        for (int i = 1;i < suitablePersonQueue.size();i++) {
            personQueue = suitablePersonQueue.get(i);
            if (personQueue.getNum() < num) {
                num = personQueue.getNum();
                minQueue = personQueue;
            }
        }
        return minQueue;
    }
    
    public void randomMode() {
        synchronized (totalQueue) {
            PersonQueue personQueue;
            Person person;
            while (totalQueue.getNum() != 0) {
                person = totalQueue.getPerson();
                personQueue = getEmptyPersonQueue();
                if (personQueue != null) {
                    synchronized (personQueue) {
                        personQueue.addPerson(person);
                    }
                    continue;
                } else {
                    personQueue = getMinPersonQueue(getSuitablePersonQueue(person));
                    synchronized (personQueue) {
                        personQueue.addPerson(person);
                    }
                }
            }
            for (int i = 0;i < personQueues.size();i++) {
                personQueue = personQueues.get(i);
                synchronized (personQueue) {
                    personQueue.notifyAll();
                }
            }
        }
    }
    
    public void morningMode() {
        synchronized (totalQueue) {
            PersonQueue personQueue;
            Person person;
            int count;
            while (totalQueue.getNum() < 6) {
                if (totalQueue.isEnd()) {
                    break;
                } else {
                    try {
                        totalQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (totalQueue.isEnd()) {
                while (totalQueue.getNum() != 0) {
                    personQueue = getMinPersonQueue(personQueues);
                    count = 0;
                    synchronized (personQueue) {
                        while (count < 6 && totalQueue.getNum() != 0) {
                            person = totalQueue.getPerson();
                            personQueue.addPerson(person);
                            count++;
                        }
                        personQueue.notifyAll();
                    }
                }
            } else {
                while (totalQueue.getNum() >= 6) {
                    personQueue = getMinPersonQueue(personQueues);
                    count = 0;
                    synchronized (personQueue) {
                        while (count < 6 && totalQueue.getNum() != 0) {
                            person = totalQueue.getPerson();
                            personQueue.addPerson(person);
                            count++;
                        }
                        personQueue.notifyAll();
                    }
                }
            }
        }
    }
    
    public void nightMode() {
        synchronized (totalQueue) {
            while (totalQueue.getNum() < 6) {
                if (totalQueue.isEnd()) {
                    break;
                } else {
                    try {
                        totalQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            PersonQueue personQueue;
            Person person;
            int count;
            totalQueue.sort();
            if (totalQueue.isEnd()) {
                while (totalQueue.getNum() != 0) {
                    personQueue = getMinPersonQueue(personQueues);
                    count = 0;
                    synchronized (personQueue) {
                        while (count < 6 && totalQueue.getNum() != 0) {
                            person = totalQueue.getPerson();
                            personQueue.addPerson(person);
                            count++;
                        }
                        personQueue.notifyAll();
                    }
                }
            } else {
                while (totalQueue.getNum() >= 6) {
                    personQueue = getMinPersonQueue(personQueues);
                    count = 0;
                    synchronized (personQueue) {
                        while (count < 6 && totalQueue.getNum() != 0) {
                            person = totalQueue.getPerson();
                            personQueue.addPerson(person);
                            count++;
                        }
                        personQueue.notifyAll();
                    }
                }
            }
        }
    }
    
    public void removePersons(PersonQueue personQueue) {
        ArrayList<Person> tmp = new ArrayList<>();
        for (int i = 1;i <= 20;i++) {
            tmp = personQueue.getFloorQueue(i);
            if (tmp.size() != 0) {
                for (Person person:tmp) {
                    totalQueue.addPerson(person);
                }
                tmp.clear();
            }
        }
    }
    
    @Override
    public void run() {
        while (true) {
            synchronized (totalQueue) {
                PersonQueue personQueue;
                if (totalQueue.isEnd()) {
                    if (totalQueue.hasNewElevator()) {
                        for (int i = 0;i < personQueues.size();i++) {
                            personQueue = personQueues.get(i);
                            synchronized (personQueue) {
                                removePersons(personQueue);
                            }
                        }
                        totalQueue.setNewElevator(false);
                    }
                    if (totalQueue.getNum() == 0) {
                        for (int i = 0;i < personQueues.size();i++) {
                            personQueue = personQueues.get(i);
                            synchronized (personQueue) {
                                personQueue.setEnd(true);
                                personQueue.notifyAll();
                            }
                        }
                        return;
                    }
                } else {
                    if (totalQueue.hasNewElevator()) {
                        for (int i = 0;i < personQueues.size();i++) {
                            personQueue = personQueues.get(i);
                            synchronized (personQueue) {
                                removePersons(personQueue);
                            }
                        }
                        totalQueue.setNewElevator(false);
                    }
                    if (totalQueue.getNum() == 0) {
                        try {
                            totalQueue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            switch (mode) {
                case "Random":
                    randomMode();
                    break;
                case "Morning":
//                    morningMode();
                    randomMode();
                    break;
                case "Night":
                    nightMode();
                    break;
                default:
                    break;
            }
        }
    }
}
