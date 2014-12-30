import java.io.Serializable;

public class Cell implements Serializable{
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
