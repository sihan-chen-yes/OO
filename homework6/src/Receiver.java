import com.oocourse.TimableOutput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.Request;

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
                               ArrayList<Elevator> elevators) {
        PersonQueue personQueue = new PersonQueue();
        personQueues.add(personQueue);
        WaitingQueue waitingQueue = new WaitingQueue();
        Elevator elevator = new Elevator(waitingQueue,id);
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
        for (int i = 0;i < 3;i++) {
            createElevator(i + 1,arrivePattern,personQueueus,elevators);
        }
        Dispatcher dispatcher = new Dispatcher(totalQueue,personQueueus,elevators,arrivePattern);
        dispatcher.start();
        Request request;
        Person person;
        TimableOutput.initStartTimestamp();
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
                    synchronized (totalQueue) {
                        totalQueue.addPerson(person);
                        totalQueue.notifyAll();
                    }
                } else if (request instanceof ElevatorRequest) {
                    int id = Integer.valueOf(((ElevatorRequest) request).getElevatorId());
                    createElevator(id,arrivePattern,personQueueus,elevators);
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
