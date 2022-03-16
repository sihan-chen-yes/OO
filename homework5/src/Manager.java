
public class Manager extends Thread {
    private final PersonQueue personQueue;
    private final WaitingQueue waitingQueue;
    private final Elevator elevator;
    private String mode;
    
    public Manager(PersonQueue personQueue, WaitingQueue waitingQueue, Elevator elevator) {
        this.personQueue = personQueue;
        this.waitingQueue = waitingQueue;
        mode = "";
        this.elevator = elevator;
    }
    
    public void setMode(String mode) {
        this.mode = mode;
    }
    
    public void updateDirection(int target) {
        int direction = elevator.getDirection();
        int targetFloor = elevator.getTargetFloor();
        if (direction == 1 && target > targetFloor) {
            elevator.setTargetFloor(target);
        } else if (direction == -1 && target < targetFloor) {
            elevator.setTargetFloor(target);
        }
    }
    
    public void addPerson(Person person) {
        int target = person.getTo();
        waitingQueue.addPerson(person);
        updateDirection(target);
    }
    
    public void judgeReqFwd() {
        int direction = elevator.getDirection();
        int locFloor = elevator.getLocFloor();
        synchronized (personQueue) {
            if (direction == 1) {
                for (int i = locFloor + 1;i <= 20;i++) {
                    if (!(personQueue.floorQueueEmpty(i))) {
                        elevator.setReqFwd(true);
                        return;
                    }
                }
            } else {
                for (int i = locFloor - 1; i >= 1; i--) {
                    if (!(personQueue.floorQueueEmpty(i))) {
                        elevator.setReqFwd(true);
                        return;
                    }
                }
            }
        }
    }
    
    public void randomHandler() {
        int direction = elevator.getDirection();
        int locFloor = elevator.getLocFloor();
        Person person;
        synchronized (waitingQueue) {
            while (waitingQueue.isFull()) {
                try {
                    waitingQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (personQueue) {
                while (!(waitingQueue.isFull()) && !(personQueue.floorQueueEmpty(locFloor))) {
                    person = personQueue.getPerson(locFloor,direction);
                    if (person == null) {
                        break;
                    } else {
                        addPerson(person);
                    }
                }
                judgeReqFwd();
                waitingQueue.notifyAll();
            }
        }
    }
    
    public void  morningHandler() {
        Person person;
        int locFloor = elevator.getLocFloor();
        synchronized (waitingQueue) {
            if (locFloor == 1) {
                while (!waitingQueue.isFull()) {
                    synchronized (personQueue) {
                        person = personQueue.getPerson(1, 1);
                        if (person == null) {
                            if (personQueue.isEnd()) {
                                break;
                            }
                            try {
                                personQueue.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            addPerson(person);
                        }
                    }
                }
            }
            judgeReqFwd();
            waitingQueue.notifyAll();
        }
    }
    
    public void nightHandler() {
        elevator.setNightMode(true);
        int locFloor = elevator.getLocFloor();
        Person person;
        synchronized (waitingQueue) {
            if (waitingQueue.isFull()) {
                try {
                    waitingQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (!(personQueue.isEmpty()) && !(waitingQueue.isFull())) {
                synchronized (personQueue) {
                    person = personQueue.getPerson(locFloor,-1);
                    if (person == null) {
                        break;
                    } else {
                        addPerson(person);
                    }
                }
            }
            judgeReqFwd();
            waitingQueue.notifyAll();
        }
    }
    
    @Override
    public void run() {
        while (true) {
            synchronized (personQueue) {
                if (personQueue.isEnd() && personQueue.isEmpty()) {
                    synchronized (waitingQueue) {
                        waitingQueue.setEnd(true);
                        waitingQueue.notifyAll();
                    }
                    return;
                }
                if (personQueue.isEmpty()) {
                    try {
                        personQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            switch (mode) {
                case "Random":
                    randomHandler();
                    break;
                case "Morning":
                    morningHandler();
                    break;
                case "Night":
                    nightHandler();
                    break;
                default:
                    break;
            }
        }
    }
}
