import com.oocourse.spec2.main.Runner;
import component.MyGroup;
import component.MyMessage;
import component.MyNetwork;
import component.MyPerson;

public class MainClass {
    public static void main(String[] args) throws Exception {
        Runner runner = new Runner(MyPerson.class,MyNetwork.class,MyGroup.class,MyMessage.class);
        runner.run();
    }
}

