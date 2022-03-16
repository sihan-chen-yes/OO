public class Car extends Vehicle {
    private int maxSpeed;

    Car(int id, int price, int maxSpeed) {
        super(id,price);
        this.maxSpeed = maxSpeed;
        // TODO
    }

    @Override
    public void run() {
        System.out.println("Wow, I can Run (maxSpeed:" + maxSpeed + ")!");
    }

    @Override
    public int getPrice() {
        int p = super.getPrice();
        if (maxSpeed >= 1000) {
            p += 1000;
        }
        System.out.println("price is: " + p);
        return p;
        // TODO
    }
}
