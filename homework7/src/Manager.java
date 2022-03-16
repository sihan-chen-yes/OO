
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
    
    public void addPerson(Person person) {
        waitingQueue.addPerson(person);
    }
    
    public void judgeReqFwd() {
        int direction = elevator.getDirection();
        int locFloor = elevator.getLocFloor();
        synchronized (personQueue) {
            if (direction == 1) {
                for (int i = locFloor + 1;i <= 20; i++) {
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
        elevator.setReqFwd(false);
    }
    
    public void randomHandler() {
        synchronized (waitingQueue) {
            while (elevator.isHandled()) {
                try {
                    waitingQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!personQueue.isEmpty()) {
                int need = elevator.getCapacity() - elevator.getNum();
                int direction = elevator.getDirection();
                int locFloor = elevator.getLocFloor();
                synchronized (personQueue) {
                    while (!personQueue.floorQueueEmpty(locFloor)
                            && waitingQueue.getLoadedNum() < need) {
                        Person person;
                        if (elevator.getNum() != 0) {
                            person = personQueue.getPerson(locFloor, direction);
                        } else {
                            person = personQueue.getPerson(locFloor,direction);
                            if (!(elevator.isReqFwd()) && person == null) {
                                elevator.changeDirection();
                                direction = elevator.getDirection();
                                person = personQueue.getPerson(locFloor,direction);
                            }
                        }
                        if (person == null) {
                            break;
                        } else {
                            addPerson(person);
                        }
                    }
                }
            }
            elevator.setHandled(true);
            judgeReqFwd();
            waitingQueue.notifyAll();
        }
    }
    
    public void nightHandler() {
        Person person;
        synchronized (waitingQueue) {
            while (elevator.isHandled()) {
                try {
                    waitingQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!personQueue.isEmpty()) {
                int locFloor = elevator.getLocFloor();
                int need = elevator.getCapacity() - elevator.getNum();
                int direction = elevator.getDirection();
                synchronized (personQueue) {
                    while (!personQueue.floorQueueEmpty(locFloor)
                            && waitingQueue.getLoadedNum() < need) {
                        person = personQueue.getPerson(locFloor,direction);
                        if (!(elevator.isReqFwd()) && person == null && elevator.getNum() == 0) {
                            elevator.changeDirection();
                            direction = elevator.getDirection();
                            person = personQueue.getPerson(locFloor,direction);
                        }
                        if (person == null) {
                            break;
                        } else {
                            addPerson(person);
                        }
                    }
                }
            }
            elevator.setHandled(true);
            judgeReqFwd();
            waitingQueue.notifyAll();
        }
    }
    
    @Override
    public void run() {
        while (true) {
            synchronized (personQueue) {
                if (personQueue.isEnd()) {
                    if (personQueue.isEmpty()) {
                        synchronized (waitingQueue) {
                            waitingQueue.setEnd(true);
                            waitingQueue.notifyAll();
                            return;
                        }
                    }
                } else {
                    if (personQueue.isWaitForTransfer()) {
                        personQueue.setWaitForTransfer(false);
                        synchronized (waitingQueue) {
                            waitingQueue.setTransferring(true);
                            waitingQueue.notifyAll();
                        }
                    } else {
                        synchronized (waitingQueue) {
                            waitingQueue.setTransferring(false);
                        }
                    }
                    if (personQueue.isEmpty()) {
                        try {
                            personQueue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            switch (mode) {
                case "Random":
                    randomHandler();
                    break;
                case "Morning":
                    randomHandler();
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
