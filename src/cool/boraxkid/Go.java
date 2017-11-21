package cool.boraxkid;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

//class defnition for Go game
public class Go extends Application {
    // overridden init method
    public void init() {
        this.sp_mainlayout = new StackPane();
        this.gc_go = new GoControl();
        this.sp_mainlayout.getChildren().add(this.gc_go);
    }

    // overridden start method
    public void start(Stage primaryStage) {
        primaryStage.setTitle(Go.APPLICATION_TITLE);
        primaryStage.setScene(new Scene(this.sp_mainlayout, Go.APPLICATION_WIDTH, Go.APPLICATION_HEIGHT));
        primaryStage.show();
    }

    // overridden stop method
    public void stop() {

    }

    // entry point into our program for launching our javafx applicaton
    public static void main(String[] args) {
        launch(args);
    }

    // static fields
    public static String APPLICATION_TITLE = "Go";
    public static int APPLICATION_WIDTH = 800;
    public static int APPLICATION_HEIGHT = 800;
    public static int APPLICATION_BORDER = 120;
    public static Color GAME_BACKGROUND_COLOR = Color.BEIGE;
    public static RadialGradient GAME_WHITE_COLOR = new RadialGradient(0.5, 0.5, 0, 0, 1.5, true, CycleMethod.REFLECT, new Stop(0, Color.WHITE), new Stop(1, Color.GREY));
    public static RadialGradient GAME_BLACK_COLOR = new RadialGradient(0.5, 0.5, 0, 0, 1.5, true, CycleMethod.REFLECT, new Stop(0, Color.DARKSLATEGREY), new Stop(1, Color.BLACK));
    public static double GAME_PIECE_SCALE = 0.95;
    public static int GAME_EMPTY_SPACE = 0;
    public static int GAME_WHITE_PLAYER = 1;
    public static int GAME_BLACK_PLAYER = 2;
    public static int GAME_BOARD_LINE_WIDTH = 7;
    public static int GAME_BOARD_LINE_HEIGHT = 7;
    public static int GAME_BOARD_WIDTH = 8;
    public static int GAME_BOARD_HEIGHT = 8;

    // private fields for a stack pane and a Go control
    private StackPane sp_mainlayout;
    private GoControl gc_go;

}
