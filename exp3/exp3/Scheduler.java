import java.util.ArrayList;

public class Scheduler extends Thread {
    private final WaitQueue waitQueue;
    private final ArrayList<ProcessingQueue> processingQueues;

    public Scheduler(WaitQueue waitQueue,
                    ArrayList<ProcessingQueue> processingQueues) {
        this.waitQueue = waitQueue;
        this.processingQueues = processingQueues;
    }

    @Override
    public void run() {
        ArrayList<Request> temp = new ArrayList<>();
        while (true) {
            synchronized (waitQueue) {
                if (waitQueue.isEnd() && waitQueue.noWaiting()) {
                    System.out.println("Schedule over");
                    for (ProcessingQueue processingQueue : processingQueues) {
                        synchronized (processingQueue) {
                            processingQueue.notifyAll();
                            // need to complete (1)
                        }
                    }
                    return;
                }
                if (waitQueue.noWaiting()) {
                    try {
                        waitQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    temp.addAll(waitQueue.getRequests());
                    for (int i = 0; i < temp.size(); i++) {
                        Request request = temp.get(i);
                        if (request.getDestination().equals("Beijing")) {
                            synchronized (processingQueues.get(0)/* need to complete (2)*/) {
                                processingQueues.get(0).addRequest(request);
                                // need to complete (3)
                            }
                        } else if (request.getDestination().equals("Domestic")) {
                            synchronized (processingQueues.get(1)/* need to complete (4)*/) {
                                processingQueues.get(1).addRequest(request);
                                // need to complete (5)
                            }
                        } else {
                            synchronized (processingQueues.get(2)/* need to complete (6)*/) {
                                processingQueues.get(2).addRequest(request);
                                // need to complete (7)
                            }
                        }
                        temp.remove(request);
                        i--;
                    }
                    waitQueue.clearQueue();
                }
            }
        }
    }
}


