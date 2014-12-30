import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application{
    @Override
    public void start(Stage stage){
        VBox root = new VBox();
        Board board = new Board();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now){
                board.refresh();
            }
        };

        HBox toolbar = makeToolbar(board,timer, stage);

        root.getChildren().addAll(toolbar, board);
        stage.setScene(new Scene(root));
        stage.setTitle("Floating");
        stage.setMinWidth(Board.WIDTH * Cell.SIZE);
        stage.setMinHeight(Board.HEIGHT * Cell.SIZE + 50);
        stage.show();
        root.requestFocus();

        timer.stop();
    }

    private HBox makeToolbar(Board board, AnimationTimer timer, Stage stage){
        Button pause = new Button("Pause");
        pause.setOnMouseClicked(e -> timer.stop());

        Button play = new Button("Play");
        play.setOnMouseClicked(e -> timer.start());

        Button seed = new Button("Seed");
        seed.setOnMouseClicked(e -> board.seed());

        Button clear = new Button("Clear");
        clear.setOnMouseClicked(e -> board.clear());

        Button save = new Button("Save");
        save.setOnMouseClicked(e -> board.save(stage));

        Button load = new Button("Load");
        load.setOnMouseClicked(e -> board.load(stage));

        HBox ret = new HBox(10,play,pause,seed, clear, save, load);
        ret.setAlignment(Pos.CENTER);
        return ret;
    }
}