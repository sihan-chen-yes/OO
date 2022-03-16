
import com.oocourse.TimableOutput;
import java.util.ArrayList;

public class Elevator extends Thread {
    private final WaitingQueue waitingQueue;
    private final ArrayList<Person> passenger;
    private int locFloor;
    private int targetFloor;
    private int direction;
    private boolean opensignal;
    private boolean nightMode;
    private boolean reqFwd;

    public Elevator(WaitingQueue waitingQueue, PersonQueue personQueue) {
        this.waitingQueue = waitingQueue;
        passenger = new ArrayList<>();
        locFloor = 1;
        targetFloor = 2;
        direction = 1;
        opensignal = false;
        nightMode = false;
        reqFwd = false;
    }

    public void move() {
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        locFloor = locFloor + direction;
        TimableOutput.println("ARRIVE-" + locFloor);
    }
    
    public void open() {
        TimableOutput.println("OPEN-" + locFloor);
        getOut();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void close() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getIn();
        TimableOutput.println("CLOSE-" + locFloor);
    }
    
    public void getIn() {
        int tmp = direction;
        if (nightMode) {
            tmp = -1;
        }
        ArrayList<Person> getInQueue = waitingQueue.getInQueue(locFloor,tmp,6 - passenger.size());
        for (Person person: getInQueue) {
            passenger.add(person);
            TimableOutput.println("IN-" + person.getId() + "-" + locFloor);
        }
        upDateDirection();
        upDateTarget();
    }
    
    public void getOut() {
        ArrayList<Person> tmp = new ArrayList<>();
        for (int i = 0;i < passenger.size(); i++) {
            Person person = passenger.get(i);
            if (person.getTo() == locFloor) {
                tmp.add(person);
                TimableOutput.println("OUT-" + person.getId() + "-" + locFloor);
            }
        }
        for (Person person:tmp) {
            passenger.remove(person);
        }
    }
    
    public void upDateDirection() {
        if (!passenger.isEmpty()) {
            Person person = passenger.get(0);
            direction = person.getDirection();
        }
    }
    
    public void upDateTarget() {
        if (direction == 1) {
            for (Person person:passenger) {
                if (person.getTo() > targetFloor) {
                    targetFloor = person.getTo();
                }
            }
        } else {
            for (Person person:passenger) {
                if (person.getTo() < targetFloor) {
                    targetFloor = person.getTo();
                }
            }
        }
    }
    
    public void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }
    
    public int getDirection() {
        return direction;
    }
    
    public int getLocFloor() {
        return locFloor;
    }
    
    public int getTargetFloor() {
        return targetFloor;
    }
    
    public void changeDirection() {
        direction *= -1;
    }
    
    public void setReqFwd(boolean reqFwd) {
        this.reqFwd = reqFwd;
    }
    
    public void setDirection() {
        if (nightMode) {
            direction = 1;
            if (passenger.size() == 6 || !reqFwd) {
                direction = -1;
            }
        }
        if ((locFloor == 20 && direction == 1) || (locFloor == 1 && direction == -1)) {
            changeDirection();
        }
    }
    
    public void setNightMode(boolean nightMode) {
        this.nightMode = nightMode;
    }
    
    public boolean getOffReq(int locFloor) {
        for (Person person:passenger) {
            if (person.getTo() == locFloor) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void run() {
        while (true) {
            synchronized (waitingQueue) {
                if (!(waitingQueue.isEnd())) {
                    try {
                        waitingQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (getOffReq(locFloor) || !waitingQueue.floorQueueEmpty(locFloor)) {
                    opensignal = true;
                }
                if (opensignal == true) {
                    open();
                    close();
                    opensignal = false;
                }
                if (waitingQueue.isEnd() && waitingQueue.isEmpty() && passenger.isEmpty()) {
                    break;
                }
                setDirection();
                if (reqFwd || !passenger.isEmpty()) {
                    move();
                }
            }
        }
    }
}
