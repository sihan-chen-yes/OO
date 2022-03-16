import com.oocourse.spec3.main.Runner;
import component.MyEmojiMessage;
import component.MyGroup;
import component.MyMessage;
import component.MyNetwork;
import component.MyNoticeMessage;
import component.MyPerson;
import component.MyRedEnvelopeMessage;

public class MainClass {
    public static void main(String[] args) throws Exception {
        Runner runner = new Runner(
                MyPerson.class,MyNetwork.class,MyGroup.class,
                MyMessage.class, MyEmojiMessage.class,
                MyNoticeMessage.class, MyRedEnvelopeMessage.class);
        runner.run();
    }
}

