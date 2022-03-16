import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.Request;

import java.io.IOException;
import java.util.ArrayList;

public class Receiver extends Thread {
    private final ElevatorInput elevatorInput;
    private final SimpleQueue totalQueue;
    
    public Receiver(ElevatorInput elevatorInput) {
        this.elevatorInput = elevatorInput;
        totalQueue = new SimpleQueue();
    }
    
    public void endReceiver() {
        synchronized (totalQueue) {
            try {
                totalQueue.setEnd(true);
                totalQueue.notifyAll();
                elevatorInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void createElevator(int id,String mode,
                               ArrayList<PersonQueue> personQueues,
                               ArrayList<Elevator> elevators,
                               String type) {
        PersonQueue personQueue = new PersonQueue();
        personQueues.add(personQueue);
        WaitingQueue waitingQueue = new WaitingQueue();
        Elevator elevator = new Elevator(waitingQueue,id,type,totalQueue);
        Manager manager = new Manager(personQueue,waitingQueue,elevator);
        manager.setMode(mode);
        elevators.add(elevator);
        elevator.start();
        manager.start();
    }
    
    @Override
    public void run() {
        ArrayList<PersonQueue> personQueueus = new ArrayList<>();
        ArrayList<Elevator> elevators = new ArrayList<>();
        String arrivePattern = elevatorInput.getArrivingPattern();
        String type;
        for (int i = 0;i < 3;i++) {
            if (i == 0) {
                type = "A";
            } else if (i == 1) {
                type = "B";
            } else {
                type = "C";
            }
            createElevator(i + 1,arrivePattern,personQueueus,elevators,type);
        }
        Dispatcher dispatcher = new Dispatcher(totalQueue,personQueueus,elevators,arrivePattern);
        dispatcher.start();
        TransferManager transferManager = new TransferManager();
        ArrayList<Person> managedPersons;
        Request request;
        Person person;
        while (true) {
            request = elevatorInput.nextRequest();
            if (request == null) {
                break;
            } else {
                if (request instanceof PersonRequest) {
                    int id = ((PersonRequest) request).getPersonId();
                    int from = ((PersonRequest) request).getFromFloor();
                    int to = ((PersonRequest) request).getToFloor();
                    person = new Person(id,from,to);
                    managedPersons = transferManager.genManagedPersons(person);
                    synchronized (totalQueue) {
                        for (Person route:managedPersons) {
                            totalQueue.addPerson(route);
                        }
                        totalQueue.notifyAll();
                    }
                } else if (request instanceof ElevatorRequest) {
                    int id = Integer.valueOf(((ElevatorRequest) request).getElevatorId());
                    type = ((ElevatorRequest) request).getElevatorType();
                    createElevator(id,arrivePattern,personQueueus,elevators,type);
                    synchronized (totalQueue) {
                        totalQueue.setNewElevator(true);
                        totalQueue.notifyAll();
                    }
                }
            }
        }
        endReceiver();
    }
}
