package cool.boraxkid;

import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;

//class definition for a custom Go control
class GoControl extends Control {
    // constructor for the class
    public GoControl() {
        this.setSkin(new GoControlSkin(this));

        this.gb_board = new GoBoard();
        this.getChildren().add(this.gb_board);

        this.setOnMouseClicked((event) -> {
            this.gb_board.placePiece(event.getX(), event.getY());
        });

        this.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.SPACE)
                this.gb_board.reset();
        });
    }

    // overridden version of the resize method
    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        this.gb_board.resize(width, height);
    }

    // private fields of a go board
    GoBoard gb_board;
}
