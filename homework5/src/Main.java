
import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        PersonQueue personQueue = new PersonQueue();
        WaitingQueue waitingQueue = new WaitingQueue(6);
        Elevator elevator = new Elevator(waitingQueue,personQueue);
        Manager manager = new Manager(personQueue,waitingQueue,elevator);
        Receiver receiver = new Receiver(personQueue,manager);
        receiver.start();
        manager.start();
        elevator.start();
    }
}
