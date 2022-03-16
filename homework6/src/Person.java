public class Person {
    private int id;
    private int from;
    private int to;
    private int direction;
    
    public Person(int id,int from,int to) {
        this.id = id;
        this.from = from;
        this.to = to;
        if (this.to > this.from) {
            direction = 1;
        } else {
            direction = -1;
        }
    }
    
    public int getId() {
        return id;
    }
    
    public int getFrom() {
        return from;
    }
    
    public int getTo() {
        return to;
    }
    
    public int getDirection() {
        return direction;
    }
}
