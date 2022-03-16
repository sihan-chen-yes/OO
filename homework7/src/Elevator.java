import com.oocourse.TimableOutput;
import java.util.ArrayList;

public class Elevator extends Thread {
    private final WaitingQueue waitingQueue;
    private final ArrayList<Person> passenger;
    private int locFloor;
    private int targetFloor;
    private int direction;
    private boolean opensignal;
    private boolean reqFwd;
    private boolean handled;
    private int id;
    private String type;
    private int movingCost;
    private int capacity;
    private SimpleQueue totalQueue;

    public Elevator(WaitingQueue waitingQueue,int id,String type,SimpleQueue totalQueue) {
        this.waitingQueue = waitingQueue;
        passenger = new ArrayList<>();
        locFloor = 1;
        targetFloor = 2;
        direction = 1;
        opensignal = false;
        reqFwd = false;
        handled = false;
        this.id = id;
        this.type = type;
        if (type.equals("A")) {
            movingCost = 600;
            capacity = 8;
        } else if (type.equals("B")) {
            movingCost = 400;
            capacity = 6;
        } else {
            movingCost = 200;
            capacity = 4;
        }
        this.totalQueue = totalQueue;
    }

    public void move() {
        try {
            Thread.sleep(movingCost);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        locFloor = locFloor + direction;
        TimableOutput.println("ARRIVE-" + locFloor + "-" + id);
    }
    
    public void open() {
        TimableOutput.println("OPEN-" + locFloor + "-" + id);
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
        TimableOutput.println("CLOSE-" + locFloor + "-" + id);
    }
    
    public void getIn() {
        ArrayList<Person> getInQueue = waitingQueue.getInQueue(
                locFloor,direction,capacity - passenger.size());
        for (Person person: getInQueue) {
            passenger.add(person);
            TimableOutput.println("IN-" + person.getId() + "-" + locFloor + "-" + id);
        }
        upDateTarget();
    }
    
    public void getOut() {
        ArrayList<Person> tmp = new ArrayList<>();
        for (int i = 0;i < passenger.size(); i++) {
            Person person = passenger.get(i);
            if (person.getTo() == locFloor) {
                tmp.add(person);
                TimableOutput.println("OUT-" + person.getId() + "-" + locFloor + "-" + id);
            }
        }
        for (Person person:tmp) {
            passenger.remove(person);
        }
        synchronized (totalQueue) {
            for (Person person:tmp) {
                if (person.getNextRoute() != null) {
                    person.getNextRoute().setValid(true);
                }
            }
            totalQueue.notifyAll();
        }
        waitingQueue.setTransferring(false);
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
    
    public int getDirection() {
        return direction;
    }
    
    public int getLocFloor() {
        return locFloor;
    }
    
    public int getNum() {
        return passenger.size();
    }
    
    public void changeDirection() {
        direction *= -1;
    }
    
    public void setReqFwd(boolean reqFwd) {
        this.reqFwd = reqFwd;
    }
    
    public void setDirection() {
        if (!reqFwd && passenger.isEmpty()) {
            changeDirection();
        }
        if ((locFloor == 20 && direction == 1) || (locFloor == 1 && direction == -1)) {
            changeDirection();
        }
    }
    
    public boolean getOffReq(int locFloor) {
        for (Person person:passenger) {
            if (person.getTo() == locFloor) {
                return true;
            }
        }
        return false;
    }
    
    public void setHandled(boolean handled) {
        this.handled = handled;
    }
    
    public boolean isHandled() {
        return handled;
    }
    
    public boolean isReqFwd() {
        return reqFwd;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public String getType() {
        return type;
    }
    
    @Override
    public void run() {
        while (true) {
            synchronized (waitingQueue) {
                if (passenger.size() == 0) {
                    waitingQueue.setTransferring(false);
                }
                if (!waitingQueue.isEnd() && handled == false && !waitingQueue.isTransferring()) {
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
                handled = false;
                waitingQueue.notifyAll();
            }
        }
    }
}
