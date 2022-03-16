public class ProducerThread extends Thread {
    private final Plate plate;
    private static int id = 1;
    
    public ProducerThread(Plate plate) {
        this.plate = plate;
    }
    
    @Override
    public void run() {
        try{
            while(id <= 10) {
                plate.put(id++);
                sleep((int)(Math.random()*100));
            }
        } catch (InterruptedException e) {
        }
    }
}
