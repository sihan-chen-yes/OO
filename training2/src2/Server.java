import java.util.ArrayList;

public class Server implements Observerable {
    private ArrayList<Observer> list = new ArrayList<>();
    
    @Override
    public void addObserver(Observer observer) {
        list.add(observer);
    }
    
    @Override
    public void removeObserver(Observer observer) {
        list.remove(observer);
    }
    
    @Override
    public void notifyObserver(String msg) {
        System.out.println("Server:" + msg + "\n");
        for (Observer e:list) {
            e.update(msg);
        }
    }
}
