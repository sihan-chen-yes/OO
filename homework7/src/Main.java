import com.oocourse.TimableOutput;
import com.oocourse.elevator3.ElevatorInput;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        boolean debug = true;
        ElevatorInput elevatorInput;
        if (debug == true) {
            PipedOutputStream myPipeOut = new PipedOutputStream();
            PipedInputStream myPipeIn = new PipedInputStream();
            elevatorInput = new ElevatorInput(myPipeIn);
            try {
                myPipeOut.connect(myPipeIn);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
            new Thread(new DebugInput(myPipeOut)).start();
        } else {
            elevatorInput = new ElevatorInput(System.in);
        }
        Receiver receiver = new Receiver(elevatorInput);
        receiver.start();
    }
}
