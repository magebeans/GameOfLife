public class Cell {
    private boolean alive;
    public static final int SIZE = 15;

    public Cell(boolean alive){
        this.alive = alive;
    }

    public boolean toggle(){
        alive = !alive;
        return alive;
    }

    public boolean isAlive(){
        return alive;
    }
}
