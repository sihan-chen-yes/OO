
public class ConsumerThread extends Thread {
    private final Plate plate;
    private static int id = 0;
    
    public ConsumerThread(Plate plate) {
        this.plate = plate;
    }
    
    @Override
    public void run() {
        try{
            while(id < 10) {
                plate.take();
                id++;
            }
        } catch (InterruptedException e) {
        }
    }
}
