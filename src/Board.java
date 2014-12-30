import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.Random;

public class Board extends Canvas implements Serializable{
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
        draw();
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
        draw();
    }

    public void clear(){
        for(int i = 0; i < WIDTH; i++){
            for(int j = 0; j < HEIGHT; j++){
                grid[i][j] = new Cell(false);
            }
        }
        draw();
    }

    public void save(Stage stage){
        FileChooser fileChooser= new FileChooser();
        fileChooser.setTitle("Save board configuration");
        File file = fileChooser.showSaveDialog(stage);
        if (file != null){
            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
                out.writeObject(this);
                out.close();
            } catch (IOException e){
                errorDialog("Error saving configuration " + e.getMessage());
            }
        }
    }

    public void load(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load board configuration");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null){
            try{
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                Board temp = (Board) in.readObject();
                grid = temp.grid;
                in.close();
            } catch (IOException|ClassNotFoundException c){
                errorDialog("Error loading file" + c.getMessage());
            }
        }
        draw();
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

    private void errorDialog(String errorString){
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);

        Button button = new Button("Okay");
        button.setOnAction(e -> dialog.close());

        VBox vBox = new VBox(5,
                new Text(errorString),
                button);
        vBox.setAlignment(Pos.CENTER);

        dialog.setTitle("Floating: Error");
        dialog.setScene(new Scene(vBox));
        dialog.sizeToScene();
        dialog.show();
    }
}
