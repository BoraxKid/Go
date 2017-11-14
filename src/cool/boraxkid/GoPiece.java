package cool.boraxkid;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Translate;

//class definition for a Go piece
class GoPiece extends Group {
    // default constructor for the class
    public GoPiece(int player) {
        this.player = player;
        this.piece = new Ellipse();
        this.t = new Translate();
        this.piece.getTransforms().add(this.t);
        this.getChildren().add(this.piece);
        this.setPiece(this.player);
    }

    // overridden version of the resize method to give the piece the correct size
    @Override
    public void resize(double width, double height) {
        super.resize(width, height);

        this.piece.setCenterX(width / 2.0);
        this.piece.setCenterY(height / 2.0);
        this.piece.setRadiusX(width / 2.0);
        this.piece.setRadiusY(height / 2.0);
    }

    // overridden version of the relocate method to position the piece correctly
    @Override
    public void relocate(double x, double y) {
        super.relocate(x, y);

        this.t.setX(x);
        this.t.setY(y);
    }

    // public method that will swap the color and type of this piece
    public void swapPiece() {
        if (this.player != Go.GAME_EMPTY_SPACE)
            this.setPiece(this.player == Go.GAME_WHITE_PLAYER ? Go.GAME_BLACK_PLAYER : Go.GAME_WHITE_PLAYER);
    }

    // method that will set the piece type
    public void setPiece(final int type) {
        this.player = type;
        if (this.player == Go.GAME_EMPTY_SPACE)
            this.piece.setFill(Color.TRANSPARENT);
        else
            this.piece.setFill(this.player == Go.GAME_WHITE_PLAYER ? Go.GAME_WHITE_COLOR : Go.GAME_BLACK_COLOR);
    }

    // returns the type of this piece
    public int getPiece() {
        return (this.player);
    }

    // private fields
    private int player;		// the player that this piece belongs to
    private Ellipse piece;	// ellipse representing the player's piece
    private Translate t;	// translation for the player piece
}
