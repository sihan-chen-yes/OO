public class Nest extends Function {
    private Function type;
    private Function content;
    
    public Nest(Function type, Function content) {
        this.type = type;
        this.content = content;
    }
    
    public Function getDerivation() {
        return new Multer(new Nest(type.getDerivation(), content), content.getDerivation());
    }
}
