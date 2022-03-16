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
    
    public ArrayList<Integer> getTypePersonQueues(String type) {
        ArrayList<Integer> index = new ArrayList<>();
        for (int i = 0;i < personQueues.size();i++) {
            if (type == elevators.get(i).getType()) {
                index.add(i);
            }
        }
        return index;
    }
    
    public ArrayList<Integer> getSuitablePersonQueue(Person person) {
        ArrayList<Integer> index = getTypePersonQueues(person.getElevatorType());
        int direction = person.getDirection();
        int loc = person.getFrom();
        ArrayList<Integer> suitableIndex = new ArrayList<>();
        Elevator elevator;
        for (int i = 0;i < index.size();i++) {
            elevator = elevators.get(index.get(i));
            if (direction == elevator.getDirection()) {
                if (direction == 1 && loc > elevator.getLocFloor()) {
                    suitableIndex.add(index.get(i));
                } else if (direction == -1 && loc < elevator.getLocFloor()) {
                    suitableIndex.add(index.get(i));
                }
            }
        }
        if (suitableIndex.size() == 0) {
            return index;
        }
        return suitableIndex;
    }
    
    public PersonQueue getMinPersonQueue(ArrayList<Integer> suitableIndex) {
        PersonQueue minQueue = personQueues.get(suitableIndex.get(0));
        int num = minQueue.getNum();
        PersonQueue personQueue;
        for (int i = 1;i < suitableIndex.size();i++) {
            personQueue = personQueues.get(suitableIndex.get(i));
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
            synchronized (personQueues) {
                while (totalQueue.hasValidReq()) {
                    person = totalQueue.getPerson();
                    personQueue = getMinPersonQueue(getSuitablePersonQueue(person));
                    synchronized (personQueue) {
                        personQueue.addPerson(person);
                    }
                }
                for (int i = 0;i < personQueues.size();i++) {
                    personQueue = personQueues.get(i);
                    synchronized (personQueue) {
                        personQueue.setWaitForTransfer(false);
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
    
    public void addedEle() {
        PersonQueue personQueue;
        for (int i = 0;i < personQueues.size();i++) {
            personQueue = personQueues.get(i);
            synchronized (personQueue) {
                removePersons(personQueue);
            }
        }
        totalQueue.setNewElevator(false);
    }
    
    @Override
    public void run() {
        while (true) {
            synchronized (totalQueue) {
                PersonQueue personQueue;
                if (totalQueue.isEnd()) {
                    if (totalQueue.hasNewElevator()) {
                        addedEle();
                    }
                    if (totalQueue.getNum() == 0) {
                        for (int i = 0;i < personQueues.size();i++) {
                            personQueue = personQueues.get(i);
                            synchronized (personQueue) {
                                personQueue.setEnd(true);
                                personQueue.setWaitForTransfer(false);
                                personQueue.notifyAll();
                            }
                        }
                        return;
                    }
                    if (!totalQueue.hasValidReq()) {
                        for (int i = 0;i < personQueues.size();i++) {
                            personQueue = personQueues.get(i);
                            synchronized (personQueue) {
                                personQueue.setWaitForTransfer(true);
                                personQueue.notifyAll();
                            }
                        }
                        synchronized (totalQueue) {
                            try {
                                totalQueue.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    if (totalQueue.hasNewElevator()) {
                        addedEle();
                    }
                    if (totalQueue.getNum() == 0) {
                        try {
                            totalQueue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (!totalQueue.hasValidReq()) {
                        try {
                            totalQueue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            randomMode();
        }
    }
}
