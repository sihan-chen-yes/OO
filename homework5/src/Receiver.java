import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class Receiver extends Thread {
    private final ElevatorInput elevatorInput;
    private final PersonQueue personQueue;
    private final Manager manager;
    
    public Receiver(PersonQueue personQueue,Manager manager) {
        elevatorInput = new ElevatorInput(System.in);
        this.personQueue = personQueue;
        this.manager = manager;
    }
    
    public void endReceiver() {
        synchronized (personQueue) {
            try {
                personQueue.setEnd(true);
                personQueue.notifyAll();
                elevatorInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void run() {
        String arrivePattern = elevatorInput.getArrivingPattern();
        manager.setMode(arrivePattern);
        PersonRequest request;
        Person person;
        while (true) {
            request = elevatorInput.nextPersonRequest();
            if (request == null) {
                break;
            } else {
                int id = request.getPersonId();
                int from = request.getFromFloor();
                int to = request.getToFloor();
                person = new Person(id,from,to);
                synchronized (personQueue) {
                    personQueue.addPerson(person);
                    personQueue.notifyAll();
                }
            }
        }
        endReceiver();
    }
}
