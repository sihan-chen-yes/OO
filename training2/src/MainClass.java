public class MainClass {
    public static void main(String[] args) {
        Plate plate = new Plate();
        new ProducerThread(plate).start();
        new ConsumerThread(plate).start();
    }
}
