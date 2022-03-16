public class Main {
    public static void main(String[] args) {
        Server server = new Server(); //该类实现Observerable接⼝
        Observer user1 = new User("user1"); //该类实现Observer接⼝
        Observer user2 = new User("user2"); //该类实现Observer接⼝
        server.addObserver(user1);
        server.addObserver(user2);
        server.notifyObserver("北航的OO课是世界上最好的OO课！");
        server.removeObserver(user2);
        server.notifyObserver("Java是世界上最好⽤的语⾔。");
    }
}
