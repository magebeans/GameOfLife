import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class Board extends Canvas {
    public static final int HEIGHT = 50;
    public static final int WIDTH = 50;
    private Cell[][] grid = new Cell[HEIGHT][WIDTH];
    private Cell[][] next = new Cell[HEIGHT][WIDTH];

    public Board(){
        super(HEIGHT*Cell.SIZE, WIDTH*Cell.SIZE);
        for(int i = 0; i < WIDTH; i++){
            for(int j = 0; j < HEIGHT; j++){
                grid[i][j] = new Cell(false);
            }
        }

        setOnMouseClicked(e -> toggleCell(e.getX(), e.getY()));
    }

    public void refresh(){
        final int[] offsets = {-1,0,1};

        for(int i = 0; i < WIDTH; i++){
            for(int j = 0; j < HEIGHT; j++){
                int count = 0;

                for(int k : offsets){
                    for(int l: offsets){
                       if(get(i+k,j+l).isAlive())
                           count++;
                    }
                }

                if(count == 3)
                    next[i][j] = new Cell(true);
                else if(count != 4)
                    next[i][j] = new Cell(false);
                else
                    next[i][j] = grid[i][j];
            }
        }

        grid = next;
        next = new Cell[WIDTH][HEIGHT];
        draw();
    }

    public void seed(){
        Random r = new Random();
        for(int i = 0;i < WIDTH; i++){
            for(int j = 0; j < HEIGHT; j++){
                if(r.nextBoolean()) grid[i][j].toggle();
            }
        }
    }

    public void clear(){
        for(int i = 0; i < WIDTH; i++){
            for(int j = 0; j < HEIGHT; j++){
                grid[i][j] = new Cell(false);
            }
        }
    }

    private Cell get(int x, int y){
        x = (x + WIDTH) % WIDTH;
        y = (y + HEIGHT) % HEIGHT;
        return grid[x][y];
    }

    private void draw(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0,0,getWidth(),getHeight());
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLANCHEDALMOND);
        int d = Cell.SIZE;
        for(int i = 0; i < WIDTH; i++){
            gc.strokeLine(i*d,0,i*d,getWidth());
            for(int j = 0; j < HEIGHT; j++){
                if(grid[i][j].isAlive())
                    gc.fillRect(i * d, j * d, d, d);
            }
        }

        for(int j = 0; j < HEIGHT; j++){
            gc.strokeLine(0,j*d,getWidth(),j*d);
        }
    }

    private void toggleCell(double x, double y){
        int i = (int) (x/Cell.SIZE);
        int j = (int) (y/Cell.SIZE);
        grid[i][j].toggle();
        draw();
    }
}
