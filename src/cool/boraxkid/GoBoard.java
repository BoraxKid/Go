package cool.boraxkid;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

//class definition for the Go board
class GoBoard extends Pane {
    // default constructor for the class
    public GoBoard() {
        this.pieces = new GoPiece[Go.GAME_BOARD_WIDTH][Go.GAME_BOARD_HEIGHT];
        this.horizontal = new Line[Go.GAME_BOARD_LINE_HEIGHT + 1];
        this.vertical = new Line[Go.GAME_BOARD_LINE_WIDTH + 1];
        this.horizontal_t = new Translate[Go.GAME_BOARD_LINE_HEIGHT + 1];
        this.vertical_t = new Translate[Go.GAME_BOARD_LINE_WIDTH + 1];

        this.initialiseLinesBackground();
        this.initialiseRender();

        this.gameLogic = new GameLogic(this);
    }

    public void reset() {
        this.gameLogic.resetGame();
    }

    // overridden version of the resize method to give the board the correct size
    @Override
    public void resize(double width, double height) {
        super.resize(width, height);

        double newWidth = width - Go.APPLICATION_BORDER;
        double newHeight = height - Go.APPLICATION_BORDER;

        if (width > height)
            newWidth = newHeight;
        else
            newHeight = newWidth;

        this.cell_width = newWidth / Go.GAME_BOARD_LINE_WIDTH;
        this.cell_height = newHeight / Go.GAME_BOARD_LINE_HEIGHT;

        this.start_x = (width / 2) - (newWidth / 2);
        this.start_y = (height / 2) - (newHeight / 2);

        this.getChildren().remove(this.background);
        this.background = new Rectangle(width, height);
        this.background.setFill(Go.GAME_BACKGROUND_COLOR);
        this.getChildren().add(0, this.background);

        this.horizontalResizeRelocate(newWidth);
        this.verticalResizeRelocate(newHeight);

        this.pieceResizeRelocate();
    }

    public void placePiece(final double x, final double y) {
        int cellX = (int)((x - this.start_x + (this.cell_width / 2.0)) / this.cell_width);
        int cellY = (int)((y - this.start_y + (this.cell_height / 2.0)) / this.cell_height);

        this.gameLogic.placePiece(cellX, cellY);
    }

    public void rightClick(final double x, final double y) {
        int cellX = (int)((x - this.start_x + (this.cell_width / 2.0)) / this.cell_width);
        int cellY = (int)((y - this.start_y + (this.cell_height / 2.0)) / this.cell_height);

        System.out.println("liberties: " + this.gameLogic.countLiberties(cellX, cellY));
    }

    // private method that will initialise the background and the lines
    private void initialiseLinesBackground() {
        this.background = new Rectangle(Go.APPLICATION_WIDTH, Go.APPLICATION_HEIGHT);
        this.background.setFill(Go.GAME_BACKGROUND_COLOR);
        this.getChildren().add(this.background);

        for (int i = 0; i < Go.GAME_BOARD_LINE_HEIGHT + 1; ++i) {
            this.horizontal[i] = new Line();

            this.horizontal[i].setStartX(0);
            this.horizontal[i].setStartY(0);
            this.horizontal[i].setEndY(0);

            this.horizontal_t[i] = new Translate(0, 0);
            this.horizontal[i].getTransforms().add(this.horizontal_t[i]);

            this.getChildren().add(this.horizontal[i]);
        }

        for (int i = 0; i < Go.GAME_BOARD_LINE_WIDTH + 1; ++i) {
            this.vertical[i] = new Line();

            this.vertical[i].setStartX(0);
            this.vertical[i].setEndX(0);
            this.vertical[i].setStartY(0);

            this.vertical_t[i] = new Translate(0, 0);
            this.vertical[i].getTransforms().add(this.vertical_t[i]);

            this.getChildren().add(this.vertical[i]);
        }
    }

    // private method for resizing and relocating the horizontal lines
    private void horizontalResizeRelocate(final double width) {
        for (int i = 0; i < Go.GAME_BOARD_LINE_HEIGHT + 1; ++i) {
            this.horizontal[i].setStartX(this.start_x);
            this.horizontal[i].setEndX(this.start_x + width);
            this.horizontal_t[i].setY(this.start_y + this.cell_height * i);
        }
    }

    // private method for resizing and relocating the vertical lines
    private void verticalResizeRelocate(final double height) {
        for (int i = 0; i < Go.GAME_BOARD_LINE_WIDTH + 1; ++i) {
            this.vertical[i].setStartY(this.start_y);
            this.vertical[i].setEndY(this.start_y + height);
            this.vertical_t[i].setX(this.start_x + this.cell_width * i);
        }
    }

    // private method for resizing and relocating all the pieces
    private void pieceResizeRelocate() {
        double cellX = this.cell_width * Go.GAME_PIECE_SCALE;
        double cellY = this.cell_height * Go.GAME_PIECE_SCALE;
        double offsetX = this.cell_width * ((1 - Go.GAME_PIECE_SCALE) / 2);
        double offsetY = this.cell_height * ((1 - Go.GAME_PIECE_SCALE) / 2);
        for (int i = 0; i < Go.GAME_BOARD_WIDTH; ++i) {
            for (int j = 0; j < Go.GAME_BOARD_HEIGHT; ++j) {
                this.pieces[i][j].resize(cellX, cellY);
                this.pieces[i][j].relocate(this.start_x + i * this.cell_width + offsetX - this.cell_width / 2.0, this.start_y + j * this.cell_height + offsetY - this.cell_height / 2.0);
            }
        }
    }

    // private method that will initialise everything in the render array
    private void initialiseRender() {
        for (int i = 0; i < Go.GAME_BOARD_WIDTH; ++i) {
            for (int j = 0; j < Go.GAME_BOARD_HEIGHT; ++j) {
                this.pieces[i][j] = new GoPiece(Go.GAME_EMPTY_SPACE);
                this.pieces[i][j].setX(i);
                this.pieces[i][j].setY(j);
            }
            this.getChildren().addAll(this.pieces[i]);
        }
    }

    // private fields that make the Go board work
    private GameLogic gameLogic;

    // rectangle that makes the background of the board
    private Rectangle background;
    // arrays for the lines that makeup the horizontal and vertical grid lines
    private Line[] horizontal;
    private Line[] vertical;
    // arrays holding translate objects for the horizontal and vertical grid lines
    private Translate[] horizontal_t;
    private Translate[] vertical_t;
    // arrays for the internal representation of the board and the pieces that are
    // in place
    public GoPiece[][] pieces;

    // the width and height of a cell in the board
    private double cell_width;
    private double cell_height;

    // Offset to center the board in the window
    private double start_x;
    private double start_y;
}
