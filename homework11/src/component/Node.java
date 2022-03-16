package component;

public class Node implements Comparable<Node> {
    private int id;
    private int dis;
    
    public Node(int id, int dis) {
        this.id = id;
        this.dis = dis;
    }
    
    public int getId() {
        return id;
    }
    
    public int getDis() {
        return dis;
    }
    
    @Override
    public int compareTo(Node o) {
        int odis = o.getDis();
        if (dis < odis) {
            return -1;
        } else if (dis == odis) {
            return 0;
        } else {
            return 1;
        }
    }
}
