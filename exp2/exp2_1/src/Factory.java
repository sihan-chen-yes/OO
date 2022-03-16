import java.util.List;

public class Factory {
    public static Vehicle getNew(List<String> ops) {
        String type = ops.get(1);
        if ("Car".equals(type)) {
            return new Car(Integer.valueOf(ops.get(2)),Integer.valueOf(ops.get(3)),Integer.valueOf(ops.get(4)));
            // TODO
        } else if ("Sprinkler".equals(type)) {
            return new Sprinkler(Integer.valueOf(ops.get(2)),Integer.valueOf(ops.get(3)),Integer.valueOf(ops.get(4)));
            // TODO
        } else {
            return new Bus(Integer.valueOf(ops.get(2)),Integer.valueOf(ops.get(3)),Integer.valueOf(ops.get(4)));
            // TODO
        }
    }
}
