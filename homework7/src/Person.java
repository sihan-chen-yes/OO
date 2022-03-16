public class Person {
    private int id;
    private int from;
    private int to;
    private int direction;
    private String elevatorType;
    private boolean valid;
    private boolean priority;
    private Person nextRoute;
    
    public Person(int id,int from,int to) {
        this.id = id;
        this.from = from;
        this.to = to;
        if (this.to > this.from) {
            direction = 1;
        } else {
            direction = -1;
        }
        elevatorType = "";
        valid = false;
        priority = false;
        nextRoute = null;
    }
    
    public String getElevatorType() {
        return elevatorType;
    }
    
    public void setElevatorType(String elevatorType) {
        this.elevatorType = elevatorType;
    }
    
    public boolean isPriority() {
        return priority;
    }
    
    public void setPriority(boolean priority) {
        this.priority = priority;
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    public void setNextRoute(Person nextRoute) {
        this.nextRoute = nextRoute;
    }
    
    public Person getNextRoute() {
        return nextRoute;
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
