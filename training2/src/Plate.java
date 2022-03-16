import java.util.ArrayList;

public class Plate {
    private final int[] list;
    private boolean full;
    
    public Plate() {
        this.list = new int[1];
        this.full = false;
    }
    
    public synchronized void put(int id) throws InterruptedException {
        while (full) {
            wait();
        }
        list[0] = id;
        full = true;
        System.out.println("Producer put:" + id);
        notifyAll();
    }
    
    public synchronized void take() throws InterruptedException {
        while(!(full)) {
            wait();
        }
        full = false;
        System.out.println("Consumer get:" + list[0]);
        notifyAll();
    }
}
